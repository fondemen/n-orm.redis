package com.googlecode.n_orm.redis;

import redis.clients.jedis.Jedis;

public class JedisProxy extends Jedis {

	public JedisProxy(String host) {
		super(host);
	}

}
