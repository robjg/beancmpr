package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;
import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.TestCase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class MapMatchableFactoryTest extends TestCase {

	@Test
	void testCreateKeyAndValue() {
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		MatchDefinition definition = SimpleMatchDefinition.of(
				null, null, null);
		
		Map<String, Integer> map = new HashMap<>();
		
		map.put("apple", 42);
		
		MapMatchableFactory<String, Integer> test = 
				new MapMatchableFactory<>(definition, accessor);
		
		Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
		
		Matchable matchable = test.createMatchable(it.next());
		
		Object[] keys = Iterables.toArray(matchable.getKeys());
		
		assertEquals("apple", keys[0]);
		assertEquals(1, keys.length);
		
		Object[] values = Iterables.toArray(matchable.getValues());
		
		assertEquals(42, values[0]);
		assertEquals(1, values.length);
		
		Object[] others = Iterables.toArray(matchable.getOthers());
		
		assertEquals(0, others.length);
		
		MatchableMetaData metaData = matchable.getMetaData();
		
		String[] keyNames = Iterables.toArray(metaData.getKeyProperties(), 
				String.class);
		
		assertEquals("key", keyNames[0]);
		assertEquals(1, keyNames.length);
		
		String[] valueNames = Iterables.toArray(metaData.getValueProperties(), 
				String.class);
		
		assertEquals("value", valueNames[0]);
		assertEquals(1, valueNames.length);
		
		String[] otherNames = Iterables.toArray(metaData.getOtherProperties(), 
				String.class);
		
		assertEquals(0, otherNames.length);
		
		assertEquals(String.class, metaData.getPropertyType("key"));
		assertEquals(Integer.class, metaData.getPropertyType("value"));
	}
	
	public static class Fruit {
		
		public String getColour() {
			return "green";
		}
		
		public int getQuantity() {
			return 42;
		}
	}
	
	@Test
	void testKeyAndValueAreBeans() {
		
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		MatchDefinition definition = SimpleMatchDefinition.of(
				new String[] { "colour", "quantity" }, 
				new String[] { "colour" },
				new String[] { "quantity" });
		
		Map<Fruit, Fruit> map = new HashMap<>();
		
		map.put(new Fruit(), new Fruit());
		
		MapMatchableFactory<Fruit, Fruit> test = 
				new MapMatchableFactory<>(definition, accessor);
		
		Iterator<Map.Entry<Fruit, Fruit>> it = map.entrySet().iterator();
		
		Matchable matchable = test.createMatchable(it.next());
		
		Object[] keys = Iterables.toArray(matchable.getKeys());
		
		assertEquals("green", keys[0]);
		assertEquals(42, keys[1]);
		assertEquals(2, keys.length);
		
		Object[] values = Iterables.toArray(matchable.getValues());
		
		assertEquals("green", values[0]);
		assertEquals(1, values.length);
		
		Object[] others = Iterables.toArray(matchable.getOthers());
		
		assertEquals(42, others[0]);
		assertEquals(1, others.length);
		
		MatchableMetaData metaData = matchable.getMetaData();
		
		String[] keyNames = Iterables.toArray(metaData.getKeyProperties(), 
				String.class);
		
		assertEquals("colour", keyNames[0]);
		assertEquals("quantity", keyNames[1]);
		assertEquals(2, keyNames.length);
		
		String[] valueNames = Iterables.toArray(metaData.getValueProperties(), 
				String.class);
		
		assertEquals("colour", valueNames[0]);
		assertEquals(1, valueNames.length);
		
		String[] otherNames = Iterables.toArray(metaData.getOtherProperties(), 
				String.class);
		
		assertEquals("quantity", otherNames[0]);
		assertEquals(1, otherNames.length);
		
		assertEquals(String.class, metaData.getPropertyType("colour"));
		assertEquals(Integer.class, metaData.getPropertyType("quantity"));
	}	
}
