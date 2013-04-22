package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.Iterables;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.beans.ComparersByPropertyOrType;
import org.oddjob.beancmpr.comparers.DefaultComparersByType;
import org.oddjob.beancmpr.comparers.IterableComparison;

public class OrderedMatchablesComparerTest extends TestCase {
	
	private static final Logger logger = 
			Logger.getLogger(OrderedMatchablesComparerTest.class);
	
	public static class Fruit {
		
		private String type;
		
		private int quantity;
				
		public Fruit(String type, int qty) {
			this.type = type;
			this.quantity = qty;
		}
		
		public String getType() {
			return type;
		}
		
		public int getQuantity() {
			return quantity;
		}
	}
	
	private class Results implements BeanCmprResultsHandler {
		
		List<Object[]> xsMissing = 
			new ArrayList<Object[]>();
		
		List<Object[]> ysMissing = 
			new ArrayList<Object[]>();
		
		List<Object[]> comparedKeys = new ArrayList<Object[]>();
		
		List<MultiValueComparison<Matchable>> comparisons = 
			new ArrayList<MultiValueComparison<Matchable>>();
		
		@Override
		public void compared(MultiValueComparison<Matchable> comparison) {

			Matchable x = comparison.getX();
			Matchable y = comparison.getY();
			
			assertEquals(x.getKey(), y.getKey());
			
			Object[] keys = Iterables.toArray(x.getKeys(), Object.class);
			
			comparedKeys.add(keys);
					
			comparisons.add(comparison);
		}
		
		@Override
		public void xMissing(MatchableGroup y) {
			
			Object[] keys = Iterables.toArray(y.getKey().getKeys(), 
					Object.class);
			
			xsMissing.add(keys);
		}
		
		@Override
		public void yMissing(MatchableGroup x) {
			
			Object[] keys = Iterables.toArray(x.getKey().getKeys(), 
					Object.class);
			
			ysMissing.add(keys);
		}
	}
	
