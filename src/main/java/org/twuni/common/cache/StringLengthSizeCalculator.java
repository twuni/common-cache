package org.twuni.common.cache;

public class StringLengthSizeCalculator<K> implements SizeCalculator<K, String> {

	@Override
	public int sizeOf( K key, String value ) {
		return value != null ? value.length() : 0;
	}

}
