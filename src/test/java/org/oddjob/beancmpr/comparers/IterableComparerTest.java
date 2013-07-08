package org.oddjob.beancmpr.comparers;

import java.util.Arrays;

import junit.framework.TestCase;

public class IterableComparerTest extends TestCase {

	public void testCompareEqual() {
		
		IterableComparer<String> test = 
				new IterableComparer<String>();
		
		Iterable<String> x = Arrays.asList("a", "b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c", "a");
		
		IterableComparison<String> comparison = test.compare(x, y);
		
		assertEquals(0, comparison.getResult());
		assertEquals(3, comparison.getMatchedCount());
		assertEquals(0, comparison.getDifferentCount());		
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}
	
	public void testCompareOneDifferentEqual() {
		
		IterableComparer<String> test = 
				new IterableComparer<String>();
		
		Iterable<String> x = Arrays.asList("a", "b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c", "d");
		
		IterableComparison<String> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getMatchedCount());
		assertEquals(1, comparison.getDifferentCount());		
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}

	public void testCompareOneXMissing() {
		
		IterableComparer<String> test = 
				new IterableComparer<String>();
		
		Iterable<String> x = Arrays.asList("b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c", "a");
		
		IterableComparison<String> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getMatchedCount());
		assertEquals(0, comparison.getDifferentCount());		
		assertEquals(1, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}

	public void testCompareOneYMissing() {
		
		IterableComparer<String> test = 
				new IterableComparer<String>();
		
		Iterable<String> x = Arrays.asList("a", "b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c");
		
		IterableComparison<String> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getMatchedCount());
		assertEquals(0, comparison.getDifferentCount());		
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(1, comparison.getYMissingCount());
	}

	/**
	 * Check comparer for Number can accept list of Integers.
	 */
	public void testGenericSanityCheck() {
		
		IterableComparer<Number> test = 
				new IterableComparer<Number>();
		
		Iterable<Integer> x = Arrays.asList(4, 1 , 9);
		
		Iterable<Integer> y = Arrays.asList(1 , 9, 5);
		
		IterableComparison<Number> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getMatchedCount());
		assertEquals(1, comparison.getDifferentCount());		
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}
}
