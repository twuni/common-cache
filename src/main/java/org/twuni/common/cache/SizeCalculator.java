package org.twuni.common.cache;

public interface SizeCalculator<K, V> {

	/**
	 * Determines the size of the item for the given key and value.
	 * 
	 * @param key
	 *            of the cache item.
	 * @param value
	 *            of the cache item.
	 * @return the size of the cache item.
	 */
	public int sizeOf( K key, V value );

}
