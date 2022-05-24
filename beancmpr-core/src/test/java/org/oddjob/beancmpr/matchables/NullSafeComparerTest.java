package org.oddjob.beancmpr.matchables;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.ArrayComparer;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

public class NullSafeComparerTest extends TestCase {

	public void testCompareTwoNumbers() {
		
		NullSafeComparer<Number> test = new NullSafeComparer<Number>(
				new NumericComparer(), "number");
		
		Comparison<Number> result = test.castAndCompare(2.4, 4.2);
		
		assertEquals(true, result.getResult() < 0);
	}
	
	public void testYIsNull() {
		
		NullSafeComparer<Number> test = new NullSafeComparer<Number>(
				new NumericComparer(), "number");
		
		Comparison<Number> result = test.castAndCompare(2.4, null);
		
		assertEquals(true, result.getResult() > 0);
		assertEquals("Null Value", result.getSummaryText());
	}
	
	public void testBothThingsAreNull() {
		
		NullSafeComparer<Number> test = new NullSafeComparer<Number>(
				new NumericComparer(), "number");
		
		Comparison<Number> result = test.castAndCompare(null, null);
		
		assertEquals(true, result.getResult() == 0);
		assertEquals("", result.getSummaryText());
	}
	
	public void testCompareTwoPrimativeArrays() {
		
		NullSafeComparer<Object> test = new NullSafeComparer<>(
				new ArrayComparer(new DefaultComparersByType()), 
				"somearray");
		
		Comparison<Object> result = test.castAndCompare(
				new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });
		
		assertEquals(0, result.getResult());
		
		assertTrue(result instanceof MultiItemComparison);
		
	}
}
