package org.oddjob.beancmpr.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class ArrayIterableTest extends TestCase {

	public void testIterableOver3Elements() {
		
		
		int[] z = { 1, 2, 3 };
		
		Iterator<Object> a = new ArrayIterable(z).iterator();

		assertEquals(true, a.hasNext());
		assertEquals(1, a.next());
		
		assertEquals(true, a.hasNext());
		assertEquals(2, a.next());
		
		assertEquals(true, a.hasNext());
		assertEquals(3, a.next());
		
		assertEquals(false, a.hasNext());
	}
	
	public void testInForLoop() {
		
		String[] array = { "red", "blue", "green" };
		
		ArrayIterable test = new ArrayIterable(array);
		
		List<Object> results = new ArrayList<>();
		
		for (Object s : test) {
			results.add(s);
		}
		
		assertEquals("red", results.get(0));
		assertEquals("blue", results.get(1));
		assertEquals("green", results.get(2));
		
		assertEquals(3, results.size());
	}
}