	public void testNoKeysMatch() {

		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "type" },
				new String[] { "quantity" },
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);
		
		List<Fruit> fruitX = Arrays.asList(
				new Fruit("orange", 2),
				new Fruit("apple", 2));
		
		UnsortedBeanMatchables<Object> xs = 
			new UnsortedBeanMatchables<Object>(fruitX, factory);
		
		List<Fruit> fruitY = Arrays.asList(
				new Fruit("banana", 3),
				new Fruit("kiwi", 3));
		
		UnsortedBeanMatchables<Object> ys = 
			new UnsortedBeanMatchables<Object>(fruitY, factory);

		Results results = new Results();
		
		OrderedMatchablesComparer test = new OrderedMatchablesComparer(
				accessor,
				new ComparersByPropertyOrType(
						null, new DefaultComparersByType()),
				results);
		
		test.compare(xs, ys);
		
		assertEquals(2, results.xsMissing.size());
		
		assertEquals("banana", 
				results.xsMissing.get(0)[0]);
		assertEquals("kiwi", 
				results.xsMissing.get(1)[0]);
		
		assertEquals(2, results.ysMissing.size());
		
		assertEquals("apple", 
				results.ysMissing.get(0)[0]);
		assertEquals("orange",
				results.ysMissing.get(1)[0]);
		
		assertEquals(0, results.comparedKeys.size());
	}	
	
	public void testKeysMatchOneValueDoesnt() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "type" },
				new String[] { "quantity" },
				null
				);
				
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);
		
		List<Fruit> fruitX = Arrays.asList(
				new Fruit("orange", 3),
				new Fruit("pear", 4),
				new Fruit("apple", 6));
		
		UnsortedBeanMatchables<Object> xs = 
			new UnsortedBeanMatchables<Object>(fruitX, factory);
				
		List<Fruit> fruitY = Arrays.asList(
				new Fruit("pear", 4),
				new Fruit("apple", 5),
				new Fruit("orange", 3));
				
		UnsortedBeanMatchables<Object> ys = 
			new UnsortedBeanMatchables<Object>(fruitY, factory);

		Results results = new Results();
		
		OrderedMatchablesComparer test = new OrderedMatchablesComparer(
				accessor, 
				new ComparersByPropertyOrType(
						null, new DefaultComparersByType()),
				results);
		
		test.compare(xs, ys);

		assertEquals(0, results.xsMissing.size());
		assertEquals(0, results.ysMissing.size());
				
		assertEquals("apple", results.comparedKeys.get(0)[0]);
		assertEquals("orange", results.comparedKeys.get(1)[0]);
		assertEquals("pear", results.comparedKeys.get(2)[0]);
		
		assertEquals(true, results.comparisons.get(0).getResult() != 0);
		assertEquals(0, results.comparisons.get(1).getResult());
		assertEquals(0, results.comparisons.get(2).getResult());		
	}

	public void testTwoXMissingOneYDuplicated() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "type" },
				new String[] { "quantity" },
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);		

		List<Fruit> fruitX = Arrays.asList(		
				new Fruit("apple", 4),
				new Fruit("banana", 5));
				
		UnsortedBeanMatchables<Object> xs = 
			new UnsortedBeanMatchables<Object>(fruitX, factory);
		
		
		List<Fruit> fruitY = Arrays.asList(		
				new Fruit("apple", 4),
				new Fruit("apple", 5),
				new Fruit("banana", 5),
				new Fruit("orange", 2));
				
		UnsortedBeanMatchables<Object> ys = 
			new UnsortedBeanMatchables<Object>(fruitY, factory);
		
		Results results = new Results();
		
		OrderedMatchablesComparer test = new OrderedMatchablesComparer(
				accessor, 
				new ComparersByPropertyOrType(
						null, new DefaultComparersByType()),
				results);
		
		test.compare(xs, ys);
		
		assertEquals(2, results.comparedKeys.size());
		
		assertEquals("apple", results.comparedKeys.get(0)[0]);
		assertEquals("banana", results.comparedKeys.get(1)[0]);
		
		assertEquals(0, results.comparisons.get(0).getResult());
		assertEquals(0, results.comparisons.get(1).getResult());
		
		assertEquals(2, results.xsMissing.size());
		
		assertEquals("apple", results.xsMissing.get(0)[0]);
		assertEquals("orange", results.xsMissing.get(1)[0]);
		
		assertEquals(0, results.ysMissing.size());
	}
	
	public void testWithNoKey() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				new String[] { "type", "quantity" },
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);		

		List<Fruit> fruitX = Arrays.asList(		
				new Fruit("apple", 5),
				new Fruit("banana", 5),
				new Fruit("kiwi", 5)
			);
				
		UnsortedBeanMatchables<Object> xs = 
			new UnsortedBeanMatchables<Object>(fruitX, factory);
		
		
		List<Fruit> fruitY = Arrays.asList(		
				new Fruit("banana", 5),
				new Fruit("orange", 2),
				new Fruit("pear", 2),
				new Fruit("apple", 4),
				new Fruit("apple", 5)
			);
				
		UnsortedBeanMatchables<Object> ys = 
			new UnsortedBeanMatchables<Object>(fruitY, factory);
		
		Results results = new Results();
		
		OrderedMatchablesComparer test = new OrderedMatchablesComparer(
				accessor, 
				new ComparersByPropertyOrType(
						null, new DefaultComparersByType()),
				results);
		
		IterableComparison<MatchableGroup> multiItemComparison =
				test.compare(xs, ys);
		
		assertEquals(1, multiItemComparison.getDifferentCount());
		assertEquals(2, multiItemComparison.getMatchedCount());
		assertEquals(2, multiItemComparison.getXMissingCount());
		assertEquals(0, multiItemComparison.getYMissingCount());
		
		assertEquals(3, results.comparedKeys.size());
		assertEquals(0, results.comparedKeys.get(0).length);
		assertEquals(0, results.comparedKeys.get(1).length);
		assertEquals(0, results.comparedKeys.get(2).length);
		
		for (MultiValueComparison<?> comparison : results.comparisons) {
			
			logger.info(comparison.toString());
			
			for (Comparison<?> valueComparison : 
				comparison.getValueComparisons()) {
				
				logger.info("  " + valueComparison.toString());
				
			}
		}
		
		assertEquals(3, results.comparisons.size());
		
	}
}