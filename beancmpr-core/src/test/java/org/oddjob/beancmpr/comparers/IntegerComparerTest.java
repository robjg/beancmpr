package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparison;

public class IntegerComparerTest extends TestCase {

	public void testIntegersEqual() {
	
		ComparableComparer<Integer> test = new ComparableComparer<Integer>();
		
		Comparison<Integer> comparison = test.compare(42, 42);
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
	
	public void testXBigger() {
		
		ComparableComparer<Integer> test = new ComparableComparer<Integer>();
		
		Comparison<Integer> comparison = test.compare(42, 24);
		
		assertEquals(1, comparison.getResult());
		assertEquals("42<>24", comparison.getSummaryText());
		assertEquals(new Integer(42), comparison.getX());
		assertEquals(new Integer(24), comparison.getY());
	}
	
	public void testYBigger() {
		
		ComparableComparer<Integer> test = new ComparableComparer<Integer>();
		
		Comparison<Integer> comparison = test.compare(24, 42);
		
		assertEquals(-1, comparison.getResult());
		assertEquals("24<>42", comparison.getSummaryText());
		assertEquals(new Integer(24), comparison.getX());
		assertEquals(new Integer(42), comparison.getY());
	}

}
