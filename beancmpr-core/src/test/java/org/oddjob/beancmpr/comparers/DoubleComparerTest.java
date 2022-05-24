package org.oddjob.beancmpr.comparers;

import java.text.DecimalFormatSymbols;

import junit.framework.TestCase;

public class DoubleComparerTest extends TestCase {

	public void testTwoDoubleNotANumber() {
		
		double x = Double.NaN;
		
		double y = Double.NaN;
				
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(1, result.getResult());
		assertEquals("NaN", 
				result.getSummaryText());		
		
		test.setTwoNaNsEqual(true);
		
		result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
		assertEquals("", result.getSummaryText());		
	}
	
	public void testFloatAndDoubleNotANumber() {
		
		double x = Double.NaN;
		
		float y = Float.NaN;
				
		NumericComparer test = new NumericComparer();
		test.setDeltaFormat("0.0");
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(1, result.getResult());
		assertEquals(new DecimalFormatSymbols().getNaN(), 
				result.getSummaryText());		
		
		test.setTwoNaNsEqual(true);
		
		result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
		assertEquals("", result.getSummaryText());		
	}
	
	public void testInfinityTwoDoubles() {
		
		double x = 1.0/0.0;
		
		double y = 1.0/0.0;
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
	}
	
	public void testInfinityOneDoubleOneFloat() {
		
		double x = 1.0/0.0;
		
		float y = 1.0F/0.0F;
				
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
	}
	
	public void testPositiveNegativeInfinity() {
		
		float x = 1.0F/0.0F;
		
		float y = -1.0F/0.0F;
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(1, result.getResult());
		assertEquals("-Infinity", result.getSummaryText());
		assertEquals(true, Double.isInfinite(result.getDelta()));
		assertEquals(true, Double.isNaN(result.getPercentage()));
	}
	
	public void testNegativePositiveInfinity() {
		
		float x = -1.0F/0.0F;
		
		float y = 1.0F/0.0F;
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(-1, result.getResult());
		assertEquals("Infinity", result.getSummaryText());
	}
	
	public void testInfinityWithDeltaFormat() {
		
		float x = -1.0F/0.0F;
		
		float y = 1.0F/0.0F;
		
		NumericComparer test = new NumericComparer();
		test.setDeltaFormat("0.0");
		NumericComparison result = test.compare(x, y);
		
		assertEquals(-1, result.getResult());
		assertEquals(new DecimalFormatSymbols().getInfinity(), 
				result.getSummaryText());
	}
}
