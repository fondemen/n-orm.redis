package com.googlecode.n_orm.redis;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Tuple;

import com.googlecode.n_orm.DatabaseNotReachedException;
import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Constraint;
import com.googlecode.n_orm.storeapi.Store;

import com.googlecode.n_orm.conversion.ConversionTools;
import com.googlecode.n_orm.redis.RowWrapper;
import com.googlecode.n_orm.redis.CloseableIterator;

import org.apache.commons.codec.binary.Base64;

// <table> -> liste ordonnée avec (poids-> id)
// <table>:<id>:families -> un set de string
// <table>:<id>:<column family>:keys -> un sorted set de string
//<table>:<id>:<column family>:vals -> un hash de string -> string
//<table>:<id>:<column family>:increments -> un hash de string -> string

public class RedisStore implements Store {
	private Jedis redisInstance;
	private static final String SEPARATOR = ":";

	public static enum DataTypes {
		keys, vals, increments
	}

	private static final String FAMILIES = "families";
	protected static Store store;
	protected boolean isWriting = false;
	protected Pipeline writingTransaction;

	/**
	 * Instanciate a unique RedisStore and return it
	 * 
	 * @return the RedisStore
	 */
	public static Store getStore() {
		if (store == null) {
			RedisStore.store = new RedisStore();
		}
		return store;
	}

	protected Jedis getReadableRedis() {
		// if(this.isWriting) {
		// this.writingTransaction.sync();
		// this.isWriting = false;
		// }

		return this.redisInstance;
	}

	/*
	 * protected Pipeline getWritableRedis() { if(this.writingTransaction ==
	 * null) this.writingTransaction = this.redisInstance.pipelined();
	 * 
	 * this.isWriting = true; return this.writingTransaction; }
	 */
	protected Jedis getWritableRedis() {

		return this.redisInstance;
	}

	/**
	 * Start the Jedis Instance
	 * 
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public void start() throws DatabaseNotReachedException {
		this.redisInstance = new Jedis("localhost");

	}

	/**
	 * Test if an id exists in the table
	 * <table>
	 */
	@Override
	public boolean exists(String table, String id)
			throws DatabaseNotReachedException {
		return (this.getReadableRedis().zscore(this.getKey(table), id) != null);
	}

	/**
	 * Test if a family exists for the element <id> in the table
	 * <table>
	 * (test if
	 * <table>
	 * :<id>:<family>:keys exist
	 */
	@Override
	public boolean exists(String table, String id, String family)
			throws DatabaseNotReachedException {
		return (this.getReadableRedis().exists(this.getKey(table, id, family,
				DataTypes.keys)));
	}

