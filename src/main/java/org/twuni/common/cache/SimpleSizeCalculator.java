package org.twuni.common.cache;

public class SimpleSizeCalculator<K, V> implements SizeCalculator<K, V> {

	@Override
	public int sizeOf( K key, V value ) {
		return value != null ? 1 : 0;
	}

}
