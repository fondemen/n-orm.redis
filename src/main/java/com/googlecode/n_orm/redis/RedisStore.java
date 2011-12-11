package com.googlecode.n_orm.redis;

import java.util.ArrayList;

//import java.util.Calendar;
//import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import com.googlecode.n_orm.DatabaseNotReachedException;
import com.googlecode.n_orm.EmptyCloseableIterator;
import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Constraint;
import com.googlecode.n_orm.storeapi.Row;
import com.googlecode.n_orm.storeapi.Store;

import com.googlecode.n_orm.conversion.ConversionTools;
import com.googlecode.n_orm.redis.RowWrapper;
import com.googlecode.n_orm.redis.CloseableIterator;

import org.apache.commons.codec.binary.Base64;

// <table> -> liste ordonnée avec (poids-> id)
// <table>:<id>:families -> un set de string
// <table>:<id>:<column family>:k = new Jedis("localhost")eys -> un sorted set de string
//<table>:<id>:<column family>:vals -> un hash de string -> string
//<table>:<id>:<column family>:increments -> un hash de string -> string

public class RedisStore implements Store {
	private static final String SEPARATOR = ":";
	private JedisProxy fakeJedis = new JedisProxy("localhost");
	public static enum DataTypes {
		keys, vals, increments
	}

	private static final String FAMILIES = "families";
	private static final int DEFAULT_ID_SCORE = 0;
	private static final int DEFAULT_COLUMN_SCORE = 0;
	protected static Store store;
	protected boolean isWriting = false;
	protected Pipeline writingTransaction;
	//private List<SimpleEntry<JedisProxy, Date>> availableJedis  = new ArrayList<SimpleEntry<JedisProxy, Date>>();
	//private static Calendar calendar = Calendar.getInstance();
	
	public static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
	
	
	/**
	 * get an available jedis instance from the available pool or create one if no one available
	 * 
	 * @return an available instance
	 */
/*	public Jedis getAvailableJedis() {
		return pool.getResource();
	}
*/
	/**
	 * Put the instance into the available pool
	 * 
	 * @param jedisProxyInstance
	 */
	/*
	public void releaseInstance(JedisProxy jedisPoolInstance) {
		pool.returnResource();
	}
	*/
	/**
	 * Instanciate a unique RedisStore and return it
	 * 
	 * @return the RedisStore
	 */
	public static Store getStore() {
		if(RedisStore.store == null) {
			RedisStore.store = new RedisStore();
		}
		return store;
	}

	public JedisProxy getReadableRedis() {
		// if(this.isWriting) {
		// this.writingTransaction.sync();
		// this.isWriting = false;
		// }
		return fakeJedis;
	}

	/*
	 * protected Pipeline getWritableRedis() { if(this.writingTransaction ==
	 * null) this.writingTransaction = this.redisInstance.pipelined();
	 * 
	 * this.isWriting = true; return this.writingTransaction; }
	 */
	protected Jedis getWritableRedis() {
		return fakeJedis;
	}


	

