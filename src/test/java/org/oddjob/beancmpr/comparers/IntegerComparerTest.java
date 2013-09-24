package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;

import junit.framework.TestCase;

public class IntegerComparerTest extends TestCase {

	public void testIntegersEqual() {
	
		IntegerComparer test = new IntegerComparer();
		
		Comparison<Integer> comparison = test.compare(42, 42);
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
	
	public void testXBigger() {
		
		IntegerComparer test = new IntegerComparer();
		
		Comparison<Integer> comparison = test.compare(42, 24);
		
		assertEquals(1, comparison.getResult());
		assertEquals("42<>24", comparison.getSummaryText());
		assertEquals(new Integer(42), comparison.getX());
		assertEquals(new Integer(24), comparison.getY());
	}
	
	public void testYBigger() {
		
		IntegerComparer test = new IntegerComparer();
		
		Comparison<Integer> comparison = test.compare(24, 42);
		
		assertEquals(-1, comparison.getResult());
		assertEquals("24<>42", comparison.getSummaryText());
		assertEquals(new Integer(24), comparison.getX());
		assertEquals(new Integer(42), comparison.getY());
	}

}
