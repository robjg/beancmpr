package org.oddjob.beancmpr.beans;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class PropertyTypeHelperTest extends TestCase {

	public void testDoubleInteger() {
		
		PropertyTypeHelper test = new PropertyTypeHelper();
		
		Class<?> result;
		
		result = test.typeFor("Foo", Double.class, Integer.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", Integer.class, Double.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", int.class, double.class);
		
		assertEquals(Number.class, result);
	}
	
	public void testOneSubClassOfOtherCases() {
		
		PropertyTypeHelper test = new PropertyTypeHelper();
		
		Class<?> result;
		
		result = test.typeFor("Foo", Number.class, Integer.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", Integer.class, Number.class);
		
		assertEquals(Number.class, result);
	}
	
	public void testBigDecimalCases() {
		
		PropertyTypeHelper test = new PropertyTypeHelper();
		
		Class<?> result;
		
		result = test.typeFor("Foo", Double.class, BigDecimal.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", int.class, BigDecimal.class);
		
		assertEquals(Number.class, result);
		
		result = test.typeFor("Foo", String.class, BigDecimal.class);
		
		assertEquals(Object.class, result);
	}
}
