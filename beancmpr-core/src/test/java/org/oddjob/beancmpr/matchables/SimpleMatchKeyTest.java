package org.oddjob.beancmpr.matchables;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

class SimpleMatchKeyTest extends TestCase {

	private static class SomeMetaData implements MatchableMetaData {
		
		Map<String, Class<?>> meta =
				new LinkedHashMap<>();
		
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
	
	@Test
	void testDifferentOneComponent() {
		
		SomeMetaData metaData = new SomeMetaData().
				add("fruit", String.class);

		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData, metaData);
		
		SimpleMatchKey key1 = new SimpleMatchKey(
				List.of("banana"), comparator);
		SimpleMatchKey key2 = new SimpleMatchKey(
				List.of("apple"), comparator);
		
		assertTrue(key1.compareTo(key2) > 0);
		assertTrue(key2.compareTo(key1) < 0);
		
		assertThat(key1, not(key2));
	}
	
	@Test
	void testDifferentTwoComponents() {
		
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
		
		assertThat(key1, not(key2));
	}

	@Test void testSameThreeComponents() {
		
		SomeMetaData metaData = new SomeMetaData()
				.add("fruit1", String.class)
				.add("fruit2", String.class)
				.add("fruit3", String.class);
		
		Comparator<Iterable<?>> comparator = new KeyComparatorFactory(
				new ComparersByNameOrType()).createComparatorFor(
						metaData, metaData);
		
		SimpleMatchKey key1 = new SimpleMatchKey(Arrays.asList(
				"orange", "banana", "apple"), comparator);
		SimpleMatchKey key2 = new SimpleMatchKey(Arrays.asList(
				"orange", "banana", "apple"), comparator);

		assertThat(key1.compareTo(key2), is(0));
		assertThat(key2.compareTo(key1), is(0));
		
		assertThat(key1, is(key2));
		assertThat(key1.hashCode(), is(key2.hashCode()));
	}
	
	@Test
	void testDifferentOneComponentNull() {
		
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

		assertThat(key1.compareTo(key2), Matchers.greaterThan(0));
		assertThat(key2.compareTo(key1), Matchers.lessThan(0));
		
		assertThat(key1, not(key2));
	}
	
	@Test
	void testWithDifferentTypes() {
		
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
				Arrays.asList("banana", 42, 15.3), comparator);
		
		SimpleMatchKey key2 = new SimpleMatchKey(
				Arrays.asList("banana", new BigInteger("42"), 15.3 ), comparator);

		assertThat(key1.compareTo(key2), is(0));
		assertThat(key2.compareTo(key1), is(0));

		assertThat(key1, is(key2));
	}
}
