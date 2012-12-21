package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.DefaultComparersByType;

import junit.framework.TestCase;

public class DefaultComparersByTypeTest extends TestCase {

	public void testFindByType() {
		
		DefaultComparersByType test = new DefaultComparersByType();
		
		Comparer<Number> numberComparer = test.comparerFor(Number.class);
		assertEquals(Number.class, numberComparer.getType());
		
		Comparer<Object> objectComparer = test.comparerFor(Object.class);
		assertEquals(Object.class, objectComparer.getType());
		
		Comparer<String[]> arrayComparer = test.comparerFor(String[].class);
		assertEquals(Object[].class, arrayComparer.getType());
	}
}
