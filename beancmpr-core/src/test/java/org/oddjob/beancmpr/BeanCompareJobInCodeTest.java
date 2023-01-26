package org.oddjob.beancmpr;

import junit.framework.TestCase;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.beancmpr.results.MatchResultHandlerFactory;

import java.util.Arrays;
import java.util.List;

public class BeanCompareJobInCodeTest extends TestCase {

	public static class Fruit {
		
		private final String type;
		
		private final String colour;
		
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
		
		MatchResultHandlerFactory results = new MatchResultHandlerFactory();
		
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
		
		MatchResult result1 = results.getResults().get(0);
		
		assertEquals(MatchResultType.Y_MISSING, result1.getResultType());
		assertEquals("apple", result1.getKeys()[0]);
		assertEquals("green", result1.getKeys()[1]);
		
		MatchResult result2 = results.getResults().get(1);
		
		assertEquals(MatchResultType.X_MISSING, result2.getResultType());
		assertEquals("banana", result2.getKeys()[0]);
		assertEquals("brown", result2.getKeys()[1]);
		
		MatchResult result3 = results.getResults().get(2);
		
		assertEquals(MatchResultType.X_MISSING, result3.getResultType());
		assertEquals("banana", result3.getKeys()[0]);
		assertEquals("green", result3.getKeys()[1]);
		
		MatchResult result4 = results.getResults().get(3);
		
		assertEquals(MatchResultType.X_MISSING, result4.getResultType());
		assertEquals("banana", result4.getKeys()[0]);
		assertEquals("yellow", result4.getKeys()[1]);
		
		MatchResult result5 = results.getResults().get(4);
		
		assertEquals(MatchResultType.Y_MISSING, result5.getResultType());
		assertEquals("orange", result5.getKeys()[0]);
		assertEquals("orange", result5.getKeys()[1]);
		
		MatchResult result6 = results.getResults().get(5);
		
		assertEquals(MatchResultType.X_MISSING, result6.getResultType());
		assertEquals("pear", result6.getKeys()[0]);
		assertEquals("green", result6.getKeys()[1]);
		
		MatchResult result7 = results.getResults().get(6);
		
		assertEquals(MatchResultType.EQUAL, result7.getResultType());
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
		
		MatchResultHandlerFactory results = new MatchResultHandlerFactory();
		
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
		
		MatchResult result1 = results.getResults().get(0);
		
		assertEquals(MatchResultType.X_MISSING, result1.getResultType());
		assertEquals("apple", result1.getKeys()[0]);
		assertEquals("green", result1.getKeys()[1]);
		
		MatchResult result2 = results.getResults().get(1);
		
		assertEquals(MatchResultType.Y_MISSING, result2.getResultType());
		assertEquals("banana", result2.getKeys()[0]);
		assertEquals("brown", result2.getKeys()[1]);
		
		MatchResult result3 = results.getResults().get(2);
		
		assertEquals(MatchResultType.Y_MISSING, result3.getResultType());
		assertEquals("banana", result3.getKeys()[0]);
		assertEquals("green", result3.getKeys()[1]);
		
		MatchResult result4 = results.getResults().get(3);
		
		assertEquals(MatchResultType.Y_MISSING, result4.getResultType());
		assertEquals("banana", result4.getKeys()[0]);
		assertEquals("yellow", result4.getKeys()[1]);
		
		MatchResult result5 = results.getResults().get(4);
		
		assertEquals(MatchResultType.X_MISSING, result5.getResultType());
		assertEquals("orange", result5.getKeys()[0]);
		assertEquals("orange", result5.getKeys()[1]);
		
		MatchResult result6 = results.getResults().get(5);
		
		assertEquals(MatchResultType.Y_MISSING, result6.getResultType());
		assertEquals("pear", result6.getKeys()[0]);
		assertEquals("green", result6.getKeys()[1]);
		
		MatchResult result7 = results.getResults().get(6);
		
		assertEquals(MatchResultType.EQUAL, result7.getResultType());
		assertEquals("pear", result7.getKeys()[0]);
		assertEquals("red", result7.getKeys()[1]);
	}
	
}
