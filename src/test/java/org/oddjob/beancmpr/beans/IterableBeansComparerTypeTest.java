package org.oddjob.beancmpr.beans;

import java.util.Arrays;

import junit.framework.TestCase;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.SharedTestData;
import org.oddjob.beancmpr.SharedTestData.Fruit;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.beancmpr.results.RowSetResultHandler;

public class IterableBeansComparerTypeTest extends TestCase {
	
	public void testCompareListsOfFruitBeansWithTypeAsKeyAndValuesToCompare() {
		
		ArooaSession session = new StandardArooaSession();
		
		SharedTestData testData = new SharedTestData();
		
		IterableBeansComparerType<Fruit> test = 
				new IterableBeansComparerType<Fruit>();
		
		test.setArooaSession(session);
		
		test.setKeys(new String[] { "id" });
		test.setValues(new String[] { "type", "quantity", "colour" });
		
		RowSetResultHandler results = 
				new RowSetResultHandler();
		
		IterableBeansComparer<Fruit> comparer = test.createComparerWith(
				new DefaultComparersByType(), results);
		
		MultiItemComparison<Iterable<Fruit>> comparison = 
				comparer.compare(testData.getFruitX(), testData.getFruitY());
		
		assertEquals(4, comparison.getComparedCount());
		assertEquals(4, comparison.getBreaksCount());
		assertEquals(0, comparison.getMatchedCount());
		assertEquals(2, comparison.getDifferentCount());
		assertEquals(1, comparison.getXMissingCount());
		assertEquals(1, comparison.getYMissingCount());
		
		assertEquals(true, results.next());
		
		assertEquals(MatchResultType.Type.NOT_EQUAL, results.getResultType());
		assertEquals(new Long(1), results.getKey(0));
		
		assertEquals("Apple", results.getComparison(0).getX());
		assertEquals("Apple", results.getComparison(0).getY());
		assertEquals(0, results.getComparison(0).getResult());
		
		assertEquals(4, results.getComparison(1).getX());
		assertEquals(4, results.getComparison(1).getY());
		assertEquals(0, results.getComparison(1).getResult());
		
		assertEquals("green", results.getComparison(2).getX());
		assertEquals("red", results.getComparison(2).getY());
		assertEquals(true, results.getComparison(2).getResult() != 0);
		assertEquals("green<>red", results.getComparison(2).getSummaryText());
		
		assertEquals(true, results.next());
		
		assertEquals(MatchResultType.Type.NOT_EQUAL, results.getResultType());
		assertEquals(new Long(2), results.getKey(0));
		
		assertEquals(0, results.getComparison(0).getResult());
		
		assertEquals(3, results.getComparison(1).getX());
		assertEquals(4, results.getComparison(1).getY());
		assertEquals(true, results.getComparison(1).getResult() != 0);
		assertEquals("3<>4", results.getComparison(1).getSummaryText());
		
		assertEquals(0, results.getComparison(2).getResult());
		
		assertEquals(true, results.next());
		
		assertEquals(MatchResultType.Type.X_MISSING, results.getResultType());
		assertEquals(new Long(3), results.getKey(0));
		
		assertEquals(true, results.next());
		
		assertEquals(MatchResultType.Type.Y_MISSING, results.getResultType());
		assertEquals(new Long(5), results.getKey(0));
		
		assertEquals(false, results.next());
	}
	
	public void testCompareListOfFruitKeyedOnNoneUniqueTypeWithAdditionalValue() {
		
		ArooaSession session = new StandardArooaSession();
		
		SharedTestData testData = new SharedTestData();
		
		IterableBeansComparerType<Fruit> test = 
				new IterableBeansComparerType<Fruit>();
		
		test.setArooaSession(session);
		
		test.setKeys(new String[] { "type" });
		test.setValues(new String[] { "quantity" });
		test.setOthers(new String[] { "colour" });
		
		MultiItemComparer<Iterable<Fruit>> comparer = test.createComparerWith(
				new DefaultComparersByType());
		
		MultiItemComparison<Iterable<Fruit>> comparison = 
				comparer.compare(testData.getFruitX(), testData.getFruitY());
		
		assertEquals(3, comparison.getComparedCount());
		assertEquals(1, comparison.getBreaksCount());
		assertEquals(2, comparison.getMatchedCount());
		assertEquals(1, comparison.getDifferentCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
		
	}
	
	public void testCompareEqualListsOfStrings() {
		
		ArooaSession session = new StandardArooaSession();
		
		Iterable<String> x = Arrays.asList("red", "blue", "green");
		
		Iterable<String> y = Arrays.asList("green", "red", "blue");
		
		IterableBeansComparerType<String> test = 
				new IterableBeansComparerType<>();
		
		test.setArooaSession(session);
		
		MultiItemComparer<Iterable<String>> comparer = test.createComparerWith(
				new DefaultComparersByType());
		
		MultiItemComparison<Iterable<String>> comparison = 
				comparer.compare(x, y);
		
		assertEquals(0, comparison.getResult());
		assertEquals("Equal, 3 matched", comparison.getSummaryText());
		
		assertEquals(3, comparison.getComparedCount());
		assertEquals(0, comparison.getBreaksCount());
		assertEquals(3, comparison.getMatchedCount());
		assertEquals(0, comparison.getDifferentCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}
}
