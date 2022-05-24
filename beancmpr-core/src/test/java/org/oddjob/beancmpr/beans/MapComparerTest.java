package org.oddjob.beancmpr.beans;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.composite.ComparersByNameOrTypeFactory;
import org.oddjob.beancmpr.matchables.MapMatchableFactoryProvider;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

public class MapComparerTest extends TestCase {
	
	public void testKeyAndValueComparison() throws ArooaConversionException {
		
		Map<String, Integer> map1 = new HashMap<String, Integer>();
		
		map1.put("apple", 4);
		map1.put("orange", 3);
		map1.put("grape", 7);
		
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		
		map2.put("pear", 4);
		map2.put("orange", 3);
		map2.put("grape", 2);
				
		PropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		MatchDefinition definition = new SimpleMatchDefinition(
				null, null, null);
		
		MapMatchableFactoryProvider<String, Integer> matchableFactoryProvider = 
				new MapMatchableFactoryProvider<>(definition, accessor);
		
		MapComparer<String, Integer> test = new MapComparer<>(
				matchableFactoryProvider, 
				new ComparersByNameOrTypeFactory().createWith(null),
				false);
		
		MultiItemComparison<Map<String, Integer>> result = 
				test.compare(map1, map2);

		assertEquals(3, result.getResult());
		
		assertEquals(1, result.getXMissingCount());
		assertEquals(1, result.getYMissingCount());
		assertEquals(1, result.getDifferentCount());
		assertEquals(1, result.getMatchedCount());
		assertEquals(3, result.getBreaksCount());
		assertEquals(4, result.getComparedCount());
		
		assertEquals("3 differences", result.getSummaryText());
	}

}
