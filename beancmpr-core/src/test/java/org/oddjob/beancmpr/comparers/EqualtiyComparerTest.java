package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;
import org.oddjob.beancmpr.Comparison;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class EqualtiyComparerTest extends TestCase {

	public void testNotEquals() {
		
		EqualityComparer test = new EqualityComparer();
		
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

		assertThat(comparison.getResult(), lessThan(0));

		assertEquals("A<>B", comparison.getSummaryText());
		
		comparison = test.compare(.5, 0.25);
				
		assertThat(comparison.getResult(), greaterThan(0));

		assertEquals("0.5<>0.25", comparison.getSummaryText());
	}
	
	public void testEquals() {
		
		EqualityComparer test = new EqualityComparer();
		
		Comparison<Object> comparison = test.compare("Apples", "Apples");
		
		assertEquals(0, comparison.getResult());
		
		assertEquals("", comparison.getSummaryText());
	}
}
