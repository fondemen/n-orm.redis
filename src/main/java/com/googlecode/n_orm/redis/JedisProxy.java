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
		try {
			String result;
			result = aJedis.ping();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String set(String key, String value) {
		Jedis aJedis = getAvailableJedis();

		try {
			String result = aJedis.set(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String get(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.get(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String set(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.set(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] get(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.get(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String quit() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.quit();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Boolean exists(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Boolean result = aJedis.exists(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long del(String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.del(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long del(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.del(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String flushDB() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.flushDB();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> keys(String pattern) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.keys(pattern);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> keys(byte[] pattern) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.keys(pattern);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String randomKey() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.randomKey();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String rename(String oldkey, String newkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.rename(oldkey, newkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] randomBinaryKey() {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.randomBinaryKey();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String rename(byte[] oldkey, byte[] newkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.rename(oldkey, newkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long renamenx(String oldkey, String newkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.renamenx(oldkey, newkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long renamenx(byte[] oldkey, byte[] newkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.renamenx(oldkey, newkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long dbSize() {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.dbSize();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long expire(String key, int seconds) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.expire(key, seconds);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long expire(byte[] key, int seconds) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.expire(key, seconds);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long expireAt(String key, long unixTime) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.expireAt(key, unixTime);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long expireAt(byte[] key, long unixTime) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.expireAt(key, unixTime);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String select(int index) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.select(index);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long move(String key, int dbIndex) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.move(key, dbIndex);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long move(byte[] key, int dbIndex) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.move(key, dbIndex);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String flushAll() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.flushAll();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String getSet(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.getSet(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] getSet(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.getSet(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> mget(String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.mget(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> mget(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.mget(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long setnx(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.setnx(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long setnx(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.setnx(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String setex(String key, int seconds, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.setex(key, seconds, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.setex(key, seconds, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String mset(String... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.mset(keysvalues);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String mset(byte[]... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.mset(keysvalues);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long msetnx(String... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.msetnx(keysvalues);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long msetnx(byte[]... keysvalues) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.msetnx(keysvalues);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long decrBy(String key, long integer) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.decrBy(key, integer);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long decrBy(byte[] key, long integer) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.decrBy(key, integer);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long decr(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.decr(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long decr(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.decr(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long incrBy(String key, long integer) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.incrBy(key, integer);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long incrBy(byte[] key, long integer) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.incrBy(key, integer);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long incr(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.incr(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long incr(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.incr(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long append(String key, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.append(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long append(byte[] key, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.append(key, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String substr(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.substr(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] substr(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.substr(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hset(String key, String field, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hset(key, field, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hset(key, field, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String hget(String key, String field) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.hget(key, field);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hsetnx(String key, String field, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hsetnx(key, field, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] hget(byte[] key, byte[] field) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.hget(key, field);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hsetnx(key, field, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String hmset(String key, Map<String, String> hash) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.hmset(key, hash);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.hmset(key, hash);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> hmget(String key, String... fields) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.hmget(key, fields);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.hmget(key, fields);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hincrBy(String key, String field, long value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hincrBy(key, field, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hincrBy(key, field, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Boolean hexists(String key, String field) {
		Jedis aJedis = getAvailableJedis();
		try {
			Boolean result = aJedis.hexists(key, field);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Boolean hexists(byte[] key, byte[] field) {
		Jedis aJedis = getAvailableJedis();
		try {
			Boolean result = aJedis.hexists(key, field);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hlen(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hlen(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> hkeys(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.hkeys(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hlen(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hlen(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> hvals(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.hvals(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> hkeys(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.hkeys(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Map<String, String> hgetAll(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Map<String, String> result = aJedis.hgetAll(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> hvals(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.hvals(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long rpush(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.rpush(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Map<byte[], byte[]> result = aJedis.hgetAll(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long lpush(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.lpush(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long rpush(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.rpush(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long llen(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.llen(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long lpush(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.lpush(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> lrange(String key, long start, long end) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.lrange(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long llen(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.llen(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.lrange(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String ltrim(String key, long start, long end) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.ltrim(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String ltrim(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.ltrim(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String lindex(String key, long index) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.lindex(key, index);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] lindex(byte[] key, int index) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.lindex(key, index);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String lset(String key, long index, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.lset(key, index, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String lset(byte[] key, int index, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.lset(key, index, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long lrem(String key, long count, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.lrem(key, count, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.lrem(key, count, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String lpop(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.lpop(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String rpop(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.rpop(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] lpop(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.lpop(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String rpoplpush(String srckey, String dstkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.rpoplpush(srckey, dstkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] rpop(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.rpop(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.rpoplpush(srckey, dstkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sadd(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sadd(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> smembers(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.smembers(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sadd(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sadd(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long srem(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.srem(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> smembers(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.smembers(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String spop(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.spop(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long srem(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.srem(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long smove(String srckey, String dstkey, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.smove(srckey, dstkey, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] spop(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.spop(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.smove(srckey, dstkey, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long scard(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.scard(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Boolean sismember(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Boolean result = aJedis.sismember(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long scard(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.scard(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> sinter(String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.sinter(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Boolean sismember(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Boolean result = aJedis.sismember(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> sinter(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.sinter(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sinterstore(String dstkey, String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sinterstore(dstkey, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> sunion(String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.sunion(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sinterstore(byte[] dstkey, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sinterstore(dstkey, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> sunion(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.sunion(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sunionstore(String dstkey, String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sunionstore(dstkey, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> sdiff(String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.sdiff(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sunionstore(byte[] dstkey, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sunionstore(dstkey, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> sdiff(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.sdiff(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sdiffstore(String dstkey, String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sdiffstore(dstkey, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String srandmember(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.srandmember(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sdiffstore(byte[] dstkey, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sdiffstore(dstkey, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] srandmember(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.srandmember(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> sort(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.sort(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Transaction multi() {
		Jedis aJedis = getAvailableJedis();
		try {
			Transaction result = aJedis.multi();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.sort(key, sortingParameters);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<Object> multi(TransactionBlock jedisTransaction) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<Object> result = aJedis.multi(jedisTransaction);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
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
		try {
			List<byte[]> result = aJedis.sort(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> blpop(int timeout, String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.blpop(timeout, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.sort(key, sortingParameters);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> blpop(int timeout, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.blpop(timeout, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sort(key, sortingParameters, dstkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sort(String key, String dstkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sort(key, dstkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> brpop(int timeout, String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.brpop(timeout, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sort(key, sortingParameters, dstkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long sort(byte[] key, byte[] dstkey) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.sort(key, dstkey);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<byte[]> brpop(int timeout, byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<byte[]> result = aJedis.brpop(timeout, keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String auth(String password) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.auth(password);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
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
		try {
			Long result = aJedis.publish(channel, message);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
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
		try {
			List<Object> result = aJedis.pipelined(jedisPipeline);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Pipeline pipelined() {
		Jedis aJedis = getAvailableJedis();
		try {
			Pipeline result = aJedis.pipelined();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long strlen(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.strlen(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long lpushx(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.lpushx(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long persist(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.persist(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long rpushx(String key, String string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.rpushx(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String echo(String string) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.echo(string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.linsert(key, where, pivot, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String brpoplpush(String source, String destination, int timeout) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.brpoplpush(source, destination, timeout);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public boolean setbit(String key, long offset, boolean value) {
		Jedis aJedis = getAvailableJedis();
		try {
			boolean result = aJedis.setbit(key, offset, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public boolean getbit(String key, long offset) {
		Jedis aJedis = getAvailableJedis();
		try {
			boolean result = aJedis.getbit(key, offset);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public long setrange(String key, long offset, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			long result = aJedis.setrange(key, offset, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String getrange(String key, long startOffset, long endOffset) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.getrange(key, startOffset, endOffset);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String save() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.save();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String bgsave() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.bgsave();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String bgrewriteaof() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.bgrewriteaof();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long lastsave() {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.lastsave();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String shutdown() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.shutdown();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String info() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.info();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
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
		try {
			String result = aJedis.slaveof(host, port);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String slaveofNoOne() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.slaveofNoOne();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public List<String> configGet(String pattern) {
		Jedis aJedis = getAvailableJedis();
		try {
			List<String> result = aJedis.configGet(pattern);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String configResetStat() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.configResetStat();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String configSet(String parameter, String value) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.configSet(parameter, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public boolean isConnected() {
		Jedis aJedis = getAvailableJedis();
		try {
			boolean result = aJedis.isConnected();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long strlen(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.strlen(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
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
		try {
			Long result = aJedis.lpushx(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long persist(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.persist(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long rpushx(byte[] key, byte[] string) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.rpushx(key, string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] echo(byte[] string) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.echo(string);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.linsert(key, where, pivot, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String debug(DebugParams params) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.debug(params);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Client getClient() {
		Jedis aJedis = getAvailableJedis();
		try {
			Client result = aJedis.getClient();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		Jedis aJedis = getAvailableJedis();
		try {
			byte[] result = aJedis.brpoplpush(source, destination, timeout);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Boolean exists(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Boolean result = aJedis.exists(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long setbit(byte[] key, long offset, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.setbit(key, offset, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long getbit(byte[] key, long offset) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.getbit(key, offset);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public long setrange(byte[] key, long offset, byte[] value) {
		Jedis aJedis = getAvailableJedis();
		try {
			long result = aJedis.setrange(key, offset, value);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String getrange(byte[] key, long startOffset, long endOffset) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.getrange(key, startOffset, endOffset);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long publish(byte[] channel, byte[] message) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.publish(channel, message);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
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
		try {
			Long result = aJedis.getDB();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public int hashCode() {
		Jedis aJedis = getAvailableJedis();
		try {
			int result = aJedis.hashCode();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hdel(String key, String field) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hdel(key, field);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long hdel(byte[] key, byte[] field) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.hdel(key, field);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String toString() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.toString();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String type(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.type(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String type(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.type(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long ttl(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.ttl(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long ttl(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.ttl(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zadd(String key, double score, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zadd(key, score, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zadd(key, score, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrange(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrange(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zrem(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zrem(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrange(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Double zincrby(String key, double score, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Double result = aJedis.zincrby(key, score, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zrem(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zrem(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Double result = aJedis.zincrby(key, score, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zrank(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zrank(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zrank(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zrank(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zrevrank(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zrevrank(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zrevrank(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zrevrank(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrevrange(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrevrange(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrangeWithScores(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrevrangeWithScores(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zcard(String key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zcard(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrevrange(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrangeWithScores(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Double zscore(String key, String member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Double result = aJedis.zscore(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrevrangeWithScores(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zcard(byte[] key) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zcard(key);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String watch(String... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.watch(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Double zscore(byte[] key, byte[] member) {
		Jedis aJedis = getAvailableJedis();
		try {
			Double result = aJedis.zscore(key, member);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String watch(byte[]... keys) {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.watch(keys);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public String unwatch() {
		Jedis aJedis = getAvailableJedis();
		try {
			String result = aJedis.unwatch();
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zcount(String key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zcount(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrangeByScore(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zcount(byte[] key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zcount(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrangeByScore(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrangeByScore(String key, String min, String max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrangeByScore(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrangeByScore(key, min, max, offset,
					count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrangeByScore(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrangeByScore(key, min, max, offset,
					count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min,
			double max, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max,
					offset, count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min,
			double max, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max,
					offset, count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrevrangeByScore(key, max, min);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrevrangeByScore(String key, String max, String min) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrevrangeByScore(key, max, min);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<String> zrevrangeByScore(String key, double max, double min,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<String> result = aJedis.zrevrangeByScore(key, max, min, offset,
					count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis
					.zrevrangeByScoreWithScores(key, max, min);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrevrangeByScoreWithScores(key, max,
					min, offset, count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zremrangeByRank(String key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zremrangeByRank(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zremrangeByScore(String key, double start, double end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zremrangeByScore(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrevrangeByScore(key, max, min);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrevrangeByScore(key, max, min);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zunionstore(String dstkey, String... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zunionstore(dstkey, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
			int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<byte[]> result = aJedis.zrevrangeByScore(key, max, min, offset,
					count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis
					.zrevrangeByScoreWithScores(key, max, min);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min, int offset, int count) {
		Jedis aJedis = getAvailableJedis();
		try {
			Set<Tuple> result = aJedis.zrevrangeByScoreWithScores(key, max,
					min, offset, count);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zremrangeByRank(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zremrangeByScore(key, start, end);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zunionstore(dstkey, params, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zunionstore(dstkey, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zinterstore(String dstkey, String... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zinterstore(dstkey, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zunionstore(dstkey, params, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zinterstore(dstkey, params, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zinterstore(dstkey, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		Jedis aJedis = getAvailableJedis();
		try {
			Long result = aJedis.zinterstore(dstkey, params, sets);
			return result;
		} finally {
			releaseInstance(aJedis);
		}
	}

}
