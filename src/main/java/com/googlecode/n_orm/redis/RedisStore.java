package com.googlecode.n_orm.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.codec.binary.Base64;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import com.googlecode.n_orm.DatabaseNotReachedException;
import com.googlecode.n_orm.conversion.ConversionTools;
import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Constraint;
import com.googlecode.n_orm.storeapi.DefaultColumnFamilyData;
import com.googlecode.n_orm.storeapi.Row;
import com.googlecode.n_orm.storeapi.Row.ColumnFamilyData;
import com.googlecode.n_orm.storeapi.SimpleStore;

// [table] -> liste ordonnée avec (poids-> id)
// [table]:families -> un set de string
// [table]:[id]:[column family]:keys -> un sorted set de string
// [table]:[id]:[column family]:vals -> un hash de string -> string
// [table]:[id]:[column family]:increments -> un hash de string -> string

public class RedisStore implements SimpleStore {
	private static final String SEPARATOR = ":";

	public static enum DataTypes {
		keys, vals, increments
	}

	private static final String FAMILIES = "families";
	private static final int DEFAULT_ID_SCORE = 0;
	private static final int DEFAULT_COLUMN_SCORE = 0;

	public static ConcurrentMap<Properties, RedisStore> stores = new ConcurrentHashMap<Properties, RedisStore>();
	public static JedisPoolConfig poolConfig = new JedisPoolConfig();

	protected int scanCaching = 50;

	protected boolean isWriting = false;

	public JedisPool pool;

	/**
	 * Instantiate a unique RedisStore and return it
	 * 
	 * @return the RedisStore
	 */
	public static RedisStore getStore() {
		return getStore(new Properties());
	}

	public static RedisStore getStore(String host) {
		Properties p = new Properties();
		p.put("host", host);
		return getStore(p);
	}

	public static RedisStore getStore(String host, int port) {
		Properties p = new Properties();
		p.put("host", host);
		p.put("port", port);
		return getStore(p);
	}

	public static RedisStore getStore(String host, int port, int timeout,
			String password) {
		Properties p = new Properties();
		p.put("host", host);
		p.put("port", port);
		p.put("timeout", timeout);
		p.put("password", password);
		return getStore(p);
	}

	public static RedisStore getStore(String host, int port, int timeout,
			String password, int database) {
		Properties p = new Properties();
		p.put("host", host);
		p.put("port", port);
		p.put("timeout", timeout);
		p.put("password", password);
		p.put("database", database);
		return getStore(p);
	}

	public static RedisStore getStore(Properties p) {
		RedisStore store = stores.get(p);
		if (store == null) {
			store = new RedisStore();
			RedisStore s = stores.putIfAbsent(p, store);
			if (s != null)
				store = s;
			else {
				Integer port;
				try {
					port = (Integer) p.get("port");
				} catch (ClassCastException x) {
					port = null;
				}
				Integer timeout;
				try {
					timeout = (Integer) p.get("timeout");
				} catch (ClassCastException x) {
					timeout = null;
				}
				Integer database;
				try {
					database = (Integer) p.get("database");
				} catch (ClassCastException x) {
					database = null;
				}
				store.pool = new JedisPool(poolConfig,
						p.getProperty("host", "localhost"),
						port == null ?  Protocol.DEFAULT_PORT : port,
						timeout == null ?  Protocol.DEFAULT_TIMEOUT : timeout,
						p.getProperty("password"),
						database == null ? Protocol.DEFAULT_DATABASE : database);
			}
		}
		return store;
	}

	/**
	 * The number of elements collected at once by an iterator.
	 * 
	 * @see #get(String, Constraint, int, Set)
	 */
	public int getScanCaching() {
		return scanCaching;
	}

	/**
	 * The number of elements collected at once by an iterator. Default value is
	 * 50.
	 * 
	 * @see #get(String, Constraint, int, Set)
	 */
	public void setScanCaching(int scanCaching) {
		this.scanCaching = scanCaching;
	}

	public Jedis getReadableRedis() {
		return null;
	}

	protected Jedis getWritableRedis() {
		return null;
	}

	@Override
	public void start() throws DatabaseNotReachedException {
		// Do nothing

	}

	@Override
	public boolean hasTable(String tableName)
			throws DatabaseNotReachedException {
		return this.getReadableRedis().exists(this.getKey(tableName));
	}

	/**
	 * Test if an id exists in the table
	 * [table]
	 */
	@Override
	public boolean exists(String table, String id)
			throws DatabaseNotReachedException {
		return (this.getReadableRedis().zscore(this.getKey(table), id) != null);
	}

	/**
	 * Test if a family exists for the element [id] in the table
	 * (test if [table]:[id]:[family]:keys exist
	 */
	@Override
	public boolean exists(String table, String id, String family)
			throws DatabaseNotReachedException {
		return (this.getReadableRedis().zcard(
				this.getKey(table, id, family, DataTypes.keys)) > 0);
	}