	/**
	 * Get an iterator on a list of Row specified with a Constraint, and for a
	 * list of families
	 * 
	 * @param table
	 * @param c
	 * @param limit
	 * @param families
	 * @return the iteator
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public CloseableKeyIterator get(String table, Constraint c, int limit,
			Set<String> families) throws DatabaseNotReachedException {

		int rangeMin = (c != null && c.getStartKey() != null) ? this.idToRank(
				table, c.getStartKey(), false) : 0;
		int rangeMax = (c != null && c.getEndKey() != null) ? this.idToRank(
				table, c.getEndKey(), true) : Integer.MAX_VALUE;

		// méthode pour chercher entre 2 clés :
		// on insère les clés
		// on demande le rang de chaque clé
		// on récupère les résultats entre les 2 clés

		List<RowWrapper> rows = new ArrayList<RowWrapper>();

		Set<Tuple> redisResults;
		if (rangeMax == -1)
			redisResults = new TreeSet<Tuple>();
		else
			redisResults = this.getReadableRedis().zrangeWithScores(
					this.getKey(table), rangeMin, rangeMax);

		for (Tuple redisResult : redisResults) {
			Map<String, Map<String, byte[]>> element = this.get(table,
					redisResult.getElement(), families);
			// Add the element only if its not null
			// if(element != null)
			rows.add(new RowWrapper(redisResult.getElement(), element));

		}

		return new CloseableIterator(rows);
	}

	/**
	 * Get the value associated to the key, for the (table, id, family) n-uplet
	 * returns the value or null if the value do not exist
	 * 
	 * @param table
	 * @param id
	 * @param family
	 * @param key
	 * @return
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public byte[] get(String table, String id, String family, String key)
			throws DatabaseNotReachedException {
		String result = this.getReadableRedis().hget(
				this.getKey(table, id, family, DataTypes.vals), key);
		return (result != null) ? this.decodeFromRedis(table, id, family, key,
				result) : null;
	}

	/**
	 * Get a Map of {key => value} for a specified id and a specified family
	 * 
	 * @param table
	 * @param id
	 * @param family
	 * @return
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public Map<String, byte[]> get(String table, String id, String family)
			throws DatabaseNotReachedException {
		// get keys associated to the family <table>:<id>:<family>:keys
		Set<String> familyKeys = this.getReadableRedis().zrangeByScore(
				this.getKey(table, id, family, DataTypes.keys),
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return this.getWithKeys(table, id, family,
				familyKeys.toArray(new String[0]));
	}

	/**
	 * Get a Map of {key => value] with a specified table, id, family and set of
	 * keys
	 * 
	 * @param table
	 * @param id
	 * @param family
	 * @param keys
	 * @return
	 */
	public Map<String, byte[]> getWithKeys(String table, String id,
			String family, String[] keys) {

		Map<String, byte[]> familyResult = new HashMap<String, byte[]>();

		if (keys.length == 0)
			return familyResult;

		List<String> familyRedisResult = this.getReadableRedis().hmget(
				this.getKey(table, id, family, DataTypes.vals), keys);

		// add and convert values from the family to a map
		for (int i = 0; i < keys.length; i++) {
			familyResult.put(keys[i], this.decodeFromRedis(table, id, family,
					keys[i], familyRedisResult.get(i)));
		}
		return familyResult;
	}

	/**
	 * Get a specified row from a specified table and family with rows specified
	 * with a Constraint
	 * 
	 * @param table
	 * @param id
	 * @param family
	 * @param c
	 * @return
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public Map<String, byte[]> get(String table, String id, String family,
			Constraint c) throws DatabaseNotReachedException {
		// la contrainte porte sur les clés dans la famille
		int rangeMin = (c != null && c.getStartKey() != null) ? this
				.columnToRank(table, id, family, c.getStartKey(), false) : 0;
		int rangeMax = (c != null && c.getEndKey() != null) ? this
				.columnToRank(table, id, family, c.getEndKey(), true)
				: Integer.MAX_VALUE;

		Set<String> keys = this.getReadableRedis().zrange(
				this.getKey(table, id, family, DataTypes.keys), rangeMin,
				rangeMax);

		return this.getWithKeys(table, id, family, keys.toArray(new String[0]));
	}

	/**
	 * Returns all the values associated to the families for an specified id
	 */
	@Override
	public Map<String, Map<String, byte[]>> get(String table, String id,
			Set<String> families) throws DatabaseNotReachedException {

		Map<String, Map<String, byte[]>> result = new HashMap<String, Map<String, byte[]>>();

		// If the family set is not, give all the families
		if (families == null)
			families = this.getFamilies(table, id);

		// Iteration on families
		Map<String, byte[]> keys;
		for (String family : families) {
			keys = this.get(table, id, family);
			if (keys.size() > 0)
				result.put(family, keys);
		}

		// Return null if no result
		if (result.size() == 0)
			return null;

		return result;
	}

	/**
	 * Return the list of the families associated to an id and a table
	 * 
	 * @param table
	 * @param id
	 * @return Set of family (or empty set)
	 */
	protected Set<String> getFamilies(String table, String id) {
		Set<String> families = this.getReadableRedis().smembers(
				this.getKey(table, id));
		return (families != null) ? families : new TreeSet<String>();
	}

