package org.oddjob.beancmpr.matchables;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparer;

public class InferredTypeCompareTest extends TestCase {

	public void testCompareTwoNumbers() {
		
		InferredTypeCompare<Number> test = new InferredTypeCompare<Number>(
				new NumericComparer(), "number");
		
		Comparison<Number> result = test.castAndCompare(2.4, 4.2);
		
		assertEquals(true, result.getResult() < 0);
	}
	
	public void testYIsNull() {
		
		InferredTypeCompare<Number> test = new InferredTypeCompare<Number>(
				new NumericComparer(), "number");
		
		Comparison<Number> result = test.castAndCompare(2.4, null);
		
		assertEquals(true, result.getResult() > 0);
	}
}
