package com.googlecode.n_orm.redis;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import redis.clients.jedis.Jedis;
import com.googlecode.n_orm.DatabaseNotReachedException;
import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Constraint;
import com.googlecode.n_orm.storeapi.Store;
import org.apache.commons.codec.binary.Base64;

// <table> -> liste ordonnée avec (poids-> id)
// <table>:<id>:families -> un set de string
// <table>:<id>:<column family>:keys -> un sorted set de string
// <table>:<id>:<column family>:vals -> un hash de string -> string

public class RedisStore implements Store {
	private Jedis redisInstance;
	private static final String SEPARATOR = ":";
	private static enum DataTypes {
		keys, vals
	}
	private static final String FAMILIES = "families";
	
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

	@Override
	public CloseableKeyIterator get(String table, Constraint c, int limit,
			Set<String> families) throws DatabaseNotReachedException {
		// TODO Auto-generated method stub
		return null;
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
		Map<String, String> redisResult = this.redisInstance.hgetAll(this.getKey(table, id, family, DataTypes.vals));
		Map<String, byte[]> result = new HashMap<String, byte[]>();
		
		// Convert String to byte[] for values
		for (Map.Entry<String, String> e : redisResult.entrySet()) {
			result.put(e.getKey(), this.decodeFromRedis(e.getValue()));
		}
		return result;
	}

	@Override
	public Map<String, byte[]> get(String table, String id, String family,
			Constraint c) throws DatabaseNotReachedException {
		// TODO Auto-generated method stub
		// la contrainte porte sur les clés dans la famille
		return null;
	}

	/**
	 * Returns all the values associated to the families for an specified id
	 */
	@Override
	public Map<String, Map<String, byte[]>> get(String table, String id,
			Set<String> families) throws DatabaseNotReachedException {
		
		Map<String, Map<String, byte[]>> result = new HashMap<String, Map<String, byte[]>>();
		Map<String, byte[]> familyResult;
		String[] familyKeys;
		List<String> familyRedisResult;
		
		// Iteration on families
		for(String family : families ){
			familyResult = new HashMap<String, byte[]>();
			// get keys associated to the family <table>:<id>:<family>:keys
			familyKeys = 		this.redisInstance.zrangeByScore(this.getKey(table, id, family, DataTypes.keys), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).toArray(new String[0]);
			familyRedisResult = this.redisInstance.hmget(this.getKey(table, id, family, DataTypes.vals), familyKeys);
			
			// add and convert values from the family to a map
			for(int i = 0; i < familyKeys.length; i++) {
				familyResult.put(familyKeys[i], this.decodeFromRedis(familyRedisResult.get(i)));
			}
			result.put(family, familyResult);
		}
		
		return result;
	}

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
		
		if(increments != null) {
			// Increment the values
			for(Map.Entry<String, Map<String, Number>> family : increments.entrySet()) {
				for(Entry<String, Number> familyKey : family.getValue().entrySet()) {
					this.redisInstance.hincrBy( this.getKey(table, id, family.getKey(), DataTypes.vals), familyKey.getKey(), familyKey.getValue().longValue());
				}
			}
		}
	}

	@Override
	public void delete(String table, String id)
			throws DatabaseNotReachedException {
		// delete :
		// - <table>
		List<String> keysToBeDeleted = new ArrayList<String>();
		keysToBeDeleted.add(this.getKey(table));
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
	
	public String encodeToRedis(byte[] data) {
		return Base64.encodeBase64String(data);
	}
	
	public byte[] decodeFromRedis(String data) {
		return Base64.decodeBase64(data);
	}

}
