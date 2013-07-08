package org.oddjob.beancmpr.matchables;

import java.util.Arrays;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.Iterables;
import org.oddjob.beancmpr.beans.ComparersByPropertyOrType;
import org.oddjob.beancmpr.matchables.DefaultMatchableComparer;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

public class DefaultMatchableComparerTest extends TestCase {

	private class MyMatchable extends MockMatchable {
	
		final Iterable<?> values;
		
		public MyMatchable(Object... values) {
			this.values = Arrays.asList(values);
		}
		
		@Override
		public MatchableMetaData getMetaData() {
			return new MockMatchableMetaData() {
				@Override
				public Iterable<String> getValueProperties() {
					return Arrays.asList("fruit", "colour");
				}
				
				@Override
				public Class<?> getPropertyType(String name) {
					return String.class;
				}
			};
		}
		
		@Override
		public Iterable<?> getValues() {
			return values;
		}
	}
	
	
	public void testCompareEqual() {
		
		DefaultMatchableComparer test = new DefaultMatchableComparer(
				new ComparersByPropertyOrType());

		MyMatchable x = new MyMatchable("apple", "red");
		MyMatchable y = new MyMatchable("apple", "red");
		
		MultiValueComparison<Matchable> result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				result.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
	}
	
	public void testCompareNotEqual() {
		
		DefaultMatchableComparer test = new DefaultMatchableComparer(
				new ComparersByPropertyOrType(
						null, null));

		MyMatchable x = new MyMatchable("apple", "red");
		MyMatchable y = new MyMatchable("apple", "green");
		
		MultiValueComparison<Matchable> result = test.compare(x, y);
		
		assertTrue(result.getResult() > 0);
		
		Comparison<?>[] comparisons = Iterables.toArray(result.getValueComparisons(), 
				Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertTrue(comparisons[1].getResult() > 0);
	}
}