	/**
	 * save changes in the database : - add or update the "changed" field -
	 * remove the "removed" fields - increments the Integer fields
	 * 
	 * @param table
	 * @param id
	 * @param changed
	 * @param removed
	 * @param increments
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public void storeChanges(String table, String id,
			Map<String, Map<String, byte[]>> changed,
			Map<String, Set<String>> removed,
			Map<String, Map<String, Number>> increments)

	throws DatabaseNotReachedException {

		synchronized (this.redisInstance) {
				
			// Parallelize the insert in the database
			Pipeline pipelineRedis = this.redisInstance.pipelined();
	
			if (changed != null) {
	
				// Add the key
				pipelineRedis.zadd(this.getKey(table), this.idToScore(id), id);
	
				// Add changed rows for each families
				for (Map.Entry<String, Map<String, byte[]>> family : changed
						.entrySet()) {
					Map<String, String> dataToBeInserted = new HashMap<String, String>();
	
					// Convert Map<String, byte[]> to Map<String, String> before
					// inserting
					for (Map.Entry<String, byte[]> key : family.getValue()
							.entrySet()) {
						dataToBeInserted.put(key.getKey(),
								this.encodeToRedis(key.getValue()));
					}
					// add the family in the set of family
					pipelineRedis.sadd(this.getKey(table, id), family.getKey());
	
					// add the set of keys
					for (String redisKey : family.getValue().keySet()) {
						pipelineRedis.zadd(this.getKey(table, id, family.getKey(),
								DataTypes.keys), this.columnToScore(redisKey),
								redisKey);
					}
	
					// add the { key => value } hashmap
					pipelineRedis
							.hmset(this.getKey(table, id, family.getKey(),
									DataTypes.vals), dataToBeInserted);
	
				}
			}
	
			if (removed != null) {
				// Remove the keys
				for (Entry<String, Set<String>> family : removed.entrySet()) {
					for (String redisKey : family.getValue()) {
						// remove from the list of keys...
						pipelineRedis.zrem(this.getKey(table, id, family.getKey(),
								DataTypes.keys), redisKey);
						// ... and from the hashmap
						pipelineRedis.hdel(this.getKey(table, id, family.getKey(),
								DataTypes.vals), redisKey);
	
						pipelineRedis.hdel(this.getKey(table, id, family.getKey(),
								DataTypes.increments), redisKey);
					}
				}
			}
	
			if (increments != null) {
				// Increment the values
				for (Map.Entry<String, Map<String, Number>> family : increments
						.entrySet()) {
	
					// Add the key
					pipelineRedis.zadd(this.getKey(table), this.idToScore(id), id);
	
					// if the family does not exist, create it
					pipelineRedis.sadd(this.getKey(table, id), family.getKey());
	
					// add the set of keys
					for (String redisKey : family.getValue().keySet()) {
						pipelineRedis.zadd(this.getKey(table, id, family.getKey(),
								DataTypes.keys), this.columnToScore(redisKey),
								redisKey);
					}
	
					for (Entry<String, Number> familyKey : family.getValue()
							.entrySet()) {
						// Increment directly in the "increments" database
						pipelineRedis.hincrBy(this.getKey(table, id,
								family.getKey(), DataTypes.increments), familyKey
								.getKey(), familyKey.getValue().longValue());
					}
				}
			}
			pipelineRedis.sync();
		}
	}

	/**
	 * Delete a row from a table
	 * 
	 * @param table
	 * @param id
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public void delete(String table, String id)
			throws DatabaseNotReachedException {
		// delete :
		// - <table>
		List<String> keysToBeDeleted = new ArrayList<String>();

		keysToBeDeleted.add(this.getKey(table, id));

		Set<String> families = this.getReadableRedis().smembers(
				this.getKey(table, id));
		for (String family : families) {
			// - <table>:<id>:<column family>:keys
			keysToBeDeleted.add(this.getKey(table, id, family, DataTypes.keys));
			// - <table>:<id>:<column family>:vals
			keysToBeDeleted.add(this.getKey(table, id, family, DataTypes.vals));
			// - <table>:<id>:<column family>:increments
			keysToBeDeleted.add(this.getKey(table, id, family,
					DataTypes.increments));
		}
		this.redisInstance.del(keysToBeDeleted.toArray(new String[0]));

		this.redisInstance.zrem(this.getKey(table), id);
	}

	/**
	 * Return the number of the rows specified with a Constraint
	 * 
	 * @param table
	 * @param c
	 * @return
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public long count(String table, Constraint c)
			throws DatabaseNotReachedException {

		int rangeMin = (c != null && c.getStartKey() != null) ? this.idToRank(
				table, c.getStartKey(), false) : 0;
		int rangeMax = (c != null && c.getEndKey() != null) ? this.idToRank(
				table, c.getEndKey(), true) : Integer.MAX_VALUE;

		return this.getReadableRedis()
				.zrange(this.getKey(table), rangeMin, rangeMax).size();
	}

	public void flushAll() {
		this.redisInstance.flushAll();
	}

	/**
	 * Return the redis keys for the elements'keys for the table
	 * 
	 * @param table
	 * @return liste ordonnée avec (poids-> id)
	 */
	protected String getKey(String table) {
		return table;
	}

