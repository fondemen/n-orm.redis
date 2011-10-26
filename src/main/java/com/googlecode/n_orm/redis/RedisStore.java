package com.googlecode.n_orm.redis;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import com.googlecode.n_orm.DatabaseNotReachedException;
import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Constraint;
import com.googlecode.n_orm.storeapi.Store;

import com.googlecode.n_orm.redis.RowWrapper;
import com.googlecode.n_orm.redis.CloseableIterator;

import org.apache.commons.codec.binary.Base64;

// <table> -> liste ordonnée avec (poids-> id)
// <table>:<id>:families -> un set de string
// <table>:<id>:<column family>:keys -> un sorted set de string
// <table>:<id>:<column family>:vals -> un hash de string -> string

public class RedisStore implements Store {
	private Jedis redisInstance;
	private static final String SEPARATOR = ":";
	public static enum DataTypes {
		keys, vals
	}
	private static final String FAMILIES = "families";
	protected static Store store;
	
	/**
	 * Instanciate a unique RedisStore and return it
	 * @return the RedisStore
	 */
	public static Store getStore() {
		if(store == null) {
			RedisStore.store = new RedisStore();
		}
		return store;
	}
	
	/**
	 * Start the Jedis Instance
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public void start() throws DatabaseNotReachedException {
		this.redisInstance = new Jedis("localhost");
		
	}

	/**
	 * Test if an id exists in the table <table>
	 */
	@Override
	public boolean exists(String table, String id)
			throws DatabaseNotReachedException {
		System.out.println("test existence of"+table+", "+id);
		return (this.redisInstance.zscore(this.getKey(table), id) != null);
	}

	/**
	 * Test if a family exists for the element <id> in the table <table>
	 * (test if <table>:<id>:<family>:keys exist
	 */
	@Override
	public boolean exists(String table, String id, String family)
			throws DatabaseNotReachedException {
		return (this.redisInstance.exists(this.getKey(table, id, family, DataTypes.keys)));
	}

	/**
	 * Get an iterator on a list of Row specified with a Constraint, and for a list of families
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
		
		double min = (c != null && c.getStartKey() != null) ? this.idToScore(c.getStartKey())	: Double.MIN_VALUE;
		double max = (c != null && c.getEndKey() != null)   ? this.idToScore(c.getEndKey()) 	: Double.MAX_VALUE;
		List<RowWrapper> rows = new ArrayList<RowWrapper>();
		
		Set<Tuple> redisResults = this.redisInstance.zrangeByScoreWithScores(this.getKey(table), min, max, 0, limit);
		
		for(Tuple redisResult : redisResults) {
			rows.add(new RowWrapper(redisResult.getElement(), this.get(table, redisResult.getElement(), families)));
			
		}
		
		
		return new CloseableIterator(rows);
	}

	/**
	 * Get the value associated to the key, for the (table, id, family) n-uplet
	 * returns the value or null if the value do not exist
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
		String result = this.redisInstance.hget(this.getKey(table, id, family, DataTypes.vals), key);
		return (result != null) ? this.decodeFromRedis(result) : null;
	}

	/**
	 * Get a Map of {key => value} for a specified id and a specified family
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
		Set<String> familyKeys = this.redisInstance.zrangeByScore(this.getKey(table, id, family, DataTypes.keys), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		return this.getWithKeys(table, id, family, familyKeys.toArray(new String[0]));
	}

	/**
	 * Get a Map of {key => value] with a specified table, id, family and set of keys
	 * @param table
	 * @param id
	 * @param family
	 * @param keys
	 * @return
	 */
	public Map<String, byte[]> getWithKeys(String table, String id, String family, String[] keys) {

		Map<String, byte[]> familyResult = new HashMap<String, byte[]>();
		
		if(keys.length == 0)
			return familyResult;
		
		List<String> familyRedisResult = this.redisInstance.hmget(this.getKey(table, id, family, DataTypes.vals), keys);
		
		// add and convert values from the family to a map
		for(int i = 0; i < keys.length; i++) {
			familyResult.put(keys[i], this.decodeFromRedis(familyRedisResult.get(i)));
		}
		return familyResult;
	}

	/**
	 * Get a specified row from a specified table and family with rows specified with a Constraint
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
		double min = (c != null && c.getStartKey() != null) ? this.idToScore(c.getStartKey())	: Double.MIN_VALUE;
		double max = (c != null && c.getEndKey() != null)   ? this.idToScore(c.getEndKey()) 	: Double.MAX_VALUE;
		Map<String, byte[]> result = new HashMap<String, byte[]>();
		
		Set<String> keys = this.redisInstance.zrangeByScore(this.getKey(table, id, family, DataTypes.keys), min, max);
		
		return this.getWithKeys(table, id, family, keys.toArray(new String[0]));
	}

	/**
	 * Returns all the values associated to the families for an specified id
	 */
	@Override
	public Map<String, Map<String, byte[]>> get(String table, String id,
			Set<String> families) throws DatabaseNotReachedException {
		
		Map<String, Map<String, byte[]>> result = new HashMap<String, Map<String, byte[]>>();
		
		if(families == null)
			return null;
		
		// Iteration on families
		Map<String, byte[]> keys;
		for(String family : families ){
			keys = this.get(table, id, family);
			if(keys.size() > 0)
				result.put(family, keys);
		}
		
		if(result.size() == 0)
			return null;
		
		return result;
	}

