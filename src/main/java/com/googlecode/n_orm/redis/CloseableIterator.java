package com.googlecode.n_orm.redis;

import java.util.Iterator;
import java.util.List;

import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Row;

final class CloseableIterator implements CloseableKeyIterator {
	private final Iterator<RowWrapper> iterator;
	
	
	CloseableIterator(List<RowWrapper> res) {
		this.iterator = res.iterator();
	}
	
	@Override
	public void close() {
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Row next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		throw new IllegalStateException(
				"Cannot remove key from a result set.");		
	}

}
