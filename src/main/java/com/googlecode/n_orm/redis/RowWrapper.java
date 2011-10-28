package com.googlecode.n_orm.redis;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.n_orm.storeapi.Row;

public class RowWrapper implements Row {
	private final String key;
	private final Map<String, Map<String, byte[]>> values;
	
	public RowWrapper(String id, Map<String, Map<String, byte[]>> val) {
		this.key = id;
		if(val == null)
			val = new HashMap<String, Map<String,byte[]>>();
		
		this.values = val;
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
