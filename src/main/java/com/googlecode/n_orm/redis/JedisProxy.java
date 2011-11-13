package com.googlecode.n_orm.redis;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Client;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
public class JedisProxy implements JedisCommands {

	private static JedisPool pool;
	public JedisProxy() {
		pool = new JedisPool(new JedisPoolConfig(), "localhost");
	}

	/**
	 * get an available jedis instance and put it in the busy pool
	 * 
	 * @return an available instance
	 */
	private Jedis getAvailableJedis() {
		return pool.getResource();
	}

	/**
	 * Put the instance into the available pool
	 * 
	 * @param jedisProxyInstance
	 */
	public void releaseInstance(Jedis jedisPoolInstance) {
		pool.returnResource(jedisPoolInstance);
	}

	/*
	 * Commands from Redis
	 */

	public String ping() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {

			result = aJedis.ping();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String set(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.set(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String get(String key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.get(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String set(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.set(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] get(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.get(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String quit() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.quit();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Boolean exists(String key) {
		Jedis aJedis = getAvailableJedis();
		Boolean result;
		try {
			result = aJedis.exists(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long del(String... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.del(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long del(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.del(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String flushDB() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.flushDB();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> keys(String pattern) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.keys(pattern);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> keys(byte[] pattern) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.keys(pattern);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String randomKey() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.randomKey();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String rename(String oldkey, String newkey) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.rename(oldkey, newkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] randomBinaryKey() {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.randomBinaryKey();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String rename(byte[] oldkey, byte[] newkey) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.rename(oldkey, newkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long renamenx(String oldkey, String newkey) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.renamenx(oldkey, newkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long renamenx(byte[] oldkey, byte[] newkey) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.renamenx(oldkey, newkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long dbSize() {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.dbSize();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long expire(String key, int seconds) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.expire(key, seconds);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long expire(byte[] key, int seconds) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.expire(key, seconds);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long expireAt(String key, long unixTime) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.expireAt(key, unixTime);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long expireAt(byte[] key, long unixTime) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.expireAt(key, unixTime);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String select(int index) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.select(index);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long move(String key, int dbIndex) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.move(key, dbIndex);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long move(byte[] key, int dbIndex) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.move(key, dbIndex);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String flushAll() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.flushAll();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String getSet(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.getSet(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] getSet(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.getSet(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> mget(String... keys) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.mget(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> mget(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.mget(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long setnx(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.setnx(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long setnx(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.setnx(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String setex(String key, int seconds, String value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.setex(key, seconds, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.setex(key, seconds, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String mset(String... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.mset(keysvalues);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String mset(byte[]... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.mset(keysvalues);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long msetnx(String... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.msetnx(keysvalues);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long msetnx(byte[]... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.msetnx(keysvalues);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long decrBy(String key, long integer) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.decrBy(key, integer);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long decrBy(byte[] key, long integer) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.decrBy(key, integer);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long decr(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.decr(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long decr(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.decr(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long incrBy(String key, long integer) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.incrBy(key, integer);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long incrBy(byte[] key, long integer) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.incrBy(key, integer);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long incr(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.incr(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long incr(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.incr(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long append(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.append(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long append(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.append(key, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String substr(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.substr(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] substr(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.substr(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hset(String key, String field, String value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hset(key, field, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hset(key, field, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String hget(String key, String field) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.hget(key, field);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hsetnx(String key, String field, String value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hsetnx(key, field, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] hget(byte[] key, byte[] field) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.hget(key, field);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hsetnx(key, field, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String hmset(String key, Map<String, String> hash) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.hmset(key, hash);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.hmset(key, hash);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> hmget(String key, String... fields) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.hmget(key, fields);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.hmget(key, fields);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hincrBy(String key, String field, long value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hincrBy(key, field, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hincrBy(key, field, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Boolean hexists(String key, String field) {
		Jedis aJedis = getAvailableJedis();
		Boolean result;
		try {
			result = aJedis.hexists(key, field);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Boolean hexists(byte[] key, byte[] field) {
		Jedis aJedis = getAvailableJedis();
		Boolean result;
		try {
			result = aJedis.hexists(key, field);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hlen(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hlen(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> hkeys(String key) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.hkeys(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hlen(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hlen(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> hvals(String key) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.hvals(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> hkeys(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.hkeys(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Map<String, String> hgetAll(String key) {
		Jedis aJedis = getAvailableJedis();
		Map<String, String> result;
		try {
			result = aJedis.hgetAll(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> hvals(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.hvals(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long rpush(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.rpush(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Map<byte[], byte[]> result;
		try {
			result = aJedis.hgetAll(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long lpush(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lpush(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long rpush(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.rpush(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long llen(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.llen(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long lpush(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lpush(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> lrange(String key, long start, long end) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.lrange(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long llen(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.llen(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.lrange(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String ltrim(String key, long start, long end) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.ltrim(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String ltrim(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.ltrim(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String lindex(String key, long index) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.lindex(key, index);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] lindex(byte[] key, int index) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.lindex(key, index);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String lset(String key, long index, String value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.lset(key, index, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String lset(byte[] key, int index, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.lset(key, index, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long lrem(String key, long count, String value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lrem(key, count, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lrem(key, count, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String lpop(String key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.lpop(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String rpop(String key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.rpop(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] lpop(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.lpop(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String rpoplpush(String srckey, String dstkey) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.rpoplpush(srckey, dstkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] rpop(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.rpop(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.rpoplpush(srckey, dstkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sadd(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sadd(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> smembers(String key) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.smembers(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sadd(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sadd(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long srem(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.srem(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> smembers(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.smembers(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String spop(String key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.spop(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long srem(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.srem(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long smove(String srckey, String dstkey, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.smove(srckey, dstkey, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] spop(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.spop(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.smove(srckey, dstkey, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long scard(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.scard(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Boolean sismember(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Boolean result;
		try {
			result = aJedis.sismember(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long scard(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.scard(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> sinter(String... keys) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.sinter(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Boolean sismember(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Boolean result;
		try {
			result = aJedis.sismember(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> sinter(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.sinter(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sinterstore(String dstkey, String... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sinterstore(dstkey, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> sunion(String... keys) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.sunion(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sinterstore(byte[] dstkey, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sinterstore(dstkey, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> sunion(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.sunion(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sunionstore(String dstkey, String... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sunionstore(dstkey, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> sdiff(String... keys) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.sdiff(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sunionstore(byte[] dstkey, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sunionstore(dstkey, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> sdiff(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.sdiff(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sdiffstore(String dstkey, String... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sdiffstore(dstkey, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String srandmember(String key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.srandmember(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sdiffstore(byte[] dstkey, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sdiffstore(dstkey, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] srandmember(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.srandmember(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> sort(String key) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.sort(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Transaction multi() {
		Jedis aJedis = getAvailableJedis();
		Transaction result;
		try {
			result = aJedis.multi();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.sort(key, sortingParameters);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<Object> multi(TransactionBlock jedisTransaction) {
		Jedis aJedis = getAvailableJedis();
		List<Object> result;
		try {
			result = aJedis.multi(jedisTransaction);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public void connect() {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.connect();
		} finally {
			releaseInstance(aJedis);
		}
	}

	public void disconnect() {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.disconnect();
		} finally {
			releaseInstance(aJedis);
		}

	}

	public List<byte[]> sort(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.sort(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> blpop(int timeout, String... keys) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.blpop(timeout, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.sort(key, sortingParameters);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> blpop(int timeout, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.blpop(timeout, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sort(key, sortingParameters, dstkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sort(String key, String dstkey) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sort(key, dstkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> brpop(int timeout, String... keys) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.brpop(timeout, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sort(key, sortingParameters, dstkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long sort(byte[] key, byte[] dstkey) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.sort(key, dstkey);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<byte[]> brpop(int timeout, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		List<byte[]> result;
		try {
			result = aJedis.brpop(timeout, keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String auth(String password) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.auth(password);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.subscribe(jedisPubSub, channels);
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long publish(String channel, String message) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.publish(channel, message);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.psubscribe(jedisPubSub, patterns);
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<Object> pipelined(PipelineBlock jedisPipeline) {
		Jedis aJedis = getAvailableJedis();
		List<Object> result;
		try {
			result = aJedis.pipelined(jedisPipeline);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Pipeline pipelined() {
		Jedis aJedis = getAvailableJedis();
		Pipeline result;
		try {
			result = aJedis.pipelined();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long strlen(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.strlen(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long lpushx(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lpushx(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long persist(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.persist(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long rpushx(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.rpushx(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String echo(String string) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.echo(string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.linsert(key, where, pivot, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String brpoplpush(String source, String destination, int timeout) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.brpoplpush(source, destination, timeout);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public boolean setbit(String key, long offset, boolean value) {
		Jedis aJedis = getAvailableJedis();
		boolean result;
		try {
			result = aJedis.setbit(key, offset, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public boolean getbit(String key, long offset) {
		Jedis aJedis = getAvailableJedis();
		boolean result;
		try {
			result = aJedis.getbit(key, offset);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public long setrange(String key, long offset, String value) {
		Jedis aJedis = getAvailableJedis();
		long result;
		try {
			result = aJedis.setrange(key, offset, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String getrange(String key, long startOffset, long endOffset) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.getrange(key, startOffset, endOffset);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String save() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.save();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String bgsave() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.bgsave();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String bgrewriteaof() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.bgrewriteaof();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long lastsave() {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lastsave();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String shutdown() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.shutdown();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String info() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.info();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public void monitor(JedisMonitor jedisMonitor) {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.monitor(jedisMonitor);
		} finally {
			releaseInstance(aJedis);
		}

	}

	public String slaveof(String host, int port) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.slaveof(host, port);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String slaveofNoOne() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.slaveofNoOne();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public List<String> configGet(String pattern) {
		Jedis aJedis = getAvailableJedis();
		List<String> result;
		try {
			result = aJedis.configGet(pattern);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String configResetStat() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.configResetStat();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String configSet(String parameter, String value) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.configSet(parameter, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public boolean isConnected() {
		Jedis aJedis = getAvailableJedis();
		boolean result;
		try {
			result = aJedis.isConnected();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long strlen(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.strlen(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public void sync() {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.sync();
		} finally {
			releaseInstance(aJedis);
		}

	}

	public Long lpushx(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.lpushx(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long persist(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.persist(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long rpushx(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.rpushx(key, string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] echo(byte[] string) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.echo(string);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.linsert(key, where, pivot, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String debug(DebugParams params) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.debug(params);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Client getClient() {
		Jedis aJedis = getAvailableJedis();
		Client result;
		try {
			result = aJedis.getClient();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		Jedis aJedis = getAvailableJedis();
		byte[] result;
		try {
			result = aJedis.brpoplpush(source, destination, timeout);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Boolean exists(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Boolean result;
		try {
			result = aJedis.exists(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long setbit(byte[] key, long offset, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.setbit(key, offset, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long getbit(byte[] key, long offset) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.getbit(key, offset);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public long setrange(byte[] key, long offset, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		long result;
		try {
			result = aJedis.setrange(key, offset, value);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String getrange(byte[] key, long startOffset, long endOffset) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.getrange(key, startOffset, endOffset);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long publish(byte[] channel, byte[] message) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.publish(channel, message);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.subscribe(jedisPubSub, channels);
		} finally {
			releaseInstance(aJedis);
		}

	}

	public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		Jedis aJedis = getAvailableJedis();
		try {
			aJedis.psubscribe(jedisPubSub, patterns);
		} finally {
			releaseInstance(aJedis);
		}

	}

	public Long getDB() {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.getDB();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public int hashCode() {
		Jedis aJedis = getAvailableJedis();
		int result;
		try {
			result = aJedis.hashCode();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hdel(String key, String field) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hdel(key, field);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long hdel(byte[] key, byte[] field) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.hdel(key, field);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String toString() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.toString();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String type(String key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.type(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String type(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.type(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long ttl(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.ttl(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long ttl(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.ttl(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zadd(String key, double score, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zadd(key, score, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zadd(key, score, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrange(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrange(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zrem(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zrem(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrange(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Double zincrby(String key, double score, String member) {
		Jedis aJedis = getAvailableJedis();
		Double result;
		try {
			result = aJedis.zincrby(key, score, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zrem(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zrem(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Double result;
		try {
			result = aJedis.zincrby(key, score, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zrank(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zrank(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zrank(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zrank(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zrevrank(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zrevrank(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zrevrank(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zrevrank(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrevrange(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrevrange(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrangeWithScores(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrevrangeWithScores(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zcard(String key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zcard(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrevrange(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrangeWithScores(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Double zscore(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		Double result;
		try {
			result = aJedis.zscore(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrevrangeWithScores(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zcard(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zcard(key);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String watch(String... keys) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.watch(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Double zscore(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		Double result;
		try {
			result = aJedis.zscore(key, member);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String watch(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.watch(keys);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public String unwatch() {
		Jedis aJedis = getAvailableJedis();
		String result;
		try {
			result = aJedis.unwatch();
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zcount(String key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zcount(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrangeByScore(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zcount(byte[] key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zcount(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrangeByScore(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, String min, String max) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrangeByScore(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrangeByScore(key, min, max, offset, count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrangeByScore(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrangeByScore(key, min, max, offset, count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrangeByScoreWithScores(key, min, max);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min,
			double max, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrangeByScoreWithScores(key, min, max, offset,
					count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min,
			double max, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrangeByScoreWithScores(key, min, max, offset,
					count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrevrangeByScore(key, max, min);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, String max, String min) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrevrangeByScore(key, max, min);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<String> result;
		try {
			result = aJedis.zrevrangeByScore(key, max, min, offset, count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrevrangeByScoreWithScores(key, max, min);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrevrangeByScoreWithScores(key, max, min, offset,
					count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zremrangeByRank(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zremrangeByRank(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zremrangeByScore(String key, double start, double end) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zremrangeByScore(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrevrangeByScore(key, max, min);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrevrangeByScore(key, max, min);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zunionstore(String dstkey, String... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zunionstore(dstkey, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<byte[]> result;
		try {
			result = aJedis.zrevrangeByScore(key, max, min, offset, count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrevrangeByScoreWithScores(key, max, min);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		Set<Tuple> result;
		try {
			result = aJedis.zrevrangeByScoreWithScores(key, max, min, offset,
					count);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zremrangeByRank(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zremrangeByScore(key, start, end);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zunionstore(dstkey, params, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zunionstore(dstkey, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zinterstore(String dstkey, String... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zinterstore(dstkey, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zunionstore(dstkey, params, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zinterstore(dstkey, params, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zinterstore(dstkey, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		Long result;
		try {
			result = aJedis.zinterstore(dstkey, params, sets);
		} finally {
			releaseInstance(aJedis);
		}
		return result;
	}

}
