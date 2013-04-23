package org.oddjob.beancmpr.beans;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class PropertyTypeHelperTest extends TestCase {

	public void test() {
		
		PropertyTypeHelper test = new PropertyTypeHelper(); 
		
		Class<?> result;
		
		result = test.typeFor("Foo", Double.class, Integer.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", Double.class, BigDecimal.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", int.class, BigDecimal.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", String.class, BigDecimal.class);
		
		assertEquals(Object.class, result);
	}
}