	/**
	 * Return the redis key for the list of families for the id
	 * 
	 * @param table
	 * @param id
	 * @return table:id:families -> un set de string
	 */
	protected String getKey(String table, String id) {
		return table + SEPARATOR + id + SEPARATOR + FAMILIES;
	}

	/**
	 * 
	 * @param table
	 * @param id
	 * @param family
	 * @param type
	 * @return <table>
	 *         :<id>:<column family>:vals -> hash de string -> string
	 *         <table>
	 *         :<id>:<column family>:keys -> un set de strings
	 */
	protected String getKey(String table, String id, String family,
			DataTypes type) {
		return table + SEPARATOR + id + SEPARATOR + FAMILIES + SEPARATOR
				+ family + SEPARATOR + type.name();
	}

	public double idToScore(String id) {
		return 0;
	}

	public double columnToScore(String id) {
		return 0;
	}

	/**
	 * Return the rank of a id
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public int idToRank(String table, String id, Boolean endSearch) {
		return this.redisKeyToRank(this.getKey(table), id, endSearch);
	}

	/**
	 * Return the rank of a column
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public int columnToRank(String table, String id, String family, String key,
			Boolean endSearch) {
		return this.redisKeyToRank(
				this.getKey(table, id, family, DataTypes.keys), key, endSearch);
	}

	public int redisKeyToRank(String redisKey, String key, Boolean endSearch) {
		// Remember if the key already exists
		boolean alreadyExists = (this.redisInstance.zadd(redisKey, 0, key) == 0);

		// Add the key and rememb


		// get the rank of the freshly inserted id
		Long rank = this.getReadableRedis().zrank(redisKey, key);

		// if the value do not already exists, remove 1 from the rank
		// if we want the previous value (endSearch)
		if (!alreadyExists && endSearch)
			rank--;

		// Remove the key if it was not already there
		if (!alreadyExists)
			this.redisInstance.zrem(redisKey, key);

		return rank.intValue();
	}

	/**
	 * Encode binary data to Base64 String
	 * 
	 * @param data
	 * @return
	 */
	public String encodeToRedis(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * Decode Base64-encoded String to binary data
	 * 
	 * @param data
	 * @return
	 */
	public byte[] decodeFromRedis(String table, String id, String family,
			String row, String data) {
		byte[] decodedData;
		// If the data is saved in the "vals" hash, decode frop base64
		if (data != null && data.length() != 0)
			decodedData = Base64.decodeBase64(data);
		else { // The data is null, we search in the increment table
			String incrementData = this.redisInstance.hget(
					this.getKey(table, id, family, DataTypes.increments), row);
			if (incrementData == null || incrementData.length() == 0)
				return new byte[] {};

			decodedData = ConversionTools.convert(
					Long.parseLong(incrementData), Long.class);
		}
		if (decodedData != null)
			return decodedData;
		else
			return new byte[] {};
	}

}
