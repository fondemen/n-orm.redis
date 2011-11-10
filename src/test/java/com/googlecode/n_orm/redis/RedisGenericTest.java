package com.googlecode.n_orm.redis;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.googlecode.n_orm.GenericTests;
import com.googlecode.n_orm.StoreTestLauncher;

@RunWith(Suite.class)
@SuiteClasses(GenericTests.class)
public class RedisGenericTest {

	@BeforeClass public static void setupStore() {
		
		//Store rs = RedisStore.getStore();
		//rs.start();
		//((RedisStore) rs).flushAll();
		
		
		StoreTestLauncher.INSTANCE = new RedisLauncher();
	}
	
}


