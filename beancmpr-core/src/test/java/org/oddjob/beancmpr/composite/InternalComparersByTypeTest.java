package org.oddjob.beancmpr.composite;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.comparers.TextComparer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class InternalComparersByTypeTest extends TestCase {

	@Test
	void testRetrieveByExactType() {
		
		Map<Type, Comparer<?>> comparers = new HashMap<>();
		
		InternalComparersByType test = new InternalComparersByType(comparers);
		
		TextComparer textComparer = new TextComparer();
		
		comparers.put(String.class, textComparer);
		
		assertSame(textComparer, test.comparerFor(String.class));
		
		assertEquals(null, test.comparerFor(Object.class));
	}
	
	@Test
	void testRetrieveBySubType() {
		
		Map<Type, Comparer<?>> comparers = new HashMap<>();
		
		InternalComparersByType test = new InternalComparersByType(comparers);
		
		NumericComparer numericComparer = new NumericComparer();
		
		comparers.put(Number.class, numericComparer);
		
		assertSame(numericComparer, test.comparerFor(Integer.class));
		
		assertEquals(null, test.comparerFor(Object.class));
	}
}
