package org.oddjob.beancmpr.matchables;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.oddjob.beancmpr.composite.ComparersByNameOrType;

public class SimpleMatchKeyTest extends TestCase {

	private class SomeMetaData implements MatchableMetaData {
		
		Map<String, Class<?>> meta = 
				new LinkedHashMap<String, Class<?>>();
		
		SomeMetaData add(String prop, Class<?> type) {
			meta.put(prop, type);
			return this;
		}
		
		@Override
		public Class<?> getPropertyType(String name) {
			return meta.get(name);
		}
		
		@Override
		public Iterable<String> getKeyProperties() {
			return meta.keySet();
		}
		
		@Override
		public Iterable<String> getOtherProperties() {
			throw new RuntimeException("Unexpected.");
		}
		
		@Override
		public Iterable<String> getValueProperties() {
			throw new RuntimeException("Unexpected.");
		}
	}
	
	public void testDifferentOneComponent() {
		
		SomeMetaData metaData = new SomeMetaData().
				add("fruit", String.class);

		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData, metaData);
		
		SimpleMatchKey key1 = new SimpleMatchKey(
				Arrays.asList("banana"), comparator);
		SimpleMatchKey key2 = new SimpleMatchKey(
				Arrays.asList("apple"), comparator);
		
		assertTrue(key1.compareTo(key2) > 0);
		assertTrue(key2.compareTo(key1) < 0);
		
		assertFalse(key1.equals(key2));
	}
	
	public void testDifferentTwoComponents() {
		
		SomeMetaData metaData = new SomeMetaData().
				add("fruit1", String.class).
				add("fruit2", String.class);

		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData, metaData);		
		
		SimpleMatchKey key1 = new SimpleMatchKey(
				Arrays.asList("banana", "banana"), comparator);
		SimpleMatchKey key2 = new SimpleMatchKey(
				Arrays.asList("banana", "apple"), comparator);
		
		assertTrue(key1.compareTo(key2) > 0);
		assertTrue(key2.compareTo(key1) < 0);
		
		assertFalse(key1.equals(key2));
	}

	public void testSameThreeComponents() {
		
		SomeMetaData metaData = new SomeMetaData().
				add("fruit1", String.class).
				add("fruit2", String.class).
				add("fruit3", String.class);
		
		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData, metaData);
		
		SimpleMatchKey key1 = new SimpleMatchKey(Arrays.asList(
				"orange", "banana", "apple"), comparator);
		SimpleMatchKey key2 = new SimpleMatchKey(Arrays.asList(
				"orange", "banana", "apple"), comparator);
		
		assertTrue(key1.compareTo(key2) == 0);
		assertTrue(key2.compareTo(key1) == 0);
		
		assertTrue(key1.equals(key2));
		assertEquals(key1.hashCode(), key2.hashCode());
	}
	
	public void testDifferentOneComponentNull() {
		
		SomeMetaData metaData = new SomeMetaData().
				add("fruit1", String.class).
				add("fruit2", String.class).
				add("fruit3", String.class);
		
		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData, metaData);		
		
		SimpleMatchKey key1 = new SimpleMatchKey(
				Arrays.asList("banana", "banana", "apple" ), comparator);
		SimpleMatchKey key2 = new SimpleMatchKey(
				Arrays.asList("banana", null, "apple" ), comparator);
		
		assertEquals(true, key1.compareTo(key2) > 0);
		assertEquals(true, key2.compareTo(key1) < 0);
		
		assertFalse(key1.equals(key2));
	}
	
	public void testWithDifferentTypes() {
		
		SomeMetaData metaData1 = new SomeMetaData().
				add("fruit", String.class).
				add("quantity", Integer.class).
				add("price", Double.class);
		
		SomeMetaData metaData2 = new SomeMetaData().
				add("fruit", String.class).
				add("quantity", BigInteger.class).
				add("price", double.class);
		
		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData1, metaData2);		
		
		SimpleMatchKey key1 = new SimpleMatchKey(
				Arrays.asList("banana", new Integer(42), new Double(15.3) ), comparator);
		
		SimpleMatchKey key2 = new SimpleMatchKey(
				Arrays.asList("banana", new BigInteger("42"), 15.3 ), comparator);
		
		assertTrue(key1.compareTo(key2) == 0);
		assertTrue(key2.compareTo(key1) == 0);
		
		assertTrue(key1.equals(key2));
	}
}
