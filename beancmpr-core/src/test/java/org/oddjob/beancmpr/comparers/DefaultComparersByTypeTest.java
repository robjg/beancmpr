package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.beans.ArrayComparer;
import org.oddjob.beancmpr.composite.DefaultComparersByType;


class DefaultComparersByTypeTest extends TestCase {

	@Test
	void testFindByTypeForSomeSimpleClasses() {
		
		DefaultComparersByType test = new DefaultComparersByType();
		
		Comparer<Number> numberComparer = test.comparerFor(Number.class);
		assertEquals(Number.class, numberComparer.getType());
		
		Comparer<Integer> integerComparer = test.comparerFor(Integer.class);
		assertEquals(Comparable.class, integerComparer.getType());

		// No primitives because all bean property comparison and array 
		// element comparison is via objects.
		Comparer<Integer> intComparer = test.comparerFor(int.class);
		assertEquals(null, intComparer);

		Comparer<String> textComparer = test.comparerFor(String.class);
		assertEquals(EqualityComparer.class, textComparer.getClass());
		
		Comparer<Object> objectComparer = test.comparerFor(Object.class);
		assertEquals(Object.class, objectComparer.getType());
		assertEquals(EqualityComparer.class, objectComparer.getClass());
		
	}

	@Test
	void testFindByTypeForArrays() {
		
		DefaultComparersByType test = new DefaultComparersByType();
		
		Comparer<String[]> arrayComparer = test.comparerFor(String[].class);
		assertEquals(Object.class, arrayComparer.getType());
		assertEquals(ArrayComparer.class, arrayComparer.getClass());
		
		// Note that int[] is not and instance of Object[].
		Comparer<?> intArrayComparer = test.comparerFor(int[].class);
		assertEquals(ArrayComparer.class, intArrayComparer.getClass());
	}
}
