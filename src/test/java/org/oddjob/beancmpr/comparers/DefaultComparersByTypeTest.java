package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.composite.DefaultComparersByType;

import junit.framework.TestCase;

public class DefaultComparersByTypeTest extends TestCase {

	public void testFindByType() {
		
		DefaultComparersByType test = new DefaultComparersByType();
		
		Comparer<Number> numberComparer = test.comparerFor(Number.class);
		assertEquals(Number.class, numberComparer.getType());
		
		Comparer<Integer> integerComparer = test.comparerFor(Integer.class);
		assertEquals(Comparable.class, integerComparer.getType());

		Comparer<Object> objectComparer = test.comparerFor(Object.class);
		assertEquals(Object.class, objectComparer.getType());
		
		Comparer<String[]> arrayComparer = test.comparerFor(String[].class);
		assertEquals(Object[].class, arrayComparer.getType());
	}
}
