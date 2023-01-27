package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.TestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IntegerComparerTest extends TestCase {

	@Test void testIntegersEqual() {
	
		ComparableComparer<Integer> test = new ComparableComparer<>();
		
		Comparison<Integer> comparison = test.compare(42, 42);
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
	
	@Test
	void testXBigger() {
		
		ComparableComparer<Integer> test = new ComparableComparer<>();
		
		Comparison<Integer> comparison = test.compare(42, 24);
		
		assertEquals(1, comparison.getResult());
		assertEquals("42<>24", comparison.getSummaryText());
		assertThat(comparison.getX(), is(42));
		assertThat(comparison.getY(), is(24));
	}
	
	@Test
	void testYBigger() {
		
		ComparableComparer<Integer> test = new ComparableComparer<>();
		
		Comparison<Integer> comparison = test.compare(24, 42);
		
		assertEquals(-1, comparison.getResult());
		assertEquals("24<>42", comparison.getSummaryText());
		assertThat(comparison.getX(), is(24));
		assertThat(comparison.getY(), is(42));
	}

}
