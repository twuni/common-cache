package org.twuni.common.cache;

import java.util.HashMap;
import java.util.Map;

import org.twuni.common.cache.exception.SizeException;

public abstract class Cache<K, V> {

	private final Map<K, V> implementation = new HashMap<K, V>();
	private final SizeCalculator<K, V> calculator;
	protected final int capacity;
	protected int size;

	/**
	 * Constructs a new cache using a {@link SimpleSizeCalculator}.
	 * 
	 * @param capacity
	 *            the maximum size of this cache.
	 */
	public Cache( int capacity ) {
		this( capacity, new SimpleSizeCalculator<K, V>() );
	}

	/**
	 * @param capacity
	 *            the maximum size of this cache.
	 * @param calculator
	 *            the algorithm to use for determining the size of individual items in the cache.
	 */
	public Cache( int capacity, SizeCalculator<K, V> calculator ) {
		this.capacity = capacity;
		this.calculator = calculator;
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

		final int sizeOfNewEntry = sizeOf( key, value );
		final int sizeOfOldEntry = sizeOf( key, implementation.get( key ) );
		final int growth = sizeOfNewEntry - sizeOfOldEntry;

		if( capacity < growth ) {
			throw new SizeException( growth, capacity );
		}

		while( capacity < size + growth ) {
			K ejectedKey = eject();
			if( !key.equals( ejectedKey ) ) {
				size -= sizeOf( ejectedKey, implementation.get( ejectedKey ) );
			}
			implementation.remove( ejectedKey );
		}

		implementation.put( key, value );
		size += growth;

		onPut( key, value );

	}

	public int size() {
		return size;
	}

	/**
	 * Determines the size of the item for the given key and value.
	 * 
	 * @param key
	 *            of the cache item.
	 * @param value
	 *            of the cache item.
	 * @return the size of the cache item.
	 */
	protected int sizeOf( K key, V value ) {
		return calculator.sizeOf( key, value );
	}

}
