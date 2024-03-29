package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;

class NullComparisonTest extends TestCase {

	@Test
	void testBothNull() {
		
		NullComparison<String> test = new NullComparison<>(null, null);

		assertEquals(0, test.getResult());
		assertEquals("", test.getSummaryText());
		
	}

	@Test
	void testXNull() {
		
		NullComparison<String> test = new NullComparison<>(
				null, "Apple");
		
		assertEquals(-1, test.getResult());
		assertEquals("Null Value", test.getSummaryText());
		assertEquals(null, test.getX());
		assertEquals("Apple", test.getY());
	}

	@Test
	void testYNull() {
		
		NullComparison<String> test = new NullComparison<>(
				"Apple", null);
		
		assertEquals(1, test.getResult());
		assertEquals("Null Value", test.getSummaryText());
		assertEquals("Apple", test.getX());
		assertEquals(null, test.getY());
	}
}
