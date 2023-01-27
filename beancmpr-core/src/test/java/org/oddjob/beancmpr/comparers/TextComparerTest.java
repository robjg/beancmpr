package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.TestCase;

class TextComparerTest extends TestCase {

	@Test
	void testCaseInsensitiveEquals() {
		
		TextComparer test = new TextComparer();
		test.setIgnoreCase(true);
		
		Comparison<String> comparison = test.compare("APPLE", "apple");
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
		assertEquals("APPLE", comparison.getX());
		assertEquals("apple", comparison.getY());		
	}
	
	@Test
	void testAppleLessThanBanana() {
		
		TextComparer test = new TextComparer();
		test.setIgnoreCase(true);
		
		Comparison<String> comparison = test.compare("apple", "BANANA");
		
		assertEquals(true, comparison.getResult() < 0);
		assertEquals("apple<>BANANA", comparison.getSummaryText());
	}
	
	@Test
	void testCaseCompare() {
		
		TextComparer test = new TextComparer();
		
		Comparison<String> comparison = test.compare("apple", "APPLE");
		
		assertEquals(true, comparison.getResult() > 0);
		assertEquals("apple<>APPLE", comparison.getSummaryText());
	}
	
	@Test
	void testIgnoreWhilespace() {
		
		TextComparer test = new TextComparer();
		test.setIgnoreWhitespace(true);
		
		Comparison<String> comparison = test.compare(
				"some  apples\t\tare\ngreen", 
				"\nsome\tapples are      green\n");
		
		assertEquals(0, comparison.getResult());
		assertEquals("", comparison.getSummaryText());
	}
}
