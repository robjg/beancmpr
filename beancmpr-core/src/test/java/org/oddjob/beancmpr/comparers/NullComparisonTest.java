package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

public class NullComparisonTest extends TestCase {

	public void testBothNull() {
		
		NullComparison<String> test = new NullComparison<String>(null, null);

		assertEquals(0, test.getResult());
		assertEquals("", test.getSummaryText());
		
	}
	
	public void testXNull() {
		
		NullComparison<String> test = new NullComparison<String>(
				null, "Apple");
		
		assertEquals(-1, test.getResult());
		assertEquals("Null Value", test.getSummaryText());
		assertEquals(null, test.getX());
		assertEquals("Apple", test.getY());
	}
	
	public void testYNull() {
		
		NullComparison<String> test = new NullComparison<String>(
				"Apple", null);
		
		assertEquals(1, test.getResult());
		assertEquals("Null Value", test.getSummaryText());
		assertEquals("Apple", test.getX());
		assertEquals(null, test.getY());
	}
}
