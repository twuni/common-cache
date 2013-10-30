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
		if( oldest == null ) {
			return null;
		}
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

	@Override
	protected void onRemove( K key, V value ) {
		Node previous = null;
		for( Node current = newest; current != null; current = current.next ) {
			if( current.key.equals( key ) ) {
				if( previous == null ) {
					newest = current;
					return;
				}
				previous.next = current.next;
				return;
			}
		}
	}

}
