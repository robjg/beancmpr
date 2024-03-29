package org.oddjob.beancmpr.beans;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

class ArrayComparerTest extends TestCase {

	@Test
	void testArrayOfIntsThatMatchButAreReversed() {
		
		int[] x = { 1, 2, 3, 4, 5 };
		int[] y = { 5, 4, 3, 2, 1 };
		
		ArrayComparer test = new ArrayComparer(
				new DefaultComparersByType());
		
		MultiItemComparison<Object> comparison = test.compare(x, y);
		
		assertTrue(comparison.getResult() == 0);
		assertEquals("", comparison.getSummaryText());
		
		assertEquals(5, comparison.getMatchedCount());
		assertEquals(5, comparison.getComparedCount());
		assertEquals(0, comparison.getBreaksCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}
	
}
