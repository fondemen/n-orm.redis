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
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class JedisProxyInstance implements JedisCommands {
	private Jedis jedis;
	
	public JedisProxyInstance() {
		this.JPIstart();
	}
	
	protected void JPIstart() {
		jedis = new Jedis("localhost");
	}

	public void release(JedisProxy jedisProxy) {
		
	}
	
	public void recycle() {
		this.JPIstart();
		
	}
/*	
	@Override
	public boolean equals(Object eq) {
		return true;
	}
*/	
	/*
	 * Below, the Jedis commands
	 */
	
	public String ping() {
		return jedis.ping();
	}

	public String set(String key, String value) {
		return jedis.set(key, value);
	}

	public String get(String key) {
		return jedis.get(key);
	}

	public String set(byte[] key, byte[] value) {
		return jedis.set(key, value);
	}

	public byte[] get(byte[] key) {
		return jedis.get(key);
	}

	public String quit() {
		return jedis.quit();
	}

	public Boolean exists(String key) {
		return jedis.exists(key);
	}

	public Long del(String... keys) {
		return jedis.del(keys);
	}

	public Long del(byte[]... keys) {
		return jedis.del(keys);
	}

	public String flushDB() {
		return jedis.flushDB();
	}

	public Set<String> keys(String pattern) {
		return jedis.keys(pattern);
	}

	public Set<byte[]> keys(byte[] pattern) {
		return jedis.keys(pattern);
	}

	public String randomKey() {
		return jedis.randomKey();
	}

	public String rename(String oldkey, String newkey) {
		return jedis.rename(oldkey, newkey);
	}

	public byte[] randomBinaryKey() {
		return jedis.randomBinaryKey();
	}

	public String rename(byte[] oldkey, byte[] newkey) {
		return jedis.rename(oldkey, newkey);
	}

	public Long renamenx(String oldkey, String newkey) {
		return jedis.renamenx(oldkey, newkey);
	}

	public Long renamenx(byte[] oldkey, byte[] newkey) {
		return jedis.renamenx(oldkey, newkey);
	}

	public Long dbSize() {
		return jedis.dbSize();
	}

	public Long expire(String key, int seconds) {
		return jedis.expire(key, seconds);
	}

	public Long expire(byte[] key, int seconds) {
		return jedis.expire(key, seconds);
	}

	public Long expireAt(String key, long unixTime) {
		return jedis.expireAt(key, unixTime);
	}

	public Long expireAt(byte[] key, long unixTime) {
		return jedis.expireAt(key, unixTime);
	}

	public String select(int index) {
		return jedis.select(index);
	}

	public Long move(String key, int dbIndex) {
		return jedis.move(key, dbIndex);
	}

	public Long move(byte[] key, int dbIndex) {
		return jedis.move(key, dbIndex);
	}

	public String flushAll() {
		return jedis.flushAll();
	}

	public String getSet(String key, String value) {
		return jedis.getSet(key, value);
	}

	public byte[] getSet(byte[] key, byte[] value) {
		return jedis.getSet(key, value);
	}

	public List<String> mget(String... keys) {
		return jedis.mget(keys);
	}

	public List<byte[]> mget(byte[]... keys) {
		return jedis.mget(keys);
	}

	public Long setnx(String key, String value) {
		return jedis.setnx(key, value);
	}

	public Long setnx(byte[] key, byte[] value) {
		return jedis.setnx(key, value);
	}

	public String setex(String key, int seconds, String value) {
		return jedis.setex(key, seconds, value);
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		return jedis.setex(key, seconds, value);
	}

	public String mset(String... keysvalues) {
		return jedis.mset(keysvalues);
	}

	public String mset(byte[]... keysvalues) {
		return jedis.mset(keysvalues);
	}

	public Long msetnx(String... keysvalues) {
		return jedis.msetnx(keysvalues);
	}

	public Long msetnx(byte[]... keysvalues) {
		return jedis.msetnx(keysvalues);
	}

	public Long decrBy(String key, long integer) {
		return jedis.decrBy(key, integer);
	}

	public Long decrBy(byte[] key, long integer) {
		return jedis.decrBy(key, integer);
	}

	public Long decr(String key) {
		return jedis.decr(key);
	}

	public Long decr(byte[] key) {
		return jedis.decr(key);
	}

	public Long incrBy(String key, long integer) {
		return jedis.incrBy(key, integer);
	}

	public Long incrBy(byte[] key, long integer) {
		return jedis.incrBy(key, integer);
	}

	public Long incr(String key) {
		return jedis.incr(key);
	}

	public Long incr(byte[] key) {
		return jedis.incr(key);
	}

	public Long append(String key, String value) {
		return jedis.append(key, value);
	}

	public Long append(byte[] key, byte[] value) {
		return jedis.append(key, value);
	}

	public String substr(String key, int start, int end) {
		return jedis.substr(key, start, end);
	}

	public byte[] substr(byte[] key, int start, int end) {
		return jedis.substr(key, start, end);
	}

	public Long hset(String key, String field, String value) {
		return jedis.hset(key, field, value);
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		return jedis.hset(key, field, value);
	}

	public String hget(String key, String field) {
		return jedis.hget(key, field);
	}

	public Long hsetnx(String key, String field, String value) {
		return jedis.hsetnx(key, field, value);
	}

	public byte[] hget(byte[] key, byte[] field) {
		return jedis.hget(key, field);
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return jedis.hsetnx(key, field, value);
	}

	public String hmset(String key, Map<String, String> hash) {
		return jedis.hmset(key, hash);
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		return jedis.hmset(key, hash);
	}

	public List<String> hmget(String key, String... fields) {
		return jedis.hmget(key, fields);
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return jedis.hmget(key, fields);
	}

	public Long hincrBy(String key, String field, long value) {
		return jedis.hincrBy(key, field, value);
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		return jedis.hincrBy(key, field, value);
	}

	public Boolean hexists(String key, String field) {
		return jedis.hexists(key, field);
	}

	public Boolean hexists(byte[] key, byte[] field) {
		return jedis.hexists(key, field);
	}

	public Long hlen(String key) {
		return jedis.hlen(key);
	}

	public Set<String> hkeys(String key) {
		return jedis.hkeys(key);
	}

	public Long hlen(byte[] key) {
		return jedis.hlen(key);
	}

	public List<String> hvals(String key) {
		return jedis.hvals(key);
	}

	public Set<byte[]> hkeys(byte[] key) {
		return jedis.hkeys(key);
	}

	public Map<String, String> hgetAll(String key) {
		return jedis.hgetAll(key);
	}

	public List<byte[]> hvals(byte[] key) {
		return jedis.hvals(key);
	}

	public Long rpush(String key, String string) {
		return jedis.rpush(key, string);
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return jedis.hgetAll(key);
	}

	public Long lpush(String key, String string) {
		return jedis.lpush(key, string);
	}

	public Long rpush(byte[] key, byte[] string) {
		return jedis.rpush(key, string);
	}

	public Long llen(String key) {
		return jedis.llen(key);
	}

	public Long lpush(byte[] key, byte[] string) {
		return jedis.lpush(key, string);
	}

	public List<String> lrange(String key, long start, long end) {
		return jedis.lrange(key, start, end);
	}

	public Long llen(byte[] key) {
		return jedis.llen(key);
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		return jedis.lrange(key, start, end);
	}

	public String ltrim(String key, long start, long end) {
		return jedis.ltrim(key, start, end);
	}

	public String ltrim(byte[] key, int start, int end) {
		return jedis.ltrim(key, start, end);
	}

	public String lindex(String key, long index) {
		return jedis.lindex(key, index);
	}

	public byte[] lindex(byte[] key, int index) {
		return jedis.lindex(key, index);
	}

	public String lset(String key, long index, String value) {
		return jedis.lset(key, index, value);
	}

	public String lset(byte[] key, int index, byte[] value) {
		return jedis.lset(key, index, value);
	}

	public Long lrem(String key, long count, String value) {
		return jedis.lrem(key, count, value);
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		return jedis.lrem(key, count, value);
	}

	public String lpop(String key) {
		return jedis.lpop(key);
	}

	public String rpop(String key) {
		return jedis.rpop(key);
	}

	public byte[] lpop(byte[] key) {
		return jedis.lpop(key);
	}

	public String rpoplpush(String srckey, String dstkey) {
		return jedis.rpoplpush(srckey, dstkey);
	}

	public byte[] rpop(byte[] key) {
		return jedis.rpop(key);
	}

	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		return jedis.rpoplpush(srckey, dstkey);
	}

	public Long sadd(String key, String member) {
		return jedis.sadd(key, member);
	}

	public Set<String> smembers(String key) {
		return jedis.smembers(key);
	}

	public Long sadd(byte[] key, byte[] member) {
		return jedis.sadd(key, member);
	}

	public Long srem(String key, String member) {
		return jedis.srem(key, member);
	}

	public Set<byte[]> smembers(byte[] key) {
		return jedis.smembers(key);
	}

	public String spop(String key) {
		return jedis.spop(key);
	}

	public Long srem(byte[] key, byte[] member) {
		return jedis.srem(key, member);
	}

	public Long smove(String srckey, String dstkey, String member) {
		return jedis.smove(srckey, dstkey, member);
	}

	public byte[] spop(byte[] key) {
		return jedis.spop(key);
	}

	public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		return jedis.smove(srckey, dstkey, member);
	}

	public Long scard(String key) {
		return jedis.scard(key);
	}

	public Boolean sismember(String key, String member) {
		return jedis.sismember(key, member);
	}

	public Long scard(byte[] key) {
		return jedis.scard(key);
	}

	public Set<String> sinter(String... keys) {
		return jedis.sinter(keys);
	}

	public Boolean sismember(byte[] key, byte[] member) {
		return jedis.sismember(key, member);
	}

	public Set<byte[]> sinter(byte[]... keys) {
		return jedis.sinter(keys);
	}

	public Long sinterstore(String dstkey, String... keys) {
		return jedis.sinterstore(dstkey, keys);
	}

	public Set<String> sunion(String... keys) {
		return jedis.sunion(keys);
	}

	public Long sinterstore(byte[] dstkey, byte[]... keys) {
		return jedis.sinterstore(dstkey, keys);
	}

	public Set<byte[]> sunion(byte[]... keys) {
		return jedis.sunion(keys);
	}

	public Long sunionstore(String dstkey, String... keys) {
		return jedis.sunionstore(dstkey, keys);
	}

	public Set<String> sdiff(String... keys) {
		return jedis.sdiff(keys);
	}

	public Long sunionstore(byte[] dstkey, byte[]... keys) {
		return jedis.sunionstore(dstkey, keys);
	}

	public Set<byte[]> sdiff(byte[]... keys) {
		return jedis.sdiff(keys);
	}

	public Long sdiffstore(String dstkey, String... keys) {
		return jedis.sdiffstore(dstkey, keys);
	}

	public String srandmember(String key) {
		return jedis.srandmember(key);
	}

	public Long sdiffstore(byte[] dstkey, byte[]... keys) {
		return jedis.sdiffstore(dstkey, keys);
	}

	public byte[] srandmember(byte[] key) {
		return jedis.srandmember(key);
	}

	public List<String> sort(String key) {
		return jedis.sort(key);
	}

	public Transaction multi() {
		return jedis.multi();
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		return jedis.sort(key, sortingParameters);
	}

	public List<Object> multi(TransactionBlock jedisTransaction) {
		return jedis.multi(jedisTransaction);
	}

	public void connect() {
		jedis.connect();
	}

	public void disconnect() {
		jedis.disconnect();
	}

	public List<byte[]> sort(byte[] key) {
		return jedis.sort(key);
	}

	public List<String> blpop(int timeout, String... keys) {
		return jedis.blpop(timeout, keys);
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		return jedis.sort(key, sortingParameters);
	}

	public List<byte[]> blpop(int timeout, byte[]... keys) {
		return jedis.blpop(timeout, keys);
	}

	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		return jedis.sort(key, sortingParameters, dstkey);
	}

	public Long sort(String key, String dstkey) {
		return jedis.sort(key, dstkey);
	}

	public List<String> brpop(int timeout, String... keys) {
		return jedis.brpop(timeout, keys);
	}

	public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		return jedis.sort(key, sortingParameters, dstkey);
	}

	public Long sort(byte[] key, byte[] dstkey) {
		return jedis.sort(key, dstkey);
	}

	public List<byte[]> brpop(int timeout, byte[]... keys) {
		return jedis.brpop(timeout, keys);
	}

	public String auth(String password) {
		return jedis.auth(password);
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		jedis.subscribe(jedisPubSub, channels);
	}

	public Long publish(String channel, String message) {
		return jedis.publish(channel, message);
	}

	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		jedis.psubscribe(jedisPubSub, patterns);
	}

	public List<Object> pipelined(PipelineBlock jedisPipeline) {
		return jedis.pipelined(jedisPipeline);
	}

	public Pipeline pipelined() {
		return jedis.pipelined();
	}

	public Long strlen(String key) {
		return jedis.strlen(key);
	}

	public Long lpushx(String key, String string) {
		return jedis.lpushx(key, string);
	}

	public Long persist(String key) {
		return jedis.persist(key);
	}

	public Long rpushx(String key, String string) {
		return jedis.rpushx(key, string);
	}

	public String echo(String string) {
		return jedis.echo(string);
	}

	public Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		return jedis.linsert(key, where, pivot, value);
	}

	public String brpoplpush(String source, String destination, int timeout) {
		return jedis.brpoplpush(source, destination, timeout);
	}

	public boolean setbit(String key, long offset, boolean value) {
		return jedis.setbit(key, offset, value);
	}

	public boolean getbit(String key, long offset) {
		return jedis.getbit(key, offset);
	}

	public long setrange(String key, long offset, String value) {
		return jedis.setrange(key, offset, value);
	}

	public String getrange(String key, long startOffset, long endOffset) {
		return jedis.getrange(key, startOffset, endOffset);
	}

	public String save() {
		return jedis.save();
	}

	public String bgsave() {
		return jedis.bgsave();
	}

	public String bgrewriteaof() {
		return jedis.bgrewriteaof();
	}

	public Long lastsave() {
		return jedis.lastsave();
	}

	public String shutdown() {
		return jedis.shutdown();
	}

	public String info() {
		return jedis.info();
	}

	public void monitor(JedisMonitor jedisMonitor) {
		jedis.monitor(jedisMonitor);
	}

	public String slaveof(String host, int port) {
		return jedis.slaveof(host, port);
	}

	public String slaveofNoOne() {
		return jedis.slaveofNoOne();
	}

	public List<String> configGet(String pattern) {
		return jedis.configGet(pattern);
	}

	public String configResetStat() {
		return jedis.configResetStat();
	}

	public String configSet(String parameter, String value) {
		return jedis.configSet(parameter, value);
	}

	public boolean isConnected() {
		return jedis.isConnected();
	}

	public Long strlen(byte[] key) {
		return jedis.strlen(key);
	}

	public void sync() {
		jedis.sync();
	}

	public Long lpushx(byte[] key, byte[] string) {
		return jedis.lpushx(key, string);
	}

	public Long persist(byte[] key) {
		return jedis.persist(key);
	}

	public Long rpushx(byte[] key, byte[] string) {
		return jedis.rpushx(key, string);
	}

	public byte[] echo(byte[] string) {
		return jedis.echo(string);
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		return jedis.linsert(key, where, pivot, value);
	}

	public String debug(DebugParams params) {
		return jedis.debug(params);
	}

	public Client getClient() {
		return jedis.getClient();
	}

	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		return jedis.brpoplpush(source, destination, timeout);
	}

	public Boolean exists(byte[] key) {
		return jedis.exists(key);
	}

	public Long setbit(byte[] key, long offset, byte[] value) {
		return jedis.setbit(key, offset, value);
	}

	public Long getbit(byte[] key, long offset) {
		return jedis.getbit(key, offset);
	}

	public long setrange(byte[] key, long offset, byte[] value) {
		return jedis.setrange(key, offset, value);
	}

	public String getrange(byte[] key, long startOffset, long endOffset) {
		return jedis.getrange(key, startOffset, endOffset);
	}

	public Long publish(byte[] channel, byte[] message) {
		return jedis.publish(channel, message);
	}

	public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		jedis.subscribe(jedisPubSub, channels);
	}

	public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		jedis.psubscribe(jedisPubSub, patterns);
	}

	public Long getDB() {
		return jedis.getDB();
	}

	public int hashCode() {
		return jedis.hashCode();
	}

	public Long hdel(String key, String field) {
		return jedis.hdel(key, field);
	}

	public Long hdel(byte[] key, byte[] field) {
		return jedis.hdel(key, field);
	}

	public String toString() {
		return jedis.toString();
	}

	public String type(String key) {
		return jedis.type(key);
	}

	public String type(byte[] key) {
		return jedis.type(key);
	}

	public Long ttl(String key) {
		return jedis.ttl(key);
	}

	public Long ttl(byte[] key) {
		return jedis.ttl(key);
	}

	public Long zadd(String key, double score, String member) {
		return jedis.zadd(key, score, member);
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		return jedis.zadd(key, score, member);
	}

	public Set<String> zrange(String key, int start, int end) {
		return jedis.zrange(key, start, end);
	}

	public Long zrem(String key, String member) {
		return jedis.zrem(key, member);
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		return jedis.zrange(key, start, end);
	}

	public Double zincrby(String key, double score, String member) {
		return jedis.zincrby(key, score, member);
	}

	public Long zrem(byte[] key, byte[] member) {
		return jedis.zrem(key, member);
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		return jedis.zincrby(key, score, member);
	}

	public Long zrank(String key, String member) {
		return jedis.zrank(key, member);
	}

	public Long zrank(byte[] key, byte[] member) {
		return jedis.zrank(key, member);
	}

	public Long zrevrank(String key, String member) {
		return jedis.zrevrank(key, member);
	}

	public Long zrevrank(byte[] key, byte[] member) {
		return jedis.zrevrank(key, member);
	}

	public Set<String> zrevrange(String key, int start, int end) {
		return jedis.zrevrange(key, start, end);
	}

	public Set<Tuple> zrangeWithScores(String key, int start, int end) {
		return jedis.zrangeWithScores(key, start, end);
	}

	public Set<Tuple> zrevrangeWithScores(String key, int start, int end) {
		return jedis.zrevrangeWithScores(key, start, end);
	}

	public Long zcard(String key) {
		return jedis.zcard(key);
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		return jedis.zrevrange(key, start, end);
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		return jedis.zrangeWithScores(key, start, end);
	}

	public Double zscore(String key, String member) {
		return jedis.zscore(key, member);
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		return jedis.zrevrangeWithScores(key, start, end);
	}

	public Long zcard(byte[] key) {
		return jedis.zcard(key);
	}

	public String watch(String... keys) {
		return jedis.watch(keys);
	}

	public Double zscore(byte[] key, byte[] member) {
		return jedis.zscore(key, member);
	}

	public String watch(byte[]... keys) {
		return jedis.watch(keys);
	}

	public String unwatch() {
		return jedis.unwatch();
	}

	public Long zcount(String key, double min, double max) {
		return jedis.zcount(key, min, max);
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		return jedis.zrangeByScore(key, min, max);
	}

	public Long zcount(byte[] key, double min, double max) {
		return jedis.zcount(key, min, max);
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		return jedis.zrangeByScore(key, min, max);
	}

	public Set<String> zrangeByScore(String key, String min, String max) {
		return jedis.zrangeByScore(key, min, max);
	}

	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		return jedis.zrangeByScore(key, min, max, offset, count);
	}

	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		return jedis.zrangeByScore(key, min, max);
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max,
			int offset, int count) {
		return jedis.zrangeByScore(key, min, max, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return jedis.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		return jedis.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min,
			double max, int offset, int count) {
		return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min,
			double max, int offset, int count) {
		return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return jedis.zrevrangeByScore(key, max, min);
	}

	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return jedis.zrevrangeByScore(key, max, min);
	}

	public Set<String> zrevrangeByScore(String key, double max, double min,
			int offset, int count) {
		return jedis.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min) {
		return jedis.zrevrangeByScoreWithScores(key, max, min);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min, int offset, int count) {
		return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Long zremrangeByRank(String key, int start, int end) {
		return jedis.zremrangeByRank(key, start, end);
	}

	public Long zremrangeByScore(String key, double start, double end) {
		return jedis.zremrangeByScore(key, start, end);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		return jedis.zrevrangeByScore(key, max, min);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		return jedis.zrevrangeByScore(key, max, min);
	}

	public Long zunionstore(String dstkey, String... sets) {
		return jedis.zunionstore(dstkey, sets);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
			int offset, int count) {
		return jedis.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min) {
		return jedis.zrevrangeByScoreWithScores(key, max, min);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min, int offset, int count) {
		return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		return jedis.zremrangeByRank(key, start, end);
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		return jedis.zremrangeByScore(key, start, end);
	}

	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		return jedis.zunionstore(dstkey, params, sets);
	}

	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		return jedis.zunionstore(dstkey, sets);
	}

	public Long zinterstore(String dstkey, String... sets) {
		return jedis.zinterstore(dstkey, sets);
	}

	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		return jedis.zunionstore(dstkey, params, sets);
	}

	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		return jedis.zinterstore(dstkey, params, sets);
	}

	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		return jedis.zinterstore(dstkey, sets);
	}

	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		return jedis.zinterstore(dstkey, params, sets);
	}






}