	/**
	 * Get an iterator on a list of Row specified with a Constraint, and for a
	 * list of families
	 */
	@Override
	public CloseableKeyIterator get(String table, Constraint c, int limit,
			Set<String> families) throws DatabaseNotReachedException {
		return new CloseableIterator(RedisStore.this, c == null ? null
				: c.getStartKey(), c == null ? null : c.getEndKey(), table,
				limit, families);
	}

	public List<Row> get(String table, String startKey, String stopKey,
			Set<String> families2, int maxBulk) {
		List<Row> result = new ArrayList<Row>(maxBulk);

		long firstRank = startKey == null ? 0 : this.idToRank(table, startKey,
				false);

		Set<String> redisKeys = this.getReadableRedis().zrange(
				this.getKey(table), firstRank, firstRank + maxBulk);

		for (String key : redisKeys) {
			if (stopKey == null || stopKey.compareTo(key) > 0)
				result.add(new RowWrapper(key, families2 == null ? null : this
						.get(table, key, families2)));
		}
		return result;
	}

	/**
	 * Get the value associated to the key, for the (table, id, family) n-uplet
	 * returns the value or null if the value do not exist
	 */
	@Override
	public byte[] get(String table, String id, String family, String key)
			throws DatabaseNotReachedException {

		// Getting value from normal vals
		String result = this.getReadableRedis().hget(
				this.getKey(table, id, family, DataTypes.vals), key);
		if (result != null)
			return this.decodeFromRedis(table, id, family, key, result);

		// Getting value from incrementing vals
		result = this.getReadableRedis().hget(
				this.getKey(table, id, family, DataTypes.increments), key);
		if (result != null)
			return decodeIncrementing(result);

		return null;
	}

	/**
	 * Get a Map of {key ={@literal >} value} for a specified id and a specified family
	 */
	@Override
	public Map<String, byte[]> get(String table, String id, String family)
			throws DatabaseNotReachedException {

		Map<String, byte[]> ret = new HashMap<String, byte[]>();
		Map<String, String> familyRedisResult;

		// Grabbing all values
		familyRedisResult = this.getReadableRedis().hgetAll(
				this.getKey(table, id, family, DataTypes.vals));
		for (Entry<String, String> result : familyRedisResult.entrySet()) {
			ret.put(result.getKey(), this.decodeFromRedis(table, id, family,
					result.getKey(), result.getValue()));
		}

		// Grabbing incrementing values
		familyRedisResult = this.getReadableRedis().hgetAll(
				this.getKey(table, id, family, DataTypes.increments));
		for (Entry<String, String> result : familyRedisResult.entrySet()) {
			ret.put(result.getKey(), this.decodeIncrementing(result.getValue()));
		}
		return ret;
	}

	/**
	 * Get a specified row from a specified table and family with rows specified
	 * with a Constraint
	 */
	@Override
	public Map<String, byte[]> get(String table, String id, String family,
			Constraint c) throws DatabaseNotReachedException {
		// la contrainte porte sur les clés dans la famille
		long rangeMin = (c != null && c.getStartKey() != null) ? this
				.columnToRank(table, id, family, c.getStartKey(), false)
				: DEFAULT_COLUMN_SCORE;
		long rangeMax = (c != null && c.getEndKey() != null) ? this
				.columnToRank(table, id, family, c.getEndKey(), true)
				: Long.MAX_VALUE;

		Set<String> keysS = this.getReadableRedis().zrange(
				this.getKey(table, id, family, DataTypes.keys), rangeMin,
				rangeMax);
		String[] keys = keysS.toArray(new String[keysS.size()]);

		Map<String, byte[]> familyResult = new HashMap<String, byte[]>();

		if (keys.length == 0)
			return familyResult;

		// Here, we assume that there are more values than increments
		// First getting normal values
		List<String> familyRedisResult = this.getReadableRedis().hmget(
				this.getKey(table, id, family, DataTypes.vals), keys);
		List<String> missingKeys = new ArrayList<String>(keys.length);
		// add and convert values from the family to a map
		for (int i = 0; i < keys.length; i++) {
			String val = familyRedisResult.get(i);
			if (val != null)
				familyResult.put(keys[i], this.decodeFromRedis(table, id,
						family, keys[i], familyRedisResult.get(i)));
			else
				missingKeys.add(keys[i]);
		}

		// grabbing missing values from increments
		if (!missingKeys.isEmpty()) {
			keys = missingKeys.toArray(new String[missingKeys.size()]);
			familyRedisResult = this.getReadableRedis().hmget(
					this.getKey(table, id, family, DataTypes.increments), keys);
			// add and convert values from the family to a map
			for (int i = 0; i < keys.length; i++) {
				String val = familyRedisResult.get(i);
				familyResult.put(keys[i], decodeIncrementing(val));
			}
		}

		return familyResult;
	}

