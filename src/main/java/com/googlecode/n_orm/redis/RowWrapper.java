package com.googlecode.n_orm.redis;

import java.util.Map;
import java.util.TreeMap;

import com.googlecode.n_orm.storeapi.Row;

public class RowWrapper implements Row {
	private final String key;
	private final Map<String, Map<String, byte[]>> values;
	
	public RowWrapper(String table, String family, String clazz, String id, Map<String, String> keysvalues) {
		this.key = id;
		this.values = new TreeMap<String, Map<String,byte[]>>();
		
		Map<String,byte[]> results = new TreeMap<String,byte[]>();
		for(Map.Entry<String, String> e : keysvalues.entrySet()) {
			results.put(e.getKey(), e.getValue().getBytes());
		}
		
		this.values.put(family, results);
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public Map<String, Map<String, byte[]>> getValues() {
		return this.values;
	}

}
