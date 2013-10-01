package org.oddjob.beancmpr;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.beancmpr.results.SimpleArraysResultHandler;

public class BeanCompareJobInCodeTest extends TestCase {

	public static class Fruit {
		
		private String type;
		
		private String colour;
		
		public Fruit(String type, String colour) {
			this.type = type;
			this.colour = colour;
		}
		
		public String getType() {
			return type;
		}
				
		public String getColour() {
			return colour;
		}		
	}
	
	
	public void testWithTwoKeysSorted() {
	
		List<Fruit> x = Arrays.asList(
				new Fruit("apple", "green"),
				new Fruit("banana", "yellow"),
				new Fruit("orange", "orange"), 
				new Fruit("pear", "red") 
				);		
		
		List<Fruit> y = Arrays.asList(
				new Fruit("banana", "brown"),
				new Fruit("banana", "yellow"),
				new Fruit("pear", "green"),
				new Fruit("pear", "red")
				);
		
		SimpleArraysResultHandler results = new SimpleArraysResultHandler();
		
		BeanCompareJob test = new BeanCompareJob();
		test.setArooaSession(new StandardArooaSession());
		test.setResults(results);
		test.setKeyProperties(new String[] { "type", "colour" });
		test.setSorted(true);
		
		test.setInX(x);
		test.setInY(y);
		
		test.run();
		
		SimpleArraysResultHandler.Row result1 = results.getResults().get(0);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result1.getResultType());
		assertEquals("apple", result1.getKeys()[0]);
		assertEquals("green", result1.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result2 = results.getResults().get(1);
		
		assertEquals(MatchResultType.Type.X_MISSING, result2.getResultType());
		assertEquals("banana", result2.getKeys()[0]);
		assertEquals("brown", result2.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result3 = results.getResults().get(2);
		
		assertEquals(MatchResultType.Type.EQUAL, result3.getResultType());
		assertEquals("banana", result3.getKeys()[0]);
		assertEquals("yellow", result3.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result4 = results.getResults().get(3);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result4.getResultType());
		assertEquals("orange", result4.getKeys()[0]);
		assertEquals("orange", result4.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result5 = results.getResults().get(4);
		
		assertEquals(MatchResultType.Type.X_MISSING, result5.getResultType());
		assertEquals("pear", result5.getKeys()[0]);
		assertEquals("green", result5.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result6 = results.getResults().get(5);
		
		assertEquals(MatchResultType.Type.EQUAL, result6.getResultType());
		assertEquals("pear", result6.getKeys()[0]);
		assertEquals("red", result6.getKeys()[1]);
	}
	
}
