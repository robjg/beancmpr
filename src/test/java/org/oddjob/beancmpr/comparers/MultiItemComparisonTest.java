package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.comparers.MultiItemComparison;

import junit.framework.TestCase;

public class MultiItemComparisonTest extends TestCase {

	public void testIsEqual() {
		
		MultiItemComparison<Object> test1 = 
				new MultiItemComparison<Object>(null, null, 0, 0, 0, 3);
		
		assertEquals(0, test1.getResult());
		
		MultiItemComparison<Object> test2 = 
				new MultiItemComparison<Object>(null, null, 1, 0, 0, 3);
		
		assertTrue(new Integer(test2.getResult()).toString(), 
				test2.getResult() > 0);
		
		MultiItemComparison<Object> test3 = 
				new MultiItemComparison<Object>(null, null, 0, 1, 0, 3);
		
		assertTrue(test3.getResult() < 0);
		
		MultiItemComparison<Object> test4 = 
				new MultiItemComparison<Object>(null, null, 0, 0, 1, 3);
		
		assertTrue(test4.getResult() != 0);		

	}
	
	public void testNumbers() {
		
		MultiItemComparison<Object> test = 
				new MultiItemComparison<Object>(null, null, 1, 2, 3, 4);
		
		assertEquals(1, test.getXsMissing());
		assertEquals(2, test.getYsMissing());
		assertEquals(3, test.getDifferent());
		assertEquals(4, test.getSame());	
	}
}
