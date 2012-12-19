package org.twuni.common.cache;

/**
 * This is an implementation of a Least Recently Used cache. As new items are added to the
 * cache, the least recently accessed cache entries are given priority when ejecting to make
 * room.
 */
public class LRUCache<K, V> extends Cache<K, V> {

	protected class Node {

		public K key;
		public V value;
		public Node next;

		public Node( K key, V value ) {
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			return key.toString();
		}

	}

	private Node leastRecentlyUsed;

	public LRUCache( int capacity ) {
		super( capacity );
	}

	@Override
	protected K eject() {
		K ejected = leastRecentlyUsed.key;
		leastRecentlyUsed = leastRecentlyUsed.next;
		return ejected;
	}

	@Override
	protected void onGet( K key, V value ) {
		onUsed( key, value );
	}

	@Override
	protected void onPut( K key, V value ) {
		onUsed( key, value );
	}

	protected void onUsed( K key, V value ) {

		if( leastRecentlyUsed == null ) {
			leastRecentlyUsed = new Node( key, value );
			return;
		}

		Node previous = null;

		for( Node current = leastRecentlyUsed; current != null; current = current.next ) {

			if( !current.key.equals( key ) ) {
				previous = current;
				if( current.next == null ) {
					current.next = new Node( key, value );
				}
				continue;
			}

			if( current.next == null ) {
				continue;
			}

			if( previous == null ) {
				leastRecentlyUsed = current.next;
				current.next = leastRecentlyUsed.next;
				leastRecentlyUsed.next = current;
			} else {
				previous.next = current.next;
				current.next = previous.next.next;
				previous.next.next = current;
			}

			current = current.next;
			previous = current;

			if( current == null ) {
				break;
			}

		}

	}

}
