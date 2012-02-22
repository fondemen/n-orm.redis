package com.googlecode.n_orm.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.googlecode.n_orm.storeapi.CloseableKeyIterator;
import com.googlecode.n_orm.storeapi.Row;

final class CloseableIterator implements CloseableKeyIterator {
	private String startKey;
	private final String stopKey, table;
	private final Set<String> families;
	private final RedisStore store;
	private Iterator<Row> currentIterator;
	private final int limit;
	private int count;
	private boolean lastCall, done;

	public CloseableIterator(RedisStore store, String startKey,
			String stopKey, String table, int limit, Set<String> families) {
		this.store = store;
		this.stopKey = stopKey;
		this.startKey = startKey;
		this.table = table;
		this.limit = limit;
		this.families = families;
		this.currentIterator = null;
		this.count = 0;
		this.done = false;
		this.lastCall = false;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean hasNext() {
		if(count > limit || done)
			return false;
		
		if(currentIterator != null && currentIterator.hasNext())
			return true;
		
		this.loadNextElements();
		if (this.done)
			return false;
		this.done = !this.currentIterator.hasNext();
		return !this.done;
	}

	@Override
	public Row next() {
		if(count++ > limit || done) {
			throw new NoSuchElementException();
		}
		if(currentIterator != null && this.currentIterator.hasNext()) {
			// On a encore des données en attente
			return currentIterator.next();
		} else {
			// On est au bout de l'iterateur, il faut recharger les éléments suivants
			this.loadNextElements();
			if(this.currentIterator.hasNext())
				return this.currentIterator.next();
			else {
				done = true;
				throw new NoSuchElementException();
			}
		}
	}

	private void loadNextElements() {
		// Pas de chargement si l'itérateur n'est pas vide
		if(this.currentIterator != null && this.currentIterator.hasNext() || this.lastCall)
			return;
		
		List<Row> result;
		int scanCaching = store.getScanCaching();
		result = this.get(table, startKey, stopKey, families, scanCaching, (count != 0));
		this.currentIterator = result.iterator();
		int size = result.size();
		
		if (size == 0)
			this.done = true;
		
		if (size < scanCaching)
			this.lastCall = true;
		
		if(result.size() >= 1) {
			// dernière clé
			this.startKey = result.get(result.size()-1).getKey();
		}
		
	}
	
	private List<Row> get(String table, String startKey, String stopKey,
			Set<String> families2, int maxBulk, boolean excludeFirstElement) {
		List<Row> result = new ArrayList<Row>(maxBulk);
		
		int firstRank = startKey == null ? 0 : store.idToRank(table, startKey, false);
		if (excludeFirstElement)
			firstRank++;

		Set<String> redisKeys = store.getReadableRedis().zrange(
				store.getKey(table), firstRank, firstRank + maxBulk);

		for (String key : redisKeys) {
			if (stopKey == null || stopKey.compareTo(key) > 0)
				result.add(new RowWrapper(key, store.get(table, key, families2)));
		}
		return result;
	}

	@Override
	public void remove() {
		throw new IllegalStateException(
				"Cannot remove key from a result set.");		
	}

}
