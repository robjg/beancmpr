package org.oddjob.beancmpr.matchables;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparison;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

public class MatchableComparerFactoryTest extends TestCase {

	private class MyMetaData extends MockMatchableMetaData {

		final Map<String, Class<?>> propertiesAndType = 
				new LinkedHashMap<String, Class<?>>();

		public MyMetaData(Iterable<String> props, Iterable<Class<?>> types) {
			ValueIterable<Class<?>> it = new ValueIterable<Class<?>>(props, types); 
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
	
	private class MyMatchable extends MockMatchable {
	
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
		List<Class<?>> classList = new ArrayList<Class<?>>();
		for (Class<?> cl : classes) {
			classList.add(cl);
		}
		return classList;
	}
	
	public void testCompareEqual() {
		
		MatchableComparerFactory test = new MatchableComparerFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "colour"), 
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
				(Iterable<String>) Arrays.asList("fruit", "colour"), 
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
				(Iterable<String>) Arrays.asList("fruit", "quantity"), 
				classList(String.class, BigInteger.class));
		
		MatchableMetaData yMetaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "quantity"), 
				classList(String.class, BigInteger.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", new BigInteger("42")),
				xMetaData);
		
		Matchable y = new MyMatchable(Arrays.asList("apple", new Integer(42)),
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
		
		assertEquals(true, comparisons[1] instanceof NumericComparison);
	}
}
