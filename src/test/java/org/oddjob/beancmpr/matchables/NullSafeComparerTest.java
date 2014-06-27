package org.oddjob.beancmpr.matchables;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparer;

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
}
