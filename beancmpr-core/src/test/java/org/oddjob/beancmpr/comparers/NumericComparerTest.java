package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

public class NumericComparerTest extends TestCase {

	
	public void testNoTolerances() {
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison comparison = test.compare(2.0, 2.0);
		
		assertEquals(0, comparison.getResult());
		
		comparison = test.compare(200.0, 190);
		
		assertTrue(comparison.getResult() > 0);
		assertEquals(-10, comparison.getDelta(), 0.001);
		assertEquals(-5, comparison.getPercentage(), 0.001);
		assertEquals("-10.0 (-5.0%)", comparison.getSummaryText());
		
		comparison = test.compare(200.0, 250);
		
		assertTrue(comparison.getResult() < 0);
		assertEquals(50, comparison.getDelta(), 0.001);
		assertEquals(25, comparison.getPercentage(), 0.001);
		assertEquals("50.0 (25.0%)", comparison.getSummaryText());

	}

	public void testOutsideNumericTolerance() {
		
		NumericComparer test = new NumericComparer();
		test.setDeltaTolerance(0.1);
		
		NumericComparison comparison = test.compare(2.0, 2.11);
		
		assertTrue(comparison.getResult() < 0);
		assertEquals(0.11, comparison.getDelta(), 0.0001);
		assertEquals(5.5, comparison.getPercentage(), 0.0001);
		
		test.setDeltaTolerance(2);
		
		comparison = test.compare(200.0, 190.0);
		
		assertTrue(comparison.getResult() > 0);		
		assertEquals(-10.0, comparison.getDelta(), 0.0001);
		assertEquals(-5.0, comparison.getPercentage(), 0.0001);
	}
	
	
	public void testOutsidePercentageTolerence() {
		
		NumericComparer test = new NumericComparer();
		test.setPercentageTolerance(5);
		test.setPercentageFormat("###");
		test.setDeltaFormat("##");
		
		NumericComparison comparison;
		
		comparison = test.compare(200, 190);
		
		assertTrue(comparison.getResult() > 0);
		assertEquals(-10.0, comparison.getDelta(), 0.0001);
		assertEquals(-5.0, comparison.getPercentage(), 0.0001);
		assertEquals("-10 (-5%)", comparison.getSummaryText());
		
		test.setDeltaTolerance(20);
		
		comparison = test.compare(200, 190);
		
		assertEquals(0, comparison.getResult());
	}
	
	public void testIntegerDoubleComparison() {
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison comparison = test.compare(2, 2.0);
		
		assertEquals(0, comparison.getResult());
		
		test.setDeltaTolerance(0.05);
		
		comparison = test.compare(2, 2.01);
		
		assertEquals(0, comparison.getResult());
	}
	
	public void testNullValuesComparison() {
		
		NumericComparer test = new NumericComparer();

		try {
			test.compare(null, null);
		}
		catch (NullPointerException e) {
			// expected
		}		
	}
	
	public void testZerosWithPercentageTolerance() {
		
		NumericComparer test = new NumericComparer();
		test.setPercentageTolerance(1.0);
		
		NumericComparison comparison = test.compare(0.0, 1000.0);
		
		assertEquals(-1, comparison.getResult());
		assertEquals("1000.0", comparison.getSummaryText());
		assertEquals(true, Double.isInfinite(comparison.getPercentage()));
		
		comparison = test.compare(1000.0, 0.0);
		
		assertEquals(1, comparison.getResult());
		assertEquals("-1000.0 (-100.0%)", comparison.getSummaryText());
		
		comparison = test.compare(0.0, 0.0);
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
	
}
