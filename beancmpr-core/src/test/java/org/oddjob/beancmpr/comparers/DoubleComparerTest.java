package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;

import java.text.DecimalFormatSymbols;


class DoubleComparerTest extends TestCase {

	@Test
	void testTwoDoubleNotANumber() {
		
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

	@Test
	void testFloatAndDoubleNotANumber() {
		
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

	@Test
	void testInfinityTwoDoubles() {
		
		double x = 1.0/0.0;
		
		double y = 1.0/0.0;
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
	}

	@Test
	void testInfinityOneDoubleOneFloat() {
		
		double x = 1.0/0.0;
		
		float y = 1.0F/0.0F;
				
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
	}

	@Test
	void testPositiveNegativeInfinity() {
		
		float x = 1.0F/0.0F;
		
		float y = -1.0F/0.0F;
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(1, result.getResult());
		assertEquals("-Infinity", result.getSummaryText());
		assertEquals(true, Double.isInfinite(result.getDelta()));
		assertEquals(true, Double.isNaN(result.getPercentage()));
	}

	@Test
	void testNegativePositiveInfinity() {
		
		float x = -1.0F/0.0F;
		
		float y = 1.0F/0.0F;
		
		NumericComparer test = new NumericComparer();
		
		NumericComparison result = test.compare(x, y);
		
		assertEquals(-1, result.getResult());
		assertEquals("Infinity", result.getSummaryText());
	}

	@Test
	void testInfinityWithDeltaFormat() {
		
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
