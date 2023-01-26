package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;
import org.oddjob.beancmpr.Comparison;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IntegerComparerTest extends TestCase {

	public void testIntegersEqual() {
	
		ComparableComparer<Integer> test = new ComparableComparer<>();
		
		Comparison<Integer> comparison = test.compare(42, 42);
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
	
	public void testXBigger() {
		
		ComparableComparer<Integer> test = new ComparableComparer<>();
		
		Comparison<Integer> comparison = test.compare(42, 24);
		
		assertEquals(1, comparison.getResult());
		assertEquals("42<>24", comparison.getSummaryText());
		assertThat(comparison.getX(), is(42));
		assertThat(comparison.getY(), is(24));
	}
	
	public void testYBigger() {
		
		ComparableComparer<Integer> test = new ComparableComparer<>();
		
		Comparison<Integer> comparison = test.compare(24, 42);
		
		assertEquals(-1, comparison.getResult());
		assertEquals("24<>42", comparison.getSummaryText());
		assertThat(comparison.getX(), is(24));
		assertThat(comparison.getY(), is(42));
	}

}
