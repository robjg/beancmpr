package org.oddjob.beancmpr.beans;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.SharedTestData;
import org.oddjob.beancmpr.SharedTestData.Fruit;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.beancmpr.results.MatchResultHandlerFactory;
import org.oddjob.beancmpr.results.MatchResultType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IterableBeansComparerTypeTest {
	
	@Test
    void testCompareListsOfFruitBeansWithTypeAsKeyAndValuesToCompare() {
		
		ArooaSession session = new StandardArooaSession();
		
		SharedTestData testData = new SharedTestData();
		
		IterableBeansComparerType<Fruit> test =
				new IterableBeansComparerType<>();
		
		test.setArooaSession(session);
		
		test.setKeys(new String[] { "id" });
		test.setValues(new String[] { "type", "quantity", "colour" });

		List<MatchResult> results = new ArrayList<>();

		CompareResultsHandler handler = MatchResultHandlerFactory
				.handlerTo(results::add);

		IterableBeansComparer<Fruit> comparer = test.createComparerWith(
				new DefaultComparersByType(), handler);
		
		MultiItemComparison<Iterable<Fruit>> comparison = 
				comparer.compare(testData.getListFruitX(), testData.getListFruitY());
		
		assertEquals(4, comparison.getComparedCount());
		assertEquals(4, comparison.getBreaksCount());
		assertEquals(0, comparison.getMatchedCount());
		assertEquals(2, comparison.getDifferentCount());
		assertEquals(1, comparison.getXMissingCount());
		assertEquals(1, comparison.getYMissingCount());

		assertEquals(MatchResultType.NOT_EQUAL, results.get(0).getResultType());
		assertEquals(1L, results.get(0).getKey(0));
		
		assertEquals("Apple", results.get(0).getComparison(0).getX());
		assertEquals("Apple", results.get(0).getComparison(0).getY());
		assertEquals(0, results.get(0).getComparison(0).getResult());
		
		assertEquals(4, results.get(0).getComparison(1).getX());
		assertEquals(4, results.get(0).getComparison(1).getY());
		assertEquals(0, results.get(0).getComparison(1).getResult());
		
		assertEquals("green", results.get(0).getComparison(2).getX());
		assertEquals("red", results.get(0).getComparison(2).getY());
        assertTrue(results.get(0).getComparison(2).getResult() != 0);
		assertEquals("green<>red", results.get(0).getComparison(2).getSummaryText());
		
		assertEquals(MatchResultType.NOT_EQUAL, results.get(1).getResultType());
		assertEquals(2L, results.get(1).getKey(0));
		
		assertEquals(0, results.get(1).getComparison(0).getResult());
		
		assertEquals(3, results.get(1).getComparison(1).getX());
		assertEquals(4, results.get(1).getComparison(1).getY());
        assertTrue(results.get(1).getComparison(1).getResult() != 0);
		assertEquals("3<>4", results.get(1).getComparison(1).getSummaryText());
		
		assertEquals(0, results.get(1).getComparison(2).getResult());
		

		assertEquals(MatchResultType.X_MISSING, results.get(2).getResultType());
		assertEquals(3L, results.get(2).getKey(0));

		assertEquals(MatchResultType.Y_MISSING, results.get(3).getResultType());
		assertEquals(5L, results.get(3).getKey(0));

		MatcherAssert.assertThat(results.size(), Matchers.is(4));
	}
	
	@Test
	void testCompareListOfFruitKeyedOnNoneUniqueTypeWithAdditionalValue() {
		
		ArooaSession session = new StandardArooaSession();
		
		SharedTestData testData = new SharedTestData();
		
		IterableBeansComparerType<Fruit> test =
				new IterableBeansComparerType<>();
		
		test.setArooaSession(session);
		
		test.setKeys(new String[] { "type" });
		test.setValues(new String[] { "quantity" });
		test.setOthers(new String[] { "colour" });
		
		MultiItemComparer<Iterable<Fruit>> comparer = test.createComparerWith(
				new DefaultComparersByType());
		
		MultiItemComparison<Iterable<Fruit>> comparison = 
				comparer.compare(testData.getListFruitX(), testData.getListFruitY());
		
		assertEquals(3, comparison.getComparedCount());
		assertEquals(1, comparison.getBreaksCount());
		assertEquals(2, comparison.getMatchedCount());
		assertEquals(1, comparison.getDifferentCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
		
	}
	
	@Test
    void testCompareEqualListsOfStrings() {
		
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
		assertEquals("", comparison.getSummaryText());
		
		assertEquals(3, comparison.getComparedCount());
		assertEquals(0, comparison.getBreaksCount());
		assertEquals(3, comparison.getMatchedCount());
		assertEquals(0, comparison.getDifferentCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}
}
