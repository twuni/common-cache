package org.twuni.common.cache;

import java.util.HashMap;
import java.util.Map;

import org.twuni.common.cache.exception.SizeException;

public abstract class Cache<K, V> {

	private final Map<K, V> implementation = new HashMap<K, V>();
	protected final int capacity;
	protected int size;

	/**
	 * @param capacity
	 *            the maximum size of this cache.
	 */
	public Cache( int capacity ) {
		this.capacity = capacity;
	}
	
	public void clear() {
		implementation.clear();
		size = 0;
	}

	/**
	 * This method is called when an item needs to be removed from the cache.
	 * 
	 * @return the key of the item to be ejected.
	 */
	protected abstract K eject();

	public final V get( K key ) {
		V value = implementation.get( key );
		onGet( key, value );
		return value;
	}

	public int getCapacity() {
		return capacity;
	}

	public boolean isEmpty() {
		return size <= 0;
	}

	public boolean isFull() {
		return size >= capacity;
	}

	/**
	 * This method is called whenever an item is retrieved from this cache. By default, it does
	 * nothing.
	 * 
	 * @param key
	 * @param value
	 */
	protected void onGet( K key, V value ) {
		// Do nothing.
	}

	/**
	 * This method is called whenever an item is added to this cache. By default, does nothing.
	 * 
	 * @param key
	 * @param value
	 */
	protected void onPut( K key, V value ) {
		// Do nothing.
	}

	/**
	 * This method is called whenever an item is removed from this cache. By default, does nothing.
	 * 
	 * @param key
	 * @param value
	 */
	protected void onRemove( K key, V value ) {
		// Do nothing.
	}

	public final void remove( K key ) {
		V value = implementation.get( key );
		int sizeOfOldEntry = sizeOf( key, value );
		implementation.remove( key );
		size -= sizeOfOldEntry;
		onRemove( key, value );
	}

	public final void put( K key, V value ) {

		int sizeOfNewEntry = sizeOf( key, value );

		if( sizeOfNewEntry > capacity ) {
			throw new SizeException( sizeOfNewEntry, capacity );
		}

		while( size + sizeOfNewEntry > capacity ) {
			K ejected = eject();
			size -= sizeOf( ejected, implementation.get( ejected ) );
			implementation.remove( ejected );
		}

		implementation.put( key, value );
		size += sizeOfNewEntry;

		onPut( key, value );

	}

	public int size() {
		return size;
	}

	/**
	 * Determines the size of the item for the given key and value. By default, all items have a
	 * size of 1.
	 * 
	 * @param key
	 *            of the cache item.
	 * @param value
	 *            of the cache item.
	 * @return the size of the cache item. Returns 1 by default.
	 */
	protected int sizeOf( K key, V value ) {
		return value == null ? 0 : 1;
	}

}
