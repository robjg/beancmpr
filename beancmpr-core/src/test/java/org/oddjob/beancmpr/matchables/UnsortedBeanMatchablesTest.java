package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;
import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

class UnsortedBeanMatchablesTest extends TestCase {

	public static class Fruit {
		
		private String type;
		
		private String colour;
		
		public String getType() {
			return type;
		}
		
		public void setType(String make) {
			this.type = make;
		}
		
		public String getColour() {
			return colour;
		}
		
		public void setColour(String colour) {
			this.colour = colour;
		}
	}
		
	
	@Test
	void testIterating() {
		
		MatchDefinition definition = SimpleMatchDefinition.of(
				new String[] { "type" }, 
				new String[] { "colour" },
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Fruit> factory = new BeanMatchableFactory<>(
				definition, accessor);
		
		List<Fruit> fruit = new ArrayList<>();
		
		Fruit bean1 = new Fruit();
		bean1.setType("banana");
		bean1.setColour("yellow");
		
		fruit.add(bean1);
		
		Fruit bean2 = new Fruit();
		bean2.setType("apple");
		bean2.setColour("red");
		
		fruit.add(bean2);
		
		Fruit bean3 = new Fruit();
		bean3.setType("apple");
		bean3.setColour("green");
		
		fruit.add(bean3);
		
		UnsortedBeanMatchables<Fruit> test = new UnsortedBeanMatchables<>(
				fruit, factory, new ComparersByNameOrType());

		MatchableGroup[] groups = Iterables.toArray(
				test, MatchableGroup.class);
		
		assertEquals(2, groups.length);
		
		Matchable[] matchables = Iterables.toArray(groups[0].getGroup(), 
				Matchable.class);
		
		assertEquals(2, matchables.length);

		Object[] keys;
		Object[] values;
		
		keys = Iterables.toArray(matchables[0].getKeys(), 
				Object.class);

		assertEquals("apple", keys[0]);
		
		values = Iterables.toArray(matchables[0].getValues(), 
				Object.class);
		
		assertEquals("red", values[0]);
		
		
		keys = Iterables.toArray(matchables[1].getKeys(), 
				Object.class);

		assertEquals("apple", keys[0]);
		
		values = Iterables.toArray(matchables[1].getValues(), 
				Object.class);
		
		assertEquals("green", values[0]);
		
	}
	
	private static class MockFactory implements MatchableFactory<Object> {
		@Override
		public Matchable createMatchable(Object bean) {
			throw new RuntimeException("Unexpected!");
		}
	}
	
	@Test
	void testEmptyIterator() {
		
		final UnsortedBeanMatchables<Object> test =
				new UnsortedBeanMatchables<>(
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
