package org.oddjob.beancmpr.matchables;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.oddjob.beancmpr.composite.ComparersByNameOrType;

public class KeyComparatorFactoryTest extends TestCase {

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
		public Iterable<String> getKeyProperties() {
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
		public Iterable<?> getKeys() {
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
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "colour"), 
				classList(String.class, String.class));
		
		MyMatchable x = new MyMatchable(Arrays.asList("apple", "red"), 
				metaData);
		MyMatchable y = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		
		Comparator<Iterable<?>> comparer = test.createComparerFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparer.compare(x.getKeys(), y.getKeys());
		
		assertEquals(0, result);
	}
	
	public void testCompareNotEqual() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "colour"), 
				classList(String.class, String.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", "red"),
				metaData);
		Matchable y = new MyMatchable(Arrays.asList("apple", "green"),
				metaData);
				
		Comparator<Iterable<?>> comparator = test.createComparerFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertTrue(result > 0);
	}
	
	public void testEqualDifferentTypes() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData xMetaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "quantity"), 
				classList(String.class, BigInteger.class));
		
		MatchableMetaData yMetaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "quantity"), 
				classList(String.class, Integer.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", new BigInteger("42")),
				xMetaData);
		
		Matchable y = new MyMatchable(Arrays.asList("apple", new Integer(42)),
				yMetaData);
				
		Comparator<Iterable<?>> comparator = test.createComparerFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertEquals(0, result);
		
	}
	
	public void testCompareOneSideNullComponent() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData xMetaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "quantity"), 
				classList(String.class, BigInteger.class));
		
		MatchableMetaData yMetaData = new MyMetaData(
				(Iterable<String>) Arrays.asList("fruit", "quantity"), 
				classList(String.class, BigInteger.class));
		
		Matchable x = new MyMatchable(Arrays.asList("apple", new BigInteger("42")),
				xMetaData);
		
		Matchable y = new MyMatchable(Arrays.asList("apple", (BigInteger) null),
				yMetaData);
				
		Comparator<Iterable<?>> comparator = test.createComparerFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertEquals(true, result > 0);
		
		result = comparator.compare(y.getKeys(), x.getKeys());
		
		assertEquals(true, result < 0);
	}
}
