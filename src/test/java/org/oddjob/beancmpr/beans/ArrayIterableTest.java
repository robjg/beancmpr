package org.oddjob.beancmpr.beans;

import java.util.Iterator;

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
}
