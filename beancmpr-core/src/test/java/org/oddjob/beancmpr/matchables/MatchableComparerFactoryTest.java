package org.oddjob.beancmpr.matchables;

import junit.framework.TestCase;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparison;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class MatchableComparerFactoryTest extends TestCase {

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
		public Iterable<String> getValueProperties() {
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
		public Iterable<?> getValues() {
			return values;
		}
	}
	
	static List<Class<?>> classList(Class<?>... classes) {
		List<Class<?>> classList = new ArrayList<>();
		Collections.addAll(classList, classes);
		return classList;
	}
	
	public void testCompareEqual() {
		
		MatchableComparerFactory test = new MatchableComparerFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				Arrays.asList("fruit", "colour"),
				classList(String.class, String.class));
		
		MyMatchable x = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		MyMatchable y = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		
		MatchableComparer comparer = test.createComparerFor(
				metaData, metaData);
		
		MultiValueComparison<Matchable> result = comparer.compare(x, y);
		
		assertEquals(0, result.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				result.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
	}
	
	public void testCompareNotEqual() {
		
		MatchableComparerFactory test = new MatchableComparerFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				Arrays.asList("fruit", "colour"),
				classList(String.class, String.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		Matchable y = new MyMatchable(Arrays.asList("apple", "green"),
				metaData);
				
		MatchableComparer comparer = test.createComparerFor(
				metaData, metaData);
		
		MultiValueComparison<Matchable> result = comparer.compare(x, y);
		
		assertTrue(result.getResult() > 0);
		
		Comparison<?>[] comparisons = Iterables.toArray(result.getValueComparisons(), 
				Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertTrue(comparisons[1].getResult() > 0);
	}
	
	public void testEqualDifferentTypes() {
		
		MatchableComparerFactory test = new MatchableComparerFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData xMetaData = new MyMetaData(
				Arrays.asList("fruit", "quantity"),
				classList(String.class, BigInteger.class));
		
		MatchableMetaData yMetaData = new MyMetaData(
				Arrays.asList("fruit", "quantity"),
				classList(String.class, BigInteger.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", new BigInteger("42")),
				xMetaData);
		
		Matchable y = new MyMatchable(Arrays.asList("apple", 42),
				yMetaData);
				
		MatchableComparer comparer = test.createComparerFor(
				xMetaData, yMetaData);
		
		MultiValueComparison<Matchable> result = comparer.compare(x, y);
		
		assertEquals(0, result.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				result.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
		
		assertThat(comparisons[1], instanceOf(NumericComparison.class));
	}
}