	/**
	 * Returns all the values associated to the families for an specified id
	 */
	@Override
	public ColumnFamilyData get(String table, String id,
			Set<String> columnFamilies) throws DatabaseNotReachedException {

		ColumnFamilyData result = new DefaultColumnFamilyData();

		// Iteration on families
		Map<String, byte[]> keys;
		for (String family : columnFamilies) {
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
	 * @return Set of family (or empty set)
	 */
	protected Set<String> getFamilies(String table) {
		Set<String> families = this.getReadableRedis().smembers(
				this.getFamiliesKey(table));
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
	public void storeChanges(String table, String id, ColumnFamilyData changed,
			Map<String, Set<String>> removed,
			Map<String, Map<String, Number>> increments)

	throws DatabaseNotReachedException {
		Jedis r = this.pool.getResource();
		try {
			tryStoreChanges(table, id, changed, removed, increments, r);
			this.pool.returnResource(r);
		} catch (RuntimeException x) {
			this.pool.returnBrokenResource(r);
			r = this.pool.getResource();
			try {
				tryStoreChanges(table, id, changed, removed, increments, r);
				this.pool.returnResource(r);
			} catch (RuntimeException y) {
				this.pool.returnBrokenResource(r);
				throw x;
			}
		}
	}

	private void tryStoreChanges(String table, String id,
			ColumnFamilyData changed, Map<String, Set<String>> removed,
			Map<String, Map<String, Number>> increments, Jedis r)

	throws DatabaseNotReachedException {
		List<Object> res;

		do {
			String tableKey = this.getKey(table);
			String famKey = this.getFamiliesKey(table);
	
			Transaction t = r.multi();
	
			// Add the key
			t.zadd(tableKey, this.idToScore(id), id);
			// t.sadd(famKey, "");
	
			if (changed != null) {
	
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
					t.sadd(famKey, family.getKey());
	
					// add the set of keys
					for (String redisKey : family.getValue().keySet()) {
						t.zadd(this.getKey(table, id, family.getKey(),
								DataTypes.keys), this.columnToScore(redisKey),
								redisKey);
					}
	
					// add the { key => value } hashmap
					t.hmset(this.getKey(table, id, family.getKey(), DataTypes.vals),
							dataToBeInserted);
	
				}
			}
	
			if (removed != null) {
				// Remove the keys
				for (Entry<String, Set<String>> family : removed.entrySet()) {
					for (String redisKey : family.getValue()) {
						// remove from the list of keys...
						t.zrem(this.getKey(table, id, family.getKey(),
								DataTypes.keys), redisKey);
						// ... and from the hashmap
						t.hdel(this.getKey(table, id, family.getKey(),
								DataTypes.vals), redisKey);
	
						// Useless for an increment cannot be removed (i.e. get
						// null)
						// t.hdel(
						// this.getKey(table, id, family.getKey(),
						// DataTypes.increments), redisKey);
					}
				}
			}
	
			if (increments != null) {
				// Increment the values
				for (Map.Entry<String, Map<String, Number>> family : increments
						.entrySet()) {
	
					// if the family does not exist, create it
					t.sadd(famKey, family.getKey());
	
					// add the set of keys
					for (String redisKey : family.getValue().keySet()) {
						t.zadd(this.getKey(table, id, family.getKey(),
								DataTypes.keys), this.columnToScore(redisKey),
								redisKey);
					}
	
					for (Entry<String, Number> familyKey : family.getValue()
							.entrySet()) {
						// Increment directly in the "increments" database
						t.hincrBy(this.getKey(table, id, family.getKey(),
								DataTypes.increments), familyKey.getKey(),
								familyKey.getValue().longValue());
					}
				}
			}
	
			res = t.exec();
		} while(res == null);
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
		Jedis r = this.pool.getResource();
		try {
			tryDelete(table, id, r);
			this.pool.returnResource(r);
		} catch (RuntimeException x) {
			this.pool.returnBrokenResource(r);
			r = this.pool.getResource();
			try {
				tryDelete(table, id, r);
				this.pool.returnResource(r);
			} catch (RuntimeException y) {
				this.pool.returnBrokenResource(r);
				throw x;
			}
		}
	}

	private void tryDelete(String table, String id, Jedis r)
			throws DatabaseNotReachedException {
		// delete :
		// - [table]

		List<Object> res;

		do {
			List<String> keysToBeDeleted = new ArrayList<String>();

			String famKey = this.getFamiliesKey(table);

			r.watch(famKey);

			Set<String> families = r.smembers(famKey);
			for (String family : families) {
				// - [table]:[id]:[column family]:keys
				keysToBeDeleted.add(this.getKey(table, id, family,
						DataTypes.keys));
				// - [table]:[id]:[column family]:vals
				keysToBeDeleted.add(this.getKey(table, id, family,
						DataTypes.vals));
				// - [table]:[id]:[column family]:increments
				keysToBeDeleted.add(this.getKey(table, id, family,
						DataTypes.increments));
			}

			Transaction t = r.multi();

			t.zrem(this.getKey(table), id);
			if (!keysToBeDeleted.isEmpty())
				t.del(keysToBeDeleted.toArray(new String[keysToBeDeleted.size()]));

			res = t.exec();
		} while (res == null);
	}

	/**
	 * Return the number of the rows specified with a Constraint
	 */
	@Override
	public long count(String table, Constraint c)
			throws DatabaseNotReachedException {

		long rangeMin = (c != null && c.getStartKey() != null) ? this.idToRank(
				table, c.getStartKey(), false) : 0;
		long rangeMax = (c != null && c.getEndKey() != null) ? this.idToRank(
				table, c.getEndKey(), false) : this.getReadableRedis()
				.zcard(this.getKey(table)).intValue();

		return rangeMax - rangeMin;
	}

	public void flushAll() {
		this.getWritableRedis().flushAll();
	}

	/**
	 * Return the redis keys for the elements'keys for the table
	 * 
	 * @param table
	 * @return ordered list according to (weigth-{@literal >} id)
	 */
	protected String getKey(String table) {
		return table;
	}

	/**
	 * Return the redis key for the list of families for the table
	 * 
	 * @param table
	 * @return table:families -{@literal >} set of strings
	 */
	protected String getFamiliesKey(String table) {
		return table + SEPARATOR + FAMILIES;
	}

	/**
	 * @return [table]:[id]:[column family]:vals -{@literal >} hash of strings -{@literal >} string <br>
	 *         [table]:[id]:[column family]:keys -{@literal >} set of strings
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
	 */
	public long idToRank(String table, String id, Boolean endSearch) {
		return this.redisKeyToRank(this.getKey(table), id, endSearch);
	}

	/**
	 * Return the rank of a column synchro because of the add/remove dring the
	 * search
	 */
	public long columnToRank(String table, String id, String family, String key,
			Boolean endSearch) {
		return this.redisKeyToRank(
				this.getKey(table, id, family, DataTypes.keys), key, endSearch);
	}

	/**
	 * Return the rank of a potential key
	 * 
	 * @param hashKey
	 *            : the hashset where the keys are stored
	 * @param id
	 *            : the id of the searchable element
	 * @param endSearch
	 *            : is it a start or a stop search
	 */
	protected long redisKeyToRank(String hashKey, String id, Boolean endSearch) {

		Long rank = this.getReadableRedis().zrank(hashKey, id);
		// Remember if the key already exists
		if (rank != null) {
			// the key is existing, return the rank :
			return rank.longValue();
		} else {
			// the keys does not exist
			Response<Long> rankR;
			Jedis r = this.pool.getResource();
			try {
				rankR = tryKeyToRank(hashKey, id, r);
				this.pool.returnResource(r);
			} catch (RuntimeException x) {
				this.pool.returnBrokenResource(r);
				r = this.pool.getResource();
				try {
					rankR = tryKeyToRank(hashKey, id, r);
					this.pool.returnResource(r);
				} catch (RuntimeException y) {
					this.pool.returnBrokenResource(r);
					throw x;
				}
			}
			rank = rankR.get();

			// if the value do not already exists, remove 1 from the rank
			// if we want the previous value (endSearch)
			if (endSearch)
				rank--;

			return rank.intValue();
		}

	}

	private Response<Long> tryKeyToRank(String hashKey, String id, Jedis r) {
		List<Object> res;
		Response<Long> rankR;
		
		do {
			Transaction t = r.multi();
	
			// Add the key
			t.zadd(hashKey, 0, id);
	
			// get the rank of the freshly inserted id
			rankR = t.zrank(hashKey, id);
	
			// Remove the key
			t.zrem(hashKey, id);
	
			// Doing transaction
			res = t.exec();
		} while (res == null);

		return rankR;
	}

	/**
	 * Encode binary data to Base64 String
	 */
	public String encodeToRedis(byte[] data) {
		return Base64.encodeBase64String(data);
	}

	/**
	 * Decode Base64-encoded String to binary data
	 */
	public byte[] decodeFromRedis(String table, String id, String family,
			String row, String data) {
		return (data != null && data.length() != 0) ? Base64.decodeBase64(data)
				: new byte[0];
	}

	/**
	 * Decode encoded number to binary data
	 */
	public byte[] decodeIncrementing(String result) {
		return ConversionTools.convert(Long.parseLong(result), Long.class);
	}

}
