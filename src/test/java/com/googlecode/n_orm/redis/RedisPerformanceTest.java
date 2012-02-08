package com.googlecode.n_orm.redis;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.googlecode.n_orm.PerformanceTests;
import com.googlecode.n_orm.StoreTestLauncher;

@RunWith(Suite.class)
@SuiteClasses(PerformanceTests.class)
public class RedisPerformanceTest {

	@BeforeClass public static void setupStore() {
		
		RedisStore rs = (RedisStore) RedisStore.getStore();
		rs.start();
		//rs.flushAll();
		
		
		StoreTestLauncher.INSTANCE = new RedisLauncher();
	}
	
}


