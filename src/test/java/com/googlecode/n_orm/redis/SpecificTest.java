package com.googlecode.n_orm.redis;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Constraint;
import com.googlecode.n_orm.storeapi.Row;


public class SpecificTest {
	private static RedisStore store;
	private static final String testTable = "testtable";
	
	@BeforeClass
	public static void prepareStore() {
		store = (RedisStore) RedisStore.getStore();
		store.start();
		store.flushAll();
	}

	@Test
	public void test01InsertTable() {
		Map<String, Map<String, byte[]>> data = new HashMap<String, Map<String, byte[]>>();
		Map<String, byte[]> dataFamily1 = new HashMap<String, byte[]>();
		Map<String, byte[]> dataFamily2 = new HashMap<String, byte[]>();
		dataFamily1.put("cle", new String("valeur").getBytes());
		dataFamily1.put("cle2", new String("valeur2").getBytes());
		dataFamily1.put("cle21", new String("valeur21").getBytes());
		dataFamily1.put("cle3", new String("valeur3").getBytes());
		dataFamily2.put("cle-fam2", new String("1").getBytes());
		dataFamily2.put("cle-incr", new String("42").getBytes());
		
		data.put("family1", dataFamily1);
		data.put("family2", dataFamily2);
		
		store.storeChanges(testTable, "123456", data, null, null);
		
		Set<String> cf = new TreeSet<String>();
		cf.add("family1");
		cf.add("family2");
		cf.add("family3");
		cf.add("family4");
		//System.out.println(store.get(testTable, "123456", cf));
		
	}
	
}

