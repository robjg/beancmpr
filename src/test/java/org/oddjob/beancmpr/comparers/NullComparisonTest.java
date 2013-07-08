package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

public class NullComparisonTest extends TestCase {

	public void testAll() {
		
		NullComparison<String> test = new NullComparison<String>(null, null);

		assertEquals(0, test.getResult());
		assertEquals("", test.getSummaryText());
		
		test = new NullComparison<String>(null, "Apple");
		
		assertEquals(-1, test.getResult());
		assertEquals("Null Value", test.getSummaryText());
		assertEquals(null, test.getX());
		assertEquals("Apple", test.getY());
	}
}
