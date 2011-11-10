package com.googlecode.n_orm.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Client;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class JedisProxy implements JedisCommands {

	private List<JedisProxyInstance> jedisAvailablePool;
	private List<JedisProxyInstance> jedisBusyPool;
	
	public JedisProxy() {
		jedisAvailablePool = new ArrayList<JedisProxyInstance>();
		jedisBusyPool = new ArrayList<JedisProxyInstance>();
	}
	
	/**
	 * get an available jedis instance and put it in the busy pool
	 * @return an available instance
	 */
	private JedisProxyInstance getAvailableJedis() {
		
		synchronized (jedisAvailablePool) {
			if(jedisAvailablePool.size() != 0) {
				// It still exists a avaiable instance, use it
				JedisProxyInstance jedisInstance = jedisAvailablePool.remove(0);
				jedisBusyPool.add(jedisInstance);
				return jedisInstance;
			} else {
				// No more avaliable jedis instance, we create one
				JedisProxyInstance jedisInstance = new JedisProxyInstance();
				jedisBusyPool.add(jedisInstance);
				System.out.println("Création d'une nouvelle instance : "+jedisBusyPool.size()+" busy/"+jedisAvailablePool.size()+" avlb");
				return jedisInstance;
			}
		}
	}
	
	/**
	 * Remove the instance from the busy pool and put it into the available pool
	 * @param jedisProxyInstance
	 */
	public void releaseInstance(JedisProxyInstance jedisProxyInstance) {
		synchronized (jedisAvailablePool) {

			int pos = jedisBusyPool.indexOf(jedisProxyInstance);
			assert (pos != -1);
			
			jedisAvailablePool.add(jedisBusyPool.remove(pos));
		}
	}

	
	/* 
	 * Commands from Redis
	 */
	
	public String ping() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.ping();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String set(String key, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.set(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String get(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.get(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String set(byte[] key, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.set(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] get(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.get(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String quit() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.quit();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Boolean exists(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Boolean result = aJedis.exists(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long del(String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.del(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long del(byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.del(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String flushDB() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.flushDB();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> keys(String pattern) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.keys(pattern);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> keys(byte[] pattern) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.keys(pattern);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String randomKey() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.randomKey();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String rename(String oldkey, String newkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.rename(oldkey, newkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] randomBinaryKey() {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.randomBinaryKey();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String rename(byte[] oldkey, byte[] newkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.rename(oldkey, newkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long renamenx(String oldkey, String newkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.renamenx(oldkey, newkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long renamenx(byte[] oldkey, byte[] newkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.renamenx(oldkey, newkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long dbSize() {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.dbSize();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long expire(String key, int seconds) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.expire(key, seconds);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long expire(byte[] key, int seconds) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.expire(key, seconds);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long expireAt(String key, long unixTime) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.expireAt(key, unixTime);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long expireAt(byte[] key, long unixTime) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.expireAt(key, unixTime);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String select(int index) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.select(index);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long move(String key, int dbIndex) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.move(key, dbIndex);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long move(byte[] key, int dbIndex) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.move(key, dbIndex);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String flushAll() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.flushAll();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String getSet(String key, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.getSet(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] getSet(byte[] key, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.getSet(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> mget(String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.mget(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> mget(byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.mget(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long setnx(String key, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.setnx(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long setnx(byte[] key, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.setnx(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String setex(String key, int seconds, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.setex(key, seconds, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.setex(key, seconds, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String mset(String... keysvalues) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.mset(keysvalues);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String mset(byte[]... keysvalues) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.mset(keysvalues);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long msetnx(String... keysvalues) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.msetnx(keysvalues);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long msetnx(byte[]... keysvalues) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.msetnx(keysvalues);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long decrBy(String key, long integer) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.decrBy(key, integer);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long decrBy(byte[] key, long integer) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.decrBy(key, integer);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long decr(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.decr(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long decr(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.decr(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long incrBy(String key, long integer) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.incrBy(key, integer);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long incrBy(byte[] key, long integer) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.incrBy(key, integer);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long incr(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.incr(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long incr(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.incr(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long append(String key, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.append(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long append(byte[] key, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.append(key, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String substr(String key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.substr(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] substr(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.substr(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hset(String key, String field, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hset(key, field, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hset(key, field, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String hget(String key, String field) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.hget(key, field);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hsetnx(String key, String field, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hsetnx(key, field, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] hget(byte[] key, byte[] field) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.hget(key, field);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hsetnx(key, field, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String hmset(String key, Map<String, String> hash) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.hmset(key, hash);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.hmset(key, hash);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> hmget(String key, String... fields) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.hmget(key, fields);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.hmget(key, fields);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hincrBy(String key, String field, long value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hincrBy(key, field, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hincrBy(key, field, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Boolean hexists(String key, String field) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Boolean result = aJedis.hexists(key, field);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Boolean hexists(byte[] key, byte[] field) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Boolean result = aJedis.hexists(key, field);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hlen(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hlen(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> hkeys(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.hkeys(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hlen(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hlen(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> hvals(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.hvals(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> hkeys(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.hkeys(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Map<String, String> hgetAll(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Map<String, String> result = aJedis.hgetAll(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> hvals(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.hvals(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long rpush(String key, String string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.rpush(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Map<byte[], byte[]> result = aJedis.hgetAll(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long lpush(String key, String string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lpush(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long rpush(byte[] key, byte[] string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.rpush(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long llen(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.llen(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long lpush(byte[] key, byte[] string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lpush(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> lrange(String key, long start, long end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.lrange(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long llen(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.llen(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.lrange(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String ltrim(String key, long start, long end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.ltrim(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String ltrim(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.ltrim(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String lindex(String key, long index) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.lindex(key, index);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] lindex(byte[] key, int index) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.lindex(key, index);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String lset(String key, long index, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.lset(key, index, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String lset(byte[] key, int index, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.lset(key, index, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long lrem(String key, long count, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lrem(key, count, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lrem(key, count, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String lpop(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.lpop(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String rpop(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.rpop(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] lpop(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.lpop(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String rpoplpush(String srckey, String dstkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.rpoplpush(srckey, dstkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] rpop(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.rpop(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.rpoplpush(srckey, dstkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sadd(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sadd(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> smembers(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.smembers(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sadd(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sadd(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long srem(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.srem(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> smembers(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.smembers(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String spop(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.spop(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long srem(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.srem(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long smove(String srckey, String dstkey, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.smove(srckey, dstkey, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] spop(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.spop(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.smove(srckey, dstkey, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long scard(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.scard(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Boolean sismember(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Boolean result = aJedis.sismember(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long scard(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.scard(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> sinter(String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.sinter(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Boolean sismember(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Boolean result = aJedis.sismember(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> sinter(byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.sinter(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sinterstore(String dstkey, String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sinterstore(dstkey, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> sunion(String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.sunion(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sinterstore(byte[] dstkey, byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sinterstore(dstkey, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> sunion(byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.sunion(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sunionstore(String dstkey, String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sunionstore(dstkey, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> sdiff(String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.sdiff(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sunionstore(byte[] dstkey, byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sunionstore(dstkey, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> sdiff(byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.sdiff(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sdiffstore(String dstkey, String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sdiffstore(dstkey, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String srandmember(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.srandmember(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sdiffstore(byte[] dstkey, byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sdiffstore(dstkey, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] srandmember(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.srandmember(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> sort(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.sort(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Transaction multi() {
		JedisProxyInstance aJedis = getAvailableJedis();
		Transaction result = aJedis.multi();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.sort(key, sortingParameters);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<Object> multi(TransactionBlock jedisTransaction) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<Object> result = aJedis.multi(jedisTransaction);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public void connect() {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.connect();
		aJedis.release(JedisProxy.this);
	}

	public void disconnect() {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.disconnect();
		aJedis.release(JedisProxy.this);
	}

	public List<byte[]> sort(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.sort(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> blpop(int timeout, String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.blpop(timeout, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.sort(key, sortingParameters);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> blpop(int timeout, byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.blpop(timeout, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sort(key, sortingParameters, dstkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sort(String key, String dstkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sort(key, dstkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> brpop(int timeout, String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.brpop(timeout, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sort(key, sortingParameters, dstkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long sort(byte[] key, byte[] dstkey) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.sort(key, dstkey);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<byte[]> brpop(int timeout, byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<byte[]> result = aJedis.brpop(timeout, keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String auth(String password) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.auth(password);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.subscribe(jedisPubSub, channels);
	}

	public Long publish(String channel, String message) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.publish(channel, message);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.psubscribe(jedisPubSub, patterns);
	}

	public List<Object> pipelined(PipelineBlock jedisPipeline) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<Object> result = aJedis.pipelined(jedisPipeline);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Pipeline pipelined() {
		JedisProxyInstance aJedis = getAvailableJedis();
		Pipeline result = aJedis.pipelined();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long strlen(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.strlen(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long lpushx(String key, String string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lpushx(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long persist(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.persist(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long rpushx(String key, String string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.rpushx(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String echo(String string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.echo(string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.linsert(key, where, pivot, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String brpoplpush(String source, String destination, int timeout) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.brpoplpush(source, destination, timeout);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public boolean setbit(String key, long offset, boolean value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		boolean result = aJedis.setbit(key, offset, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public boolean getbit(String key, long offset) {
		JedisProxyInstance aJedis = getAvailableJedis();
		boolean result = aJedis.getbit(key, offset);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public long setrange(String key, long offset, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		long result = aJedis.setrange(key, offset, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String getrange(String key, long startOffset, long endOffset) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.getrange(key, startOffset, endOffset);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String save() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.save();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String bgsave() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.bgsave();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String bgrewriteaof() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.bgrewriteaof();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long lastsave() {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lastsave();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String shutdown() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.shutdown();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String info() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.info();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public void monitor(JedisMonitor jedisMonitor) {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.monitor(jedisMonitor);
		aJedis.release(JedisProxy.this);
	}

	public String slaveof(String host, int port) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.slaveof(host, port);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String slaveofNoOne() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.slaveofNoOne();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public List<String> configGet(String pattern) {
		JedisProxyInstance aJedis = getAvailableJedis();
		List<String> result = aJedis.configGet(pattern);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String configResetStat() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.configResetStat();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String configSet(String parameter, String value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.configSet(parameter, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public boolean isConnected() {
		JedisProxyInstance aJedis = getAvailableJedis();
		boolean result = aJedis.isConnected();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long strlen(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.strlen(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public void sync() {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.sync();
		aJedis.release(JedisProxy.this);
	}

	public Long lpushx(byte[] key, byte[] string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.lpushx(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long persist(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.persist(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long rpushx(byte[] key, byte[] string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.rpushx(key, string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] echo(byte[] string) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.echo(string);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.linsert(key, where, pivot, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String debug(DebugParams params) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.debug(params);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Client getClient() {
		JedisProxyInstance aJedis = getAvailableJedis();
		Client result = aJedis.getClient();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		JedisProxyInstance aJedis = getAvailableJedis();
		byte[] result = aJedis.brpoplpush(source, destination, timeout);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Boolean exists(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Boolean result = aJedis.exists(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long setbit(byte[] key, long offset, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.setbit(key, offset, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long getbit(byte[] key, long offset) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.getbit(key, offset);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public long setrange(byte[] key, long offset, byte[] value) {
		JedisProxyInstance aJedis = getAvailableJedis();
		long result = aJedis.setrange(key, offset, value);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String getrange(byte[] key, long startOffset, long endOffset) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.getrange(key, startOffset, endOffset);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long publish(byte[] channel, byte[] message) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.publish(channel, message);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.subscribe(jedisPubSub, channels);
		aJedis.release(JedisProxy.this);
	}

	public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		JedisProxyInstance aJedis = getAvailableJedis();
		aJedis.psubscribe(jedisPubSub, patterns);
		aJedis.release(JedisProxy.this);
	}

	public Long getDB() {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.getDB();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public int hashCode() {
		JedisProxyInstance aJedis = getAvailableJedis();
		int result = aJedis.hashCode();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hdel(String key, String field) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hdel(key, field);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long hdel(byte[] key, byte[] field) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.hdel(key, field);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String toString() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.toString();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String type(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.type(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String type(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.type(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long ttl(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.ttl(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long ttl(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.ttl(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zadd(String key, double score, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zadd(key, score, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zadd(key, score, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrange(String key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrange(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zrem(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zrem(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrange(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Double zincrby(String key, double score, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Double result = aJedis.zincrby(key, score, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zrem(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zrem(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Double result = aJedis.zincrby(key, score, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zrank(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zrank(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zrank(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zrank(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zrevrank(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zrevrank(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zrevrank(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zrevrank(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrevrange(String key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrevrange(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrangeWithScores(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrevrangeWithScores(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zcard(String key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zcard(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrevrange(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrangeWithScores(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Double zscore(String key, String member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Double result = aJedis.zscore(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrevrangeWithScores(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zcard(byte[] key) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zcard(key);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String watch(String... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.watch(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Double zscore(byte[] key, byte[] member) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Double result = aJedis.zscore(key, member);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String watch(byte[]... keys) {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.watch(keys);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public String unwatch() {
		JedisProxyInstance aJedis = getAvailableJedis();
		String result = aJedis.unwatch();
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zcount(String key, double min, double max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zcount(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrangeByScore(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zcount(byte[] key, double min, double max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zcount(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrangeByScore(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrangeByScore(String key, String min, String max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrangeByScore(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrangeByScore(key, min, max, offset, count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrangeByScore(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max,
			int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrangeByScore(key, min, max, offset, count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min,
			double max, int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max,
				offset, count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min,
			double max, int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrangeByScoreWithScores(key, min, max,
				offset, count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrevrangeByScore(key, max, min);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrevrangeByScore(String key, String max, String min) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrevrangeByScore(key, max, min);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<String> zrevrangeByScore(String key, double max, double min,
			int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<String> result = aJedis.zrevrangeByScore(key, max, min, offset,
				count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrevrangeByScoreWithScores(key, max, min);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min, int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrevrangeByScoreWithScores(key, max, min,
				offset, count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zremrangeByRank(String key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zremrangeByRank(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zremrangeByScore(String key, double start, double end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zremrangeByScore(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrevrangeByScore(key, max, min);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrevrangeByScore(key, max, min);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zunionstore(String dstkey, String... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zunionstore(dstkey, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
			int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<byte[]> result = aJedis.zrevrangeByScore(key, max, min, offset,
				count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrevrangeByScoreWithScores(key, max, min);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min, int offset, int count) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Set<Tuple> result = aJedis.zrevrangeByScoreWithScores(key, max, min,
				offset, count);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zremrangeByRank(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zremrangeByScore(key, start, end);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zunionstore(dstkey, params, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zunionstore(dstkey, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zinterstore(String dstkey, String... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zinterstore(dstkey, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zunionstore(dstkey, params, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zinterstore(dstkey, params, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zinterstore(dstkey, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}

	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		JedisProxyInstance aJedis = getAvailableJedis();
		Long result = aJedis.zinterstore(dstkey, params, sets);
		aJedis.release(JedisProxy.this);
		return result;
	}



}
