package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;


public class SortedBeanMatchablesTest extends TestCase {

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
	
	public void testIteratingWithOneKey() {
				
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "type" }, 
				new String[] { },
				new String[] { "colour" });
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Fruit> factory = new BeanMatchableFactory<>(
				definition, accessor);
		
		List<Fruit> fruits = new ArrayList<Fruit>();
		
		Fruit bean1 = new Fruit("apple", "red");
		Fruit bean2 = new Fruit("apple", "green");
		Fruit bean3 = new Fruit("banana", "yellow");
		
		fruits.add(bean1);
		fruits.add(bean2);
		fruits.add(bean3);
		
		Iterable<MatchableGroup> test = new SortedBeanMatchables<Fruit>(
				fruits, factory, new ComparersByNameOrType());
		
		MatchableGroup[] groups = Iterables.toArray(
				test, MatchableGroup.class);		
		
		assertEquals(2, groups.length);
				
		Matchable[] matchables = Iterables.toArray(
				groups[0].getGroup(), Matchable.class);
				
		assertEquals(2, matchables.length);
		
		Object[] matchable0Keys = Iterables.toArray(matchables[0].getKeys());
		
		assertEquals("apple", matchable0Keys[0]);
		
		Object[] matchable0Others = Iterables.toArray(matchables[0].getOthers());
		
		assertEquals("red", matchable0Others[0]);
	
		matchables = Iterables.toArray(
				groups[1].getGroup(), Matchable.class);
		
		Object[] matchable1Keys = Iterables.toArray(matchables[0].getKeys());
		
		assertEquals("banana", matchable1Keys[0]);		
	}

	public void testIteratingWithTwoKeys() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "type", "colour" }, 
				new String[] { },
				new String[] { });
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Fruit> factory = new BeanMatchableFactory<>(
				definition, accessor);
		
		List<Fruit> fruits = new ArrayList<Fruit>();
		
		Fruit bean1 = new Fruit("apple", "green");
		Fruit bean2 = new Fruit("apple", "red");
		Fruit bean3 = new Fruit("banana", "yellow");
		
		fruits.add(bean1);
		fruits.add(bean2);
		fruits.add(bean3);
		
		Iterable<MatchableGroup> test = new SortedBeanMatchables<Fruit>(
				fruits, factory, new ComparersByNameOrType());
		
		MatchableGroup[] groups = Iterables.toArray(
				test, MatchableGroup.class);		
		
		assertEquals(3, groups.length);
				
		Matchable[] matchables = Iterables.toArray(
				groups[0].getGroup(), Matchable.class);
				
		assertEquals(1, matchables.length);
		
		Object[] matchable0Keys = Iterables.toArray(matchables[0].getKeys());
		
		assertEquals("apple", matchable0Keys[0]);
		assertEquals("green", matchable0Keys[1]);
	
		matchables = Iterables.toArray(
				groups[2].getGroup(), Matchable.class);
		
		Object[] matchable1Keys = Iterables.toArray(matchables[0].getKeys());
		
		assertEquals("banana", matchable1Keys[0]);		
	}

	public void testThrowsExceptionWhenUnordered() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "type", "colour" }, 
				new String[] { },
				new String[] { });
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Fruit> factory = new BeanMatchableFactory<>(
				definition, accessor);
		
		List<Fruit> fruits = new ArrayList<Fruit>();
		
		Fruit bean1 = new Fruit("apple", "green");
		Fruit bean2 = new Fruit("banana", "yellow");
		Fruit bean3 = new Fruit("apple", "red");
		
		fruits.add(bean1);
		fruits.add(bean2);
		fruits.add(bean3);
		
		Iterable<MatchableGroup> test = new SortedBeanMatchables<Fruit>(
				fruits, factory, new ComparersByNameOrType());
		
		Iterator<MatchableGroup> iter = test.iterator();
		
		assertEquals(true, iter.hasNext());
		assertEquals(1, iter.next().getSize());
		
		assertEquals(true, iter.hasNext());
		
		try {
			iter.next().getSize();
			fail("Should fail.");
		}
		catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	private class MockFactory implements MatchableFactory<Object> {
		@Override
		public Matchable createMatchable(Object bean) {
			throw new RuntimeException("Unexpected!");
		}
	}
	
	public void testEmptyIterator() {
		
		final SortedBeanMatchables<Object> test = new SortedBeanMatchables<Object>(
				new ArrayList<Fruit>(), new MockFactory(), 
				new ComparersByNameOrType());
		
		Iterator<MatchableGroup> iter = test.iterator();
		
		assertFalse(iter.hasNext());
		
		try {
			assertNull(iter.next());
			fail("Exception expected.");
		}
		catch (NoSuchElementException e) {
			// expected.
		}
	}
		
}
