package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.SimpleComparer;

public class SimpleComparerTest extends TestCase {

	public void testNotEquals() {
		
		SimpleComparer test = new SimpleComparer();
		
		Comparison<Object> comparison = test.compare("Apples", "Oranges");
		
		assertTrue(comparison.getResult() < 0);
		
		assertEquals("Apples<>Oranges", comparison.getSummaryText());
		
		comparison = test.compare(
				new Object() {
					@Override
					public String toString() {
						return "A";
					}
				}, 
				new Object() {
					@Override
					public String toString() {
						return "B";
					}
				});
				
		assertTrue(comparison.getResult() < 0);
		
		assertEquals("A<>B", comparison.getSummaryText());
		
		comparison = test.compare(new Double(.5), new Double(0.25));
				
		assertTrue(comparison.getResult() > 0);
		
		assertEquals("0.5<>0.25", comparison.getSummaryText());
	}
	
	public void testEquals() {
		
		SimpleComparer test = new SimpleComparer();
		
		Comparison<Object> comparison = test.compare("Apples", "Apples");
		
		assertEquals(0, comparison.getResult());
		
		assertEquals("", comparison.getSummaryText());
	}
}
