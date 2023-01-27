package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

class KeyComparatorFactoryTest extends TestCase {

	private static class MyMetaData extends MockMatchableMetaData {

		final Map<String, Class<?>> propertiesAndType =
				new LinkedHashMap<>();

		public MyMetaData(Iterable<String> props, Iterable<Class<?>> types) {
			ValueIterable<Class<?>> it = new ValueIterable<>(props, types);
			for (ValueIterable.Value<Class<?>> v : it) {
				propertiesAndType.put(v.getPropertyName(), v.getValue());
			}
		}
		
		@Override
		public Iterable<String> getKeyProperties() {
			return propertiesAndType.keySet();
		}
		
		@Override
		public Class<?> getPropertyType(String name) {
			return propertiesAndType.get(name);
		}
	}
	
	private static class MyMatchable extends MockMatchable {
	
		final Iterable<?> values;
		
		final MatchableMetaData metaData;
		
		public MyMatchable(Iterable<?> values, MatchableMetaData metaData) {
			this.values = values;
			this.metaData = metaData;
		}
		
		@Override
		public MatchableMetaData getMetaData() {
			return metaData;
		}
		
		@Override
		public Iterable<?> getKeys() {
			return values;
		}
	}
	
	static List<Class<?>> classList(Class<?>... classes) {
		List<Class<?>> classList = new ArrayList<>();
		Collections.addAll(classList, classes);
		return classList;
	}
	
	@Test
	void testCompareEqual() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				Arrays.asList("fruit", "colour"),
				classList(String.class, String.class));
		
		MyMatchable x = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		MyMatchable y = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		
		Comparator<Iterable<?>> comparer = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparer.compare(x.getKeys(), y.getKeys());
		
		assertEquals(0, result);
	}
	
	@Test
	void testCompareNotEqual() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				Arrays.asList("fruit", "colour"),
				classList(String.class, String.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		Matchable y = new MyMatchable(Arrays.asList("apple", "green"),
				metaData);
				
		Comparator<Iterable<?>> comparator = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertTrue(result > 0);
	}
	
	@Test
	void testEqualDifferentTypes() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData xMetaData = new MyMetaData(
				Arrays.asList("fruit", "quantity"),
				classList(String.class, BigInteger.class));
		
		MatchableMetaData yMetaData = new MyMetaData(
				Arrays.asList("fruit", "quantity"),
				classList(String.class, Integer.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", new BigInteger("42")),
				xMetaData);
		
		Matchable y = new MyMatchable(Arrays.asList("apple", 42),
				yMetaData);
				
		Comparator<Iterable<?>> comparator = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertEquals(0, result);
		
	}
	
	@Test
	void testCompareOneSideNullComponent() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData xMetaData = new MyMetaData(
				Arrays.asList("fruit", "quantity"),
				classList(String.class, BigInteger.class));
		
		MatchableMetaData yMetaData = new MyMetaData(
				Arrays.asList("fruit", "quantity"),
				classList(String.class, BigInteger.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", new BigInteger("42")),
				xMetaData);
		
		Matchable y = new MyMatchable(Arrays.asList("apple", (BigInteger) null),
				yMetaData);
				
		Comparator<Iterable<?>> comparator = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertThat(result, greaterThan(0));
		
		result = comparator.compare(y.getKeys(), x.getKeys());

		assertThat(result, lessThan(0));
	}
}
