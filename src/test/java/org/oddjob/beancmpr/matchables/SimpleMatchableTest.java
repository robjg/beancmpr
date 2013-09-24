package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.List;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.matchables.SimpleMatchable;

import junit.framework.TestCase;


public class SimpleMatchableTest extends TestCase {
	
	public void testKeys() {
		
		List<Comparable<?>> keys1 = new ArrayList<Comparable<?>>();
		keys1.add("Apple");
		keys1.add(new Integer(2));
		
		SimpleMatchable test  = new SimpleMatchable(
				keys1, null, null, null);
		
		Object[] result = Iterables.toArray(test.getKeys());
		
		assertEquals("Apple", result[0]);
		assertEquals(new Integer(2), result[1]);
		assertEquals(2, result.length);
	}

	public void testValues() {
		
		List<Object> values = new ArrayList<Object>();
		values.add("Apple");
		values.add(new Integer(2));
		
		SimpleMatchable test1 = new SimpleMatchable(
				null, values, null, null);
		
		Object[] results = Iterables.toArray(test1.getValues(), Object.class);
		assertEquals("Apple", results[0]);
		assertEquals(new Integer(2), results[1]);
		assertEquals(2, results.length);
	}
	
	public void testOthers() {
		
		List<Object> others = new ArrayList<Object>();
		others.add("Apple");
		others.add(new Integer(2));
		
		SimpleMatchable test1 = new SimpleMatchable(
				null, null, others, null);
		
		Object[] results = Iterables.toArray(test1.getOthers(), Object.class);
		assertEquals("Apple", results[0]);
		assertEquals(new Integer(2), results[1]);
		assertEquals(2, results.length);
	}
}
