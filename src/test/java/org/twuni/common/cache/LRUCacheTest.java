package org.twuni.common.cache;

import org.junit.Assert;
import org.junit.Test;
import org.twuni.common.cache.exception.SizeException;

public class LRUCacheTest extends Assert {

	private static <K, V> void assertCacheOrdering( LRUCache<K, V> cache, K... expected ) {
		assertArrayEquals( expected, cache.inspectOrdering().toArray() );
	}

	private static <K, V> void initialState( LRUCache<K, V> cache, K... keys ) {
		for( K key : keys ) {
			cache.get( key );
		}
	}

	@Test
	public void clear_shouldAdjustSizeToZero() {
		LRUCache<String, String> cache = prepareCacheForTest_clear( 5 );
		Assert.assertEquals( 0, cache.size() );
	}

	@Test
	public void clear_shouldNotAffectCapacity() {
		LRUCache<String, String> cache = prepareCacheForTest_clear( 5 );
		Assert.assertEquals( 5, cache.getCapacity() );
	}

	@Test
	public void clear_shouldRemoveExistingItems() {
		LRUCache<String, String> cache = prepareCacheForTest_clear( 5 );
		Assert.assertNull( cache.get( "test" ) );
	}

	private LRUCache<String, String> prepareCacheForTest_clear( int capacity ) {
		LRUCache<String, String> cache = new LRUCache<String, String>( capacity );
		cache.put( "test", "12345" );
		cache.clear();
		return cache;
	}

	@Test
	public void remove_shouldAdjustSizeWhenCacheContainsGivenKey() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 100 );
		Assert.assertEquals( 0, cache.size() );
		cache.put( "dog", "Boxer" );
		Assert.assertEquals( 1, cache.size() );
		cache.remove( "dog" );
		Assert.assertEquals( 0, cache.size() );
	}

	@Test
	public void remove_shouldDoNothingWhenCacheDoesNotContainGivenKey() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 100 );
		Assert.assertEquals( 0, cache.size() );
		cache.put( "dog", "Boxer" );
		Assert.assertEquals( 1, cache.size() );
		cache.remove( "cat" );
		Assert.assertEquals( 1, cache.size() );
	}

	@Test
	public void remove_shouldDoNothingWhenCacheIsEmpty() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 100 );
		Assert.assertTrue( cache.isEmpty() );
		cache.remove( "test" );
		Assert.assertTrue( cache.isEmpty() );
	}

	@Test
	public void testCacheEjectsLeastRecentlyUsedValueWhenCapacityReached() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 2 );
		cache.put( "test", "123" );
		cache.put( "another test", "456" );
		cache.get( "test" );
		cache.put( "final test", "789" );
		Assert.assertNull( cache.get( "another test" ) );
		Assert.assertEquals( "789", cache.get( "final test" ) );
		Assert.assertEquals( "123", cache.get( "test" ) );
	}

	@Test
	public void testSingleCapacityCacheEjectsOldestValueWhenCapacityReached() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 1 );
		cache.put( "test", "123" );
		cache.put( "another test", "456" );
		Assert.assertEquals( "456", cache.get( "another test" ) );
		Assert.assertNull( cache.get( "test" ) );
	}

	@Test
	public void testSingleCapacityCacheReturnsValueOnGet() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 1 );
		String expected = "123";
		cache.put( "test", expected );
		String actual = cache.get( "test" );
		Assert.assertEquals( expected, actual );
	}

	@Test( expected = SizeException.class )
	public void testZeroCapacityCacheThrowsSizeExceptionOnPut() {
		LRUCache<String, String> cache = new LRUCache<String, String>( 0 );
		cache.put( "key", "value" );
	}

	@Test
	public void usingHighestPriorityKey_shouldReduceItToLowestPriority() {

		LRUCache<String, String> cache = new LRUCache<String, String>( 5 );

		initialState( cache, "A", "B", "C" );
		cache.get( "A" );
		assertCacheOrdering( cache, "B", "C", "A" );

	}

	@Test
	public void usingLowestPriorityKey_shouldHaveNoEffect() {

		LRUCache<String, String> cache = new LRUCache<String, String>( 5 );

		initialState( cache, "A", "B", "C" );
		cache.get( "C" );
		assertCacheOrdering( cache, "A", "B", "C" );

	}

	@Test
	public void usingMidPriorityKey_shouldReduceItToLowestPriority() {

		LRUCache<String, String> cache = new LRUCache<String, String>( 5 );

		initialState( cache, "A", "B", "C" );
		cache.get( "B" );
		assertCacheOrdering( cache, "A", "C", "B" );

	}

}
