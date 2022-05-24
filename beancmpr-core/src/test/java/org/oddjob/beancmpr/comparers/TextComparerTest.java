package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;

import junit.framework.TestCase;

public class TextComparerTest extends TestCase {

	public void testCaseInsensitiveEquals() {
		
		TextComparer test = new TextComparer();
		test.setIgnoreCase(true);
		
		Comparison<String> comparison = test.compare("APPLE", "apple");
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
		assertEquals("APPLE", comparison.getX());
		assertEquals("apple", comparison.getY());		
	}
	
	public void testAppleLessThanBanana() {
		
		TextComparer test = new TextComparer();
		test.setIgnoreCase(true);
		
		Comparison<String> comparison = test.compare("apple", "BANANA");
		
		assertEquals(true, comparison.getResult() < 0);
		assertEquals("apple<>BANANA", comparison.getSummaryText());
	}
	
	public void testCaseCompare() {
		
		TextComparer test = new TextComparer();
		
		Comparison<String> comparison = test.compare("apple", "APPLE");
		
		assertEquals(true, comparison.getResult() > 0);
		assertEquals("apple<>APPLE", comparison.getSummaryText());
	}
	
	public void testIgnoreWhilespace() {
		
		TextComparer test = new TextComparer();
		test.setIgnoreWhitespace(true);
		
		Comparison<String> comparison = test.compare(
				"some  apples\t\tare\ngreen", 
				"\nsome\tapples are      green\n");
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
}
