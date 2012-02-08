package com.googlecode.n_orm.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Row;

final class CloseableIterator implements CloseableKeyIterator {
	private static final int MAX_BULK = 100;
	private String startKey;
	private final String stopKey, table;
	private final Set<String> families;
	private final RedisStore store;
	private List<Row> currentRows;
	private Iterator<Row> currentIterator;
	private final int limit;
	private int count;

	public CloseableIterator(RedisStore store, String startKey,
			String stopKey, String table, int limit, Set<String> families) {
		this.store = store;
		this.startKey = startKey;
		this.stopKey = stopKey;
		this.table = table;
		this.limit = limit;
		this.families = families;
		this.currentRows = new ArrayList<Row>();
		this.currentIterator = this.currentRows.iterator();
		this.count = 0;
		
		// Force to load firsts elements
		hasNext();
	}

	@Override
	public void close() {
	}

	@Override
	public boolean hasNext() {
		if(count > limit)
			return false;
		
		if(currentIterator.hasNext())
			return true;
		
		this.loadNextElements();
		return this.currentIterator.hasNext();
	}

	@Override
	public Row next() {
		if(count++ > limit) {
			return null;
		}
		if(this.currentIterator.hasNext()) {
			// On a encore des données en attente
			return currentIterator.next();
		} else {
			// On est au bout de l'iterateur, il faut recharger les éléments suivants
			this.loadNextElements();
			if(this.currentIterator.hasNext())
				return this.currentIterator.next();
			else
				return null;
		}
	}

	private void loadNextElements() {
		// Pas de chargement si l'itérateur n'est pas vide
		if(this.currentIterator.hasNext())
			return;
		
		List<Row> result;
		result = store.get(table, startKey, stopKey, families, MAX_BULK, (count != 0));
		this.currentRows = result;
		this.currentIterator = currentRows.iterator();
		
		if(result.size() >= 1) {
			// dernière clé
			this.startKey = result.get(result.size()-1).getKey();
		}
		
	}

	@Override
	public void remove() {
		throw new IllegalStateException(
				"Cannot remove key from a result set.");		
	}

}
