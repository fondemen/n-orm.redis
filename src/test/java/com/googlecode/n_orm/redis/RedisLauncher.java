package com.googlecode.n_orm.redis;

import java.util.Properties;

import com.googlecode.n_orm.StoreSelector;
import com.googlecode.n_orm.StoreTestLauncher;

public class RedisLauncher extends StoreTestLauncher {

	@Override
	public Properties prepare(Class<?> testClass) {
		//Jedis jedis = new Jedis("localhost");
		//jedis.flushAll();
						
		Properties p = new Properties();
		
		p.setProperty(StoreSelector.STORE_DRIVERCLASS_PROPERTY, com.googlecode.n_orm.redis.RedisStore.class.getName());
		p.setProperty(StoreSelector.STORE_DRIVERCLASS_STATIC_ACCESSOR, "getStore");
		
		return p;
	}

}
