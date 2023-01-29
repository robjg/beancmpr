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


	@Test
	void testDifferentOneComponent() {
		
		MatchableMetaData metaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.build();

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

		MatchableMetaData metaData = SimpleMatchableMeta.builder()
				.addKey("fruit1", String.class)
				.addKey("fruit2", String.class)
				.build();

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
		
		MatchableMetaData metaData = SimpleMatchableMeta.builder()
				.addKey("fruit1", String.class)
				.addKey("fruit2", String.class)
				.addKey("fruit3", String.class)
				.build();
		
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
		
		MatchableMetaData metaData = SimpleMatchableMeta.builder()
				.addKey("fruit1", String.class)
				.addKey("fruit2", String.class)
				.addKey("fruit3", String.class)
				.build();
		
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
		
		MatchableMetaData metaData1 = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("quantity", Integer.class)
				.addKey("price", Double.class)
				.build();
		
		MatchableMetaData metaData2 = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("quantity", BigInteger.class)
				.addKey("price", double.class)
				.build();
		
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