	/**
	 * Start the Jedis Instance
	 * 
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public void start() throws DatabaseNotReachedException {
		// Do nothing
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
				table, c.getStartKey(), false) : DEFAULT_ID_SCORE;
		int rangeMax = (c != null && c.getEndKey() != null) ? this.idToRank(
				table, c.getEndKey(), true) : Integer.MAX_VALUE;

		// méthode pour chercher entre 2 clés :
		// on insère les clés
		// on demande le rang de chaque clé
		// on récupère les résultats entre les 2 clés

		String startKey, stopKey;
		
		if(rangeMax == -1) {
			// no result, return empty Iterator
			return new EmptyCloseableIterator();
		}
		
		Set<String> startKeys = this.getReadableRedis().zrange(
				this.getKey(table), rangeMin, rangeMin);
		if(startKeys.size() == 0)
			return new EmptyCloseableIterator();
		else
			startKey = startKeys.iterator().next();
		
		// FIXME HERE
		Set<String> stopKeys = this.getReadableRedis().zrange(
				this.getKey(table), rangeMax, rangeMax);
		if(stopKeys.size() == 0)
			stopKey = null;
		else
			stopKey = stopKeys.iterator().next();


		return new CloseableIterator(RedisStore.this, startKey, stopKey, table, limit, families);
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
				.columnToRank(table, id, family, c.getStartKey(), false) : DEFAULT_COLUMN_SCORE;
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
	 * Get the element startKey and the maxBulk next elements
	 * @param table
	 * @param startKey
	 * @param families2
	 * @param maxBulk
	 */
	public List<Row> get(String table, String startKey, String stopKey, Set<String> families2,
			int maxBulk, boolean excludeFirstElement) {
		List<Row> result = new ArrayList<Row>();
		int delta = maxBulk;
		
		int firstRank = this.idToRank(table, startKey, false);
		if(excludeFirstElement)
			firstRank++;
		
		int lastRank;
		if(stopKey != null)
			lastRank = this.idToRank(table, stopKey, true);
		else
			lastRank = Integer.MAX_VALUE; 
			
		//System.out.println("1st rank: "+firstRank+"("+startKey+"), last rank: "+lastRank+"("+stopKey+")");
		if(lastRank == -1)
			return result;
		
		if(lastRank < firstRank)
			return result;
		
		if(lastRank - firstRank < maxBulk)
			delta = lastRank - firstRank + 1;
		
		Set<String> redisKeys = this.getReadableRedis().zrange(this.getKey(table), firstRank, firstRank + delta - 1);
		
		for(String key : redisKeys) {
			result.add(new RowWrapper(key, this.get(table, key, families2)));
		}
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
				
		// Parallelize the insert in the database
		// Pipeline pipelineRedis = this.getWritableRedis().pipelined();
		Jedis pipelineRedis = this.getWritableRedis();
		
		if (changed != null) {

			// Add the key
			pipelineRedis.zadd(this.getKey(table), this.idToScore(id), id);
			pipelineRedis.sadd(this.getKey(table, id),"");
					
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
		// pipelineRedis.sync();
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
		this.getWritableRedis().del(keysToBeDeleted.toArray(new String[0]));

		this.getWritableRedis().zrem(this.getKey(table), id);
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
				table, c.getStartKey(), false) : DEFAULT_ID_SCORE;
		int rangeMax = (c != null && c.getEndKey() != null) ? this.idToRank(
				table, c.getEndKey(), true) : Integer.MAX_VALUE;

		return this.getReadableRedis()
				.zrange(this.getKey(table), rangeMin, rangeMax).size();
	}

	public void flushAll() {
		this.getWritableRedis().flushAll();
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
	 *         <table>:<id>:<column family>:keys -> un set de strings
	 */
	protected String getKey(String table, String id, String family,
			DataTypes type) {
		return table + SEPARATOR + id + SEPARATOR + FAMILIES + SEPARATOR
				+ family + SEPARATOR + type.name();
	}

	public double idToScore(String id) {
		return DEFAULT_ID_SCORE;
	}

	public double columnToScore(String id) {
		return DEFAULT_COLUMN_SCORE;
	}

	/**
	 * Return the rank of a id
	 * 
	 * @param table
	 * @param id
	 * @return
	 */
	public int idToRank(String table, String id, Boolean endSearch) {
		return this.redisKeyToRank(this.getReadableRedis().exists(this.getKey(table, id)), this.getKey(table), id, endSearch);
	}

	/**
	 * Return the rank of a column
	 * synchro because of the add/remove dring the search
	 * @param table
	 * @param id
	 * @return
	 */
	public int columnToRank(String table, String id, String family, String key,
			Boolean endSearch) {
		boolean alreadyExists = this.getReadableRedis().exists(this.getKey(table, id, family, DataTypes.keys))
				&&
					(this.getReadableRedis().hexists(this.getKey(table, id, family, DataTypes.vals), key)
						||
					this.getReadableRedis().hexists(this.getKey(table, id, family, DataTypes.increments), key)
					);
		return this.redisKeyToRank(
				alreadyExists,
				this.getKey(table, id, family, DataTypes.keys), key, endSearch);
	}

	/**
	 * Return the rank of a potential key
	 * @param existingKey : this redis key has to exist if the object exists
	 * @param hashKey     : the hashset where the keys are stored
	 * @param id		  : the id of the searchable element
	 * @param endSearch   : is it a start or a stop search
	 * @return
	 */
	protected int redisKeyToRank(boolean existingKey, String hashKey, String id, Boolean endSearch) {
		
		// Remember if the key already exists
		if(existingKey) {
			// the key is existing, return the rank :
			
			Long rank = this.getReadableRedis().zrank(hashKey, id);
			return rank.intValue();
		} else {
			// the keys does not exist
			// Add the key
			this.getWritableRedis().zadd(hashKey, 0, id);
	
			// get the rank of the freshly inserted id
			Long rank = this.getReadableRedis().zrank(hashKey, id);
	
			// if the value do not already exists, remove 1 from the rank
			// if we want the previous value (endSearch)
			if (endSearch)
				rank--;
	
			// Remove the key
			this.getWritableRedis().zrem(hashKey, id);
	
			return rank.intValue();
		}

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
			String incrementData = this.getReadableRedis().hget(
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
