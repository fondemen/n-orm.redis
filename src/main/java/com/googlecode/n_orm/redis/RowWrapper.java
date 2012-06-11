package com.googlecode.n_orm.redis;

import com.googlecode.n_orm.storeapi.DefaultColumnFamilyData;
import com.googlecode.n_orm.storeapi.Row;

public class RowWrapper implements Row {
	private final String key;
	private final ColumnFamilyData values;
	
	public RowWrapper(String id, ColumnFamilyData val) {
		this.key = id;
		if(val == null)
			val = new DefaultColumnFamilyData();
		
		this.values = val;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public ColumnFamilyData getValues() {
		return this.values;
	}

}
