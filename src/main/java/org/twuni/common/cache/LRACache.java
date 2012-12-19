package org.twuni.common.cache;

/**
 * This is an implementation of a Least Recently Added cache.
 */
public class LRACache<K, V> extends Cache<K, V> {

	protected class Node {

		public K key;
		public Node next;

	}

	private Node oldest;
	private Node newest;

	public LRACache( int capacity ) {
		super( capacity );
	}

	@Override
	protected K eject() {
		K ejected = oldest.key;
		oldest = oldest.next;
		return ejected;
	}

	@Override
	protected void onPut( K key, V value ) {

		Node node = new Node();

		node.key = key;

		if( oldest == null ) {
			oldest = node;
			newest = node;
			return;
		}

		if( newest == null ) {
			newest = node;
		} else {
			newest.next = node;
			newest = newest.next;
		}

	}

}
