package org.twuni.common.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of a Least Recently Used cache. As new items are added to the cache,
 * the least recently accessed cache entries are given priority when ejecting to make room.
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

		public void clear() {
			key = null;
			value = null;
			if( next != null ) {
				next.clear();
				next = null;
			}
		}

		public boolean isLast() {
			return next == null;
		}

		public Node last() {
			Node last = this;
			while( !last.isLast() ) {
				last = last.next;
			}
			return last;
		}

		public List<K> keys() {
			List<K> order = new ArrayList<K>();
			for( Node current = this; current != null; current = current.next ) {
				order.add( current.key );
			}
			return order;
		}

		public boolean matches( K key ) {
			return key != null ? key.equals( this.key ) : this.key == null;
		}

		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			s.append( key );
			if( next != null ) {
				s.append( '→' ).append( next );
			}
			return s.toString();
		}

	}

	private Node leastRecentlyUsed;

	public LRUCache( int capacity ) {
		super( capacity );
	}

	public LRUCache( int capacity, SizeCalculator<K, V> calculator ) {
		super( capacity, calculator );
	}

	@Override
	public void clear() {
		super.clear();
		if( leastRecentlyUsed != null ) {
			leastRecentlyUsed.clear();
			leastRecentlyUsed = null;
		}
	}

	@Override
	protected K eject() {
		if( leastRecentlyUsed == null ) {
			return null;
		}
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

	@Override
	protected void onRemove( K key, V value ) {
		Node previous = null;
		for( Node current = leastRecentlyUsed; current != null; current = current.next ) {
			if( current.key.equals( key ) ) {
				if( previous == null ) {
					leastRecentlyUsed = current;
					return;
				}
				previous.next = current.next;
				return;
			}
		}
	}

	protected void onUsed( K key, V value ) {

		if( leastRecentlyUsed == null ) {
			leastRecentlyUsed = new Node( key, value );
			return;
		}

		Node previous = null;

		for( Node current = leastRecentlyUsed; current != null; current = current.next ) {

			if( current.matches( key ) ) {

				if( current.isLast() ) {
					break;
				}

				if( previous == null ) {
					leastRecentlyUsed = current.next;
					current.last().next = current;
					current.next = null;
					break;
				}

				previous.next = current.next;
				current.last().next = current;
				current.next = null;
				break;
			}

			if( current.isLast() ) {
				current.next = new Node( key, value );
				break;
			}

			previous = current;

		}

	}

	public List<K> inspectOrdering() {
		return leastRecentlyUsed != null ? leastRecentlyUsed.keys() : null;
	}

	@Override
	public String toString() {
		return leastRecentlyUsed != null ? leastRecentlyUsed.toString() : "(empty)";
	}

}
