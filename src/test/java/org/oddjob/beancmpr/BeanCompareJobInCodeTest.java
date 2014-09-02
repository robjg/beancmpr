package org.oddjob.beancmpr;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;
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
	
	
	public void testWithTwoKeysSortedAndBigGapInX() {
	
		List<Fruit> x = Arrays.asList(
				new Fruit("apple", "green"),
				new Fruit("orange", "orange"), 
				new Fruit("pear", "red") 
				);		
		
		List<Fruit> y = Arrays.asList(
				new Fruit("banana", "brown"),
				new Fruit("banana", "green"),
				new Fruit("banana", "yellow"),
				new Fruit("pear", "green"),
				new Fruit("pear", "red")
				);
		
		SimpleArraysResultHandler results = new SimpleArraysResultHandler();
		
		IterableBeansComparerType<Fruit> iterableComparer = 
				new IterableBeansComparerType<>();
		iterableComparer.setArooaSession(new StandardArooaSession());
		iterableComparer.setKeys(new String[] { "type", "colour" });
		iterableComparer.setSorted(true);
				
		BeanCompareJob<Iterable<Fruit>> test = new BeanCompareJob<>();
		test.setResults(results);
		test.setComparer(iterableComparer);
		
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
		
		assertEquals(MatchResultType.Type.X_MISSING, result3.getResultType());
		assertEquals("banana", result3.getKeys()[0]);
		assertEquals("green", result3.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result4 = results.getResults().get(3);
		
		assertEquals(MatchResultType.Type.X_MISSING, result4.getResultType());
		assertEquals("banana", result4.getKeys()[0]);
		assertEquals("yellow", result4.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result5 = results.getResults().get(4);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result5.getResultType());
		assertEquals("orange", result5.getKeys()[0]);
		assertEquals("orange", result5.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result6 = results.getResults().get(5);
		
		assertEquals(MatchResultType.Type.X_MISSING, result6.getResultType());
		assertEquals("pear", result6.getKeys()[0]);
		assertEquals("green", result6.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result7 = results.getResults().get(6);
		
		assertEquals(MatchResultType.Type.EQUAL, result7.getResultType());
		assertEquals("pear", result7.getKeys()[0]);
		assertEquals("red", result7.getKeys()[1]);
	}
	
	public void testWithTwoKeysSortedAndBigGapInY() {
		
		List<Fruit> x = Arrays.asList(
				new Fruit("banana", "brown"),
				new Fruit("banana", "green"),
				new Fruit("banana", "yellow"),
				new Fruit("pear", "green"),
				new Fruit("pear", "red")
				);
		
		List<Fruit> y = Arrays.asList(
				new Fruit("apple", "green"),
				new Fruit("orange", "orange"), 
				new Fruit("pear", "red") 
				);		
		
		SimpleArraysResultHandler results = new SimpleArraysResultHandler();
		
		IterableBeansComparerType<Fruit> iterableComparer = 
				new IterableBeansComparerType<>();
		iterableComparer.setArooaSession(new StandardArooaSession());
		iterableComparer.setKeys(new String[] { "type", "colour" });
		iterableComparer.setSorted(true);

		BeanCompareJob<Iterable<Fruit>> test = new BeanCompareJob<>();
		test.setResults(results);
		test.setComparer(iterableComparer);
		
		test.setInX(x);
		test.setInY(y);
		
		test.run();
		
		SimpleArraysResultHandler.Row result1 = results.getResults().get(0);
		
		assertEquals(MatchResultType.Type.X_MISSING, result1.getResultType());
		assertEquals("apple", result1.getKeys()[0]);
		assertEquals("green", result1.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result2 = results.getResults().get(1);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result2.getResultType());
		assertEquals("banana", result2.getKeys()[0]);
		assertEquals("brown", result2.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result3 = results.getResults().get(2);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result3.getResultType());
		assertEquals("banana", result3.getKeys()[0]);
		assertEquals("green", result3.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result4 = results.getResults().get(3);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result4.getResultType());
		assertEquals("banana", result4.getKeys()[0]);
		assertEquals("yellow", result4.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result5 = results.getResults().get(4);
		
		assertEquals(MatchResultType.Type.X_MISSING, result5.getResultType());
		assertEquals("orange", result5.getKeys()[0]);
		assertEquals("orange", result5.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result6 = results.getResults().get(5);
		
		assertEquals(MatchResultType.Type.Y_MISSING, result6.getResultType());
		assertEquals("pear", result6.getKeys()[0]);
		assertEquals("green", result6.getKeys()[1]);
		
		SimpleArraysResultHandler.Row result7 = results.getResults().get(6);
		
		assertEquals(MatchResultType.Type.EQUAL, result7.getResultType());
		assertEquals("pear", result7.getKeys()[0]);
		assertEquals("red", result7.getKeys()[1]);
	}
	
}
