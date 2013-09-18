package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.beanutils.MagicBeanClassCreator;
import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;

public class BeanMatchableFactoryTest extends TestCase {

	public static class Snack {
		
		private String fruit;
		
		private String colour;

		public String getFruit() {
			return fruit;
		}

		public void setFruit(String fruit) {
			this.fruit = fruit;
		}

		public String getColour() {
			return colour;
		}

		public void setColour(String colour) {
			this.colour = colour;
		}
	}
		
	@SuppressWarnings("unchecked")
	public void testCreateKeys() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "fruit", "colour" }, 
				null,
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);
		
		Snack snack = new Snack();
		
		Matchable result;
		Object[] values;
		
		
		result = factory.createMatchable(snack);
		
		assertEquals(new SimpleMatchKey(Arrays.asList(
				(Comparable<?>) null, (Comparable<?>) null)),
				result.getKey());

		values = Iterables.toArray(result.getKeys(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals(null, values[0]);
		assertEquals(null, values[1]);
		
		snack.setFruit("apple");
		snack.setColour("red");
		
		result = factory.createMatchable(snack);
		
		assertEquals(new SimpleMatchKey(Arrays.asList("apple", "red")),
				result.getKey());

		values = Iterables.toArray(result.getKeys(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals("apple", values[0]);
		assertEquals("red", values[1]);
		
		MatchableMetaData metaData = result.getMetaData();
		
		assertEquals(String.class, metaData.getPropertyType("fruit"));
	}
	
	public void testCreateValues() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				new String[] { "fruit", "colour" }, 
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);
		
		Snack snack = new Snack();
		
		Matchable result;
		Object[] values;
		
		
		result = factory.createMatchable(snack);
		
		assertEquals(new SimpleMatchKey(new ArrayList<Comparable<Object>>()),
				result.getKey());

		values = Iterables.toArray(result.getValues(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals(null, values[0]);
		assertEquals(null, values[1]);
		
		snack.setFruit("apple");
		snack.setColour("red");
		
		result = factory.createMatchable(snack);
		
		values = Iterables.toArray(result.getValues(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals("apple", values[0]);
		assertEquals("red", values[1]);
	}
	
	public void testCreateOthers() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				null,
				new String[] { "fruit", "colour" }
				);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);
		
		Snack snack = new Snack();
		
		Matchable result;
		Object[] values;
		
		
		result = factory.createMatchable(snack);
		
		assertEquals(new SimpleMatchKey(new ArrayList<Comparable<Object>>()),
				result.getKey());

		values = Iterables.toArray(result.getOthers(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals(null, values[0]);
		assertEquals(null, values[1]);
		
		snack.setFruit("apple");
		snack.setColour("red");
		
		result = factory.createMatchable(snack);
		
		values = Iterables.toArray(result.getOthers(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals("apple", values[0]);
		assertEquals("red", values[1]);
		
	}
	
	public static class ThingWithPrimitive {
		
		public int getInt() {
			return 2;
		}
	}
	
	public void testPrimativeType() {
		
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				new String[] { "int" },
				null
				);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);

		
		ThingWithPrimitive thing = new ThingWithPrimitive();
		
		Matchable result = factory.createMatchable(thing);
		Object[] values = Iterables.toArray(result.getValues(), Object.class);
		
		assertEquals(2, values[0]);
		
		assertEquals(Integer.class, result.getMetaData().getPropertyType("int"));
	}	
	
	public void testWithMagicBeans() {
		
		MagicBeanClassCreator creator = new MagicBeanClassCreator("fruit");
		creator.addProperty("type", String.class);
		creator.addProperty("quantity", Integer.class);
		
		ArooaClass beanClass = creator.create();
		
		Object bean = beanClass.newInstance();
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		accessor.setProperty(bean, "type", "apple");
		accessor.setProperty(bean, "quantity", 42);
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				new String[] { "type", "quantity" },
				null
				);
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);

		
		Matchable matchable = factory.createMatchable(bean);
		
		assertEquals(0, Iterables.toArray(
				matchable.getKeys(), Object.class).length);
		assertEquals(0, Iterables.toArray(
				matchable.getOthers(), Object.class).length);
		
		Object[] values = Iterables.toArray(matchable.getValues(), Object.class);
		assertEquals("apple", values[0]);
		assertEquals(42, values[1]);
	}
}
