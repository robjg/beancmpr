package org.oddjob.beancmpr.comparers;

import java.util.Arrays;

import org.oddjob.beancmpr.comparers.DefaultComparersByType;
import org.oddjob.beancmpr.comparers.MultiItemComparison;
import org.oddjob.beancmpr.comparers.SimpleIterableComparer;

import junit.framework.TestCase;

public class SimpleIterableComparerTest extends TestCase {

	public void testCompareEqual() {
		
		SimpleIterableComparer<String> test = 
				new SimpleIterableComparer<String>();
		
		test.setComparersByType(new DefaultComparersByType());
		
		Iterable<String> x = Arrays.asList("a", "b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c", "a");
		
		MultiItemComparison<String> comparison = test.compare(x, y);
		
		assertEquals(0, comparison.getResult());
		assertEquals(3, comparison.getSame());
		assertEquals(0, comparison.getDifferent());		
		assertEquals(0, comparison.getXsMissing());
		assertEquals(0, comparison.getYsMissing());
	}
	
	public void testCompareOneDifferentEqual() {
		
		SimpleIterableComparer<String> test = 
				new SimpleIterableComparer<String>();
		
		test.setComparersByType(new DefaultComparersByType());
		
		Iterable<String> x = Arrays.asList("a", "b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c", "d");
		
		MultiItemComparison<String> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getSame());
		assertEquals(1, comparison.getDifferent());		
		assertEquals(0, comparison.getXsMissing());
		assertEquals(0, comparison.getYsMissing());
	}

	public void testCompareOneXMissing() {
		
		SimpleIterableComparer<String> test = 
				new SimpleIterableComparer<String>();
		
		test.setComparersByType(new DefaultComparersByType());
		
		Iterable<String> x = Arrays.asList("b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c", "a");
		
		MultiItemComparison<String> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getSame());
		assertEquals(0, comparison.getDifferent());		
		assertEquals(1, comparison.getXsMissing());
		assertEquals(0, comparison.getYsMissing());
	}

	public void testCompareOneYMissing() {
		
		SimpleIterableComparer<String> test = 
				new SimpleIterableComparer<String>();
		
		test.setComparersByType(new DefaultComparersByType());
		
		Iterable<String> x = Arrays.asList("a", "b", "c");
		
		Iterable<String> y = Arrays.asList("b", "c");
		
		MultiItemComparison<String> comparison = test.compare(x, y);
		
		assertEquals(false, comparison.getResult() == 0);
		assertEquals(2, comparison.getSame());
		assertEquals(0, comparison.getDifferent());		
		assertEquals(0, comparison.getXsMissing());
		assertEquals(1, comparison.getYsMissing());
	}

}