	/**
	 * save changes in the database :
	 *  - add or update the "changed" field
	 *  - remove the "removed" fields
	 *  - increments the Integer fields
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
		
		if(changed != null) {
			// Add changed rows for each families
			for(Map.Entry<String, Map<String, byte[]>> family : changed.entrySet()) {
				Map<String, String> dataToBeInserted = new HashMap<String, String>();
				
				// Convert Map<String, byte[]> to Map<String, String> before inserting
				for(Map.Entry<String, byte[]> key : family.getValue().entrySet()) {
					dataToBeInserted.put(key.getKey(), this.encodeToRedis(key.getValue()));
				}
				// add the family in the set of family
				this.redisInstance.sadd(this.getKey(table, id), family.getKey());
				
				// add the set of keys
				for(String redisKey : family.getValue().keySet()) {
					this.redisInstance.zadd(this.getKey(table, id, family.getKey(), DataTypes.keys), this.idToScore(redisKey), redisKey);
				}
				
				// add the { key => value } hashmap
				this.redisInstance.hmset(this.getKey(table, id, family.getKey(), DataTypes.vals), dataToBeInserted);
				
				this.redisInstance.zadd(this.getKey(table), this.idToScore(id), id);
			}
		}
		
		if(removed != null) {
			// Remove the keys
			for(Entry<String, Set<String>> family : removed.entrySet()) {
				for(String redisKey : family.getValue()) {
					// remove from the list of keys...
					this.redisInstance.zrem(this.getKey(table, id, family.getKey(), DataTypes.keys), redisKey);
					// ... and from the hashmap
					this.redisInstance.hdel(this.getKey(table, id, family.getKey(), DataTypes.vals), redisKey);
				}
			}
		}
		
		double number;
		if(increments != null) {
			// Increment the values
			for(Map.Entry<String, Map<String, Number>> family : increments.entrySet()) {
				for(Entry<String, Number> familyKey : family.getValue().entrySet()) {
					byte[] redisValue = this.get(table, id, family.getKey(), familyKey.getKey());
					if(redisValue != null)
						number = Double.parseDouble(new String(redisValue));
					else
						number = 0;
					this.redisInstance.hset( this.getKey(table, id, family.getKey(), DataTypes.vals), familyKey.getKey(),
							this.encodeToRedis(Double.toString((number+familyKey.getValue().doubleValue())).getBytes()));
				}
			}
		}
	}

	/**
	 * Delete a row from a table
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
		
		Set<String> families = this.redisInstance.smembers(this.getKey(table, id));
		for(String family : families) {
			// - <table>:<id>:<column family>:keys
			keysToBeDeleted.add(this.getKey(table,id,family,DataTypes.keys));
			// - <table>:<id>:<column family>:vals 
			keysToBeDeleted.add(this.getKey(table,id,family,DataTypes.vals));
		}
		this.redisInstance.del(keysToBeDeleted.toArray(new String[0]));
		
		this.redisInstance.zrem(this.getKey(table), id);
	}

	/**
	 * Return the number of the rows specified with a Constraint
	 * @param table
	 * @param c
	 * @return
	 * @throws DatabaseNotReachedException
	 */
	@Override
	public long count(String table, Constraint c)
			throws DatabaseNotReachedException {

		double min = (c != null && c.getStartKey() != null) ? this.idToScore(c.getStartKey())	: Double.MIN_VALUE;
		double max = (c != null && c.getEndKey() != null)   ? this.idToScore(c.getEndKey()) 	: Double.MAX_VALUE;
		
		return this.redisInstance.zcount(this.getKey(table), min, max);
	}
	
	public void flushAll() {
		this.redisInstance.flushAll();
	}
	
	/**
	 * Return the redis keys for the elements'keys for the table
	 * @param table
	 * @return liste ordonnée avec (poids-> id)
	 */
	protected String getKey(String table) {
		return table;
	}
	
	/**
	 *  Return the redis key for the list of families for the id
	 * @param table
	 * @param id
	 * @return <table>:<id>:families -> un set de string
	 */
	protected String getKey(String table, String id) {
		return table+SEPARATOR+id+SEPARATOR+FAMILIES;
	}
	
	/**
	 *  
	 * @param table
	 * @param id
	 * @param family
	 * @param type
	 * @return <table>:<id>:<column family>:vals -> hash de string -> string
	 * <table>:<id>:<column family>:keys -> un set de strings
	 */
	protected String getKey(String table, String id, String family, DataTypes type) {
		return table+SEPARATOR+id+SEPARATOR+FAMILIES+SEPARATOR+family+SEPARATOR+type.name();
	}
	
	public double idToScore(String id) {		
		byte[] byteId = id.getBytes();
		double score = 0;
		
		for(int i = 0; i < byteId.length; i++) {
			// Addition en commencant par le bit de poids ford et en décalant à droite
			// en décimal de 2^8, taille d'un byte
			score += (byteId[i] - Byte.MIN_VALUE) * Math.pow(2, 1000 - 8*i);

		}
		
		return score;
	}
	
	/**
	 * Encode binary data to Base64 String
	 * @param data
	 * @return
	 */
	public String encodeToRedis(byte[] data) {
		return Base64.encodeBase64String(data);
	}
	
	/**
	 * Decode Base64-encoded String to binary data  
	 * @param data
	 * @return
	 */
	public byte[] decodeFromRedis(String data) {
		return Base64.decodeBase64(data);
	}

}
