package org.oddjob.beancmpr.matchables;

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
		
	public void testCreateKeys() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				new String[] { "fruit", "colour" }, 
				null,
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Object> factory = 
				new BeanMatchableFactory<Object>(definition, accessor);
		
		Snack snack = new Snack();
		
		Matchable result;
		Object[] values;
		
		
		result = factory.createMatchable(snack);
		
		values = Iterables.toArray(result.getKeys(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals(null, values[0]);
		assertEquals(null, values[1]);
		
		snack.setFruit("apple");
		snack.setColour("red");
		
		result = factory.createMatchable(snack);
		
		values = Iterables.toArray(result.getKeys(), Object.class);
		
		assertEquals(2, values.length);
		
		assertEquals("apple", values[0]);
		assertEquals("red", values[1]);
		
		MatchableMetaData metaData = result.getMetaData();
		
		String[] keyMeta = Iterables.toArray(metaData.getKeyProperties(), 
				String.class);
		String[] valueMeta = Iterables.toArray(metaData.getValueProperties(), 
				String.class);
		String[] otherMeta = Iterables.toArray(metaData.getOtherProperties(), 
				String.class);
		
		assertEquals(2, keyMeta.length);
		assertEquals("fruit", keyMeta[0]);
		assertEquals("colour", keyMeta[1]);
		
		assertEquals(0, valueMeta.length);
		assertEquals(0, otherMeta.length);
		
		assertEquals(String.class, metaData.getPropertyType("fruit"));
		assertEquals(String.class, metaData.getPropertyType("colour"));
	}
	
	public void testCreateValues() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				new String[] { "fruit", "colour" }, 
				null);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Object> factory = 
				new BeanMatchableFactory<Object>(definition, accessor);
		
		Snack snack = new Snack();
		
		Matchable result;
		Object[] values;
		
		
		result = factory.createMatchable(snack);
		
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
		
		MatchableMetaData metaData = result.getMetaData();
		
		String[] keyMeta = Iterables.toArray(metaData.getKeyProperties(), 
				String.class);
		String[] valueMeta = Iterables.toArray(metaData.getValueProperties(), 
				String.class);
		String[] otherMeta = Iterables.toArray(metaData.getOtherProperties(), 
				String.class);
		
		assertEquals(0, keyMeta.length);
		
		assertEquals(2, valueMeta.length);
		assertEquals("fruit", valueMeta[0]);
		assertEquals("colour", valueMeta[1]);
		
		assertEquals(0, otherMeta.length);
		
		assertEquals(String.class, metaData.getPropertyType("fruit"));
		assertEquals(String.class, metaData.getPropertyType("colour"));
	}
	
	public void testCreateOthers() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null,
				null,
				new String[] { "fruit", "colour" }
				);
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		BeanMatchableFactory<Object> factory = 
				new BeanMatchableFactory<Object>(definition, accessor);
		
		Snack snack = new Snack();
		
		Matchable result;
		Object[] values;
		Object[] others;
		
		result = factory.createMatchable(snack);
		
		values = Iterables.toArray(result.getValues(), Object.class);
		assertEquals(1, values.length);
		assertSame(snack, values[0]);
		
		others = Iterables.toArray(result.getOthers(), Object.class);
		
		assertEquals(2, others.length);
		
		assertEquals(null, others[0]);
		assertEquals(null, others[1]);
		
		snack.setFruit("apple");
		snack.setColour("red");
		
		result = factory.createMatchable(snack);
		
		others = Iterables.toArray(result.getOthers(), Object.class);
		
		assertEquals(2, others.length);
		
		assertEquals("apple", others[0]);
		assertEquals("red", others[1]);
		
		MatchableMetaData metaData = result.getMetaData();
		
		String[] keyMeta = Iterables.toArray(metaData.getKeyProperties(), 
				String.class);
		String[] valueMeta = Iterables.toArray(metaData.getValueProperties(), 
				String.class);
		String[] otherMeta = Iterables.toArray(metaData.getOtherProperties(), 
				String.class);
		
		assertEquals(0, keyMeta.length);
		
		assertEquals(1, valueMeta.length);
		assertEquals("value", valueMeta[0]);
		
		assertEquals(2, otherMeta.length);
		assertEquals("fruit", otherMeta[0]);
		assertEquals("colour", otherMeta[1]);
		
		assertEquals(String.class, metaData.getPropertyType("fruit"));
		assertEquals(String.class, metaData.getPropertyType("colour"));
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
		
		BeanMatchableFactory<Object> factory = 
				new BeanMatchableFactory<Object>(definition, accessor);

		
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
		
		BeanMatchableFactory<Object> factory = 
				new BeanMatchableFactory<Object>(definition, accessor);
		
		Matchable matchable = factory.createMatchable(bean);
		
		assertEquals(0, Iterables.toArray(
				matchable.getKeys(), Object.class).length);
		assertEquals(0, Iterables.toArray(
				matchable.getOthers(), Object.class).length);
		
		Object[] values = Iterables.toArray(matchable.getValues(), Object.class);
		assertEquals("apple", values[0]);
		assertEquals(42, values[1]);
	}
	
	public void testGivenNoPropertiesSingleValueCreated() {
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null, null, null);
		
		BeanMatchableFactory<String> test = 
				new BeanMatchableFactory<String>(definition, null);
		
		Matchable matchable = test.createMatchable("red");
		
		Object[] values = Iterables.toArray(matchable.getValues(), Object.class);
		
		assertEquals("red", values[0]);
		assertEquals(1, values.length);
			
		MatchableMetaData meta = matchable.getMetaData();

		String[] valueProperties = Iterables.toArray(
				meta.getValueProperties(), String.class);
		
		assertEquals("value", valueProperties[0]);
		assertEquals(1, valueProperties.length);
		
		assertEquals(String.class, meta.getPropertyType("value"));
		
		assertEquals(0, Iterables.toArray(
				meta.getKeyProperties()).length);
		assertEquals(0, Iterables.toArray(
				meta.getOtherProperties()).length);
	}
}
