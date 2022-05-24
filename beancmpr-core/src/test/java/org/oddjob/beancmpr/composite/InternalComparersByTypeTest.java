package org.oddjob.beancmpr.composite;

import java.util.HashMap;
import java.util.Map;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.comparers.TextComparer;

import junit.framework.TestCase;

public class InternalComparersByTypeTest extends TestCase {

	public void testRetriveByExactType() {
		
		Map<Class<?>, Comparer<?>> comparers = new HashMap<>();
		
		InternalComparersByType test = new InternalComparersByType(comparers);
		
		TextComparer textComparer = new TextComparer();
		
		comparers.put(String.class, textComparer);
		
		assertSame(textComparer, test.comparerFor(String.class));
		
		assertEquals(null, test.comparerFor(Object.class));
	}
	
	public void testRetriveBySubType() {
		
		Map<Class<?>, Comparer<?>> comparers = new HashMap<>();
		
		InternalComparersByType test = new InternalComparersByType(comparers);
		
		NumericComparer numericComparer = new NumericComparer();
		
		comparers.put(Number.class, numericComparer);
		
		assertSame(numericComparer, test.comparerFor(Integer.class));
		
		assertEquals(null, test.comparerFor(Object.class));
	}
}
