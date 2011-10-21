package com.googlecode.n_orm.redis;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.googlecode.n_orm.storeapi.Constraint;


public class BasicTest {
	private static RedisStore store;
	private static final String testTable = "testtable";
	
	@BeforeClass
	public static void prepareStore() {
		store = new RedisStore();
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
		dataFamily2.put("cle-fam2", new String("1").getBytes());
		
		data.put("family1", dataFamily1);
		data.put("family2", dataFamily2);
		
		store.storeChanges(testTable, "123456", data, null, null);
		store.storeChanges(testTable, "123457", data, null, null);
		store.storeChanges(testTable, "123458", data, null, null);
		store.storeChanges(testTable, "300000", data, null, null);
		store.storeChanges(testTable, "300010", data, null, null);
	}
	
	@Test
	public void test02GetTableWithFamily() {
		Map<String, byte[]> result = store.get(testTable, "123456", "family1");
		assertEquals("valeur", new String(result.get("cle")));
	}
	
	@Test
	public void test03Count() {
		assertEquals(5,store.count(testTable, null));
	}

	
	@Test
	public void test06IdToStringWithNumbers() {
		assertTrue(store.idToScore("1") < store.idToScore("2"));
		assertTrue(store.idToScore("1") < store.idToScore("10"));
		
		assertTrue(store.idToScore("123") > store.idToScore("12"));
		assertTrue(store.idToScore("123") < store.idToScore("13"));
		assertTrue(store.idToScore("12") == store.idToScore("12"));

	}
	
	@Test
	public void test06IdToStringWithLetters() {
		assertTrue(store.idToScore("a") < store.idToScore("b"));
		assertTrue(store.idToScore("aaa") < store.idToScore("bc"));

		assertTrue(store.idToScore("aa") > store.idToScore("a"));
		assertTrue(store.idToScore("aabcd") < store.idToScore("ab"));
		assertTrue(store.idToScore("aba") == store.idToScore("aba"));

	}
	
	
	@Test
	public void test05CountWithConstraint() {
		assertEquals(3,store.count(testTable, new Constraint("123450", "123460")));
		assertEquals(3,store.count(testTable, new Constraint("1", "2")));
		assertEquals(0,store.count(testTable, new Constraint("a", "z")));
	}
	
	@Test
	public void test07getTableWithSetFamilies() {
		Set<String> families = new HashSet<String>();
		families.add("family1");
		families.add("family2");
		Map<String, Map<String, byte[]>> result = store.get(testTable, "123456", families);
		
		assertEquals("1", new String(result.get("family2").get("cle-fam2")));
		assertFalse("not a valid value".equals(new String(result.get("family2").get("cle-fam2"))));
	}
	
	@Test
	public void test08existsId() {
		assertTrue(store.exists(testTable, "123456"));
		assertFalse(store.exists(testTable, "no valid id"));
	}
	
	@Test
	public void test09existsIdWithFamily() {
		assertTrue(store.exists(testTable, "123456", "family1"));
		assertTrue(store.exists(testTable, "123456", "family2"));
		assertFalse(store.exists(testTable, "not a valid id","family1"));
		assertFalse(store.exists(testTable, "123456","not a valid family"));
	}
	
	@Test
	public void test10delete() {
		assertTrue(store.exists(testTable, "300000"));
		assertTrue(store.exists(testTable, "300000", "family1"));
		store.delete(testTable, "300000");
		assertFalse(store.exists(testTable, "300000"));
		assertFalse(store.exists(testTable, "300000", "family1"));
	}

}
