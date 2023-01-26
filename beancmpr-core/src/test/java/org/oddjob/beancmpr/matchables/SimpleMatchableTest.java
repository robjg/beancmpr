package org.oddjob.beancmpr.matchables;

import junit.framework.TestCase;
import org.oddjob.arooa.utils.Iterables;

import java.util.ArrayList;
import java.util.List;


public class SimpleMatchableTest extends TestCase {
	
	public void testKeys() {
		
		List<Comparable<?>> keys1 = new ArrayList<>();
		keys1.add("Apple");
		keys1.add(2);
		
		SimpleMatchable test  = new SimpleMatchable(
				keys1, null, null, null);
		
		Object[] result = Iterables.toArray(test.getKeys());
		
		assertEquals("Apple", result[0]);
		assertEquals(2, result[1]);
		assertEquals(2, result.length);
	}

	public void testValues() {
		
		List<Object> values = new ArrayList<>();
		values.add("Apple");
		values.add(2);
		
		SimpleMatchable test1 = new SimpleMatchable(
				null, values, null, null);
		
		Object[] results = Iterables.toArray(test1.getValues(), Object.class);
		assertEquals("Apple", results[0]);
		assertEquals(2, results[1]);
		assertEquals(2, results.length);
	}
	
	public void testOthers() {
		
		List<Object> others = new ArrayList<>();
		others.add("Apple");
		others.add(2);
		
		SimpleMatchable test1 = new SimpleMatchable(
				null, null, others, null);
		
		Object[] results = Iterables.toArray(test1.getOthers(), Object.class);
		assertEquals("Apple", results[0]);
		assertEquals(2, results[1]);
		assertEquals(2, results.length);
	}
}
