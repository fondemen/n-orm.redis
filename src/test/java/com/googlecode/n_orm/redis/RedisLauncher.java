package com.googlecode.n_orm.redis;

import java.util.Map;
import java.util.TreeMap;

import com.googlecode.n_orm.StoreSelector;
import com.googlecode.n_orm.StoreTestLauncher;

public class RedisLauncher extends StoreTestLauncher {

	@Override
	public Map<String, Object> prepare(Class<?> testClass) {
		//Jedis jedis = new Jedis("localhost");
		//jedis.flushAll();
						
		Map<String, Object> p = new TreeMap<String, Object>();
		
		p.put(StoreSelector.STORE_DRIVERCLASS_PROPERTY, com.googlecode.n_orm.redis.RedisStore.class.getName());
		p.put(StoreSelector.STORE_DRIVERCLASS_STATIC_ACCESSOR, "getStore");
		
		return p;
	}

}
