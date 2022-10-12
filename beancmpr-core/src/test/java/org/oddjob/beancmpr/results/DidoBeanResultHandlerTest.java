package org.oddjob.beancmpr.results;

import junit.framework.TestCase;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.BeanCompareJob;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.SharedTestData;
import org.oddjob.beancmpr.SharedTestData.Fruit;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DidoBeanResultHandlerTest extends TestCase {

	public void testCreateResultsAllDifferent() {
		
		SharedTestData testData = new SharedTestData();
				
		List<Object> results = new ArrayList<>();
		
		IterableBeansComparerType<Fruit> iterableComparer = 
				new IterableBeansComparerType<>();
		iterableComparer.setArooaSession(new StandardArooaSession());
		iterableComparer.setKeys(new String[] {"type"});
		iterableComparer.setValues(new String[] {"quantity", "price"});
				
		BeanCompareJob<Iterable<Fruit>> beanCompare = new BeanCompareJob<>();
		beanCompare.setInX(testData.getListFruitX());
		beanCompare.setInY(testData.getListFruitY());
		beanCompare.setResults(new DidoBeanResultHandler());
		beanCompare.acceptDestination(results::add);
		beanCompare.setComparer(iterableComparer);
		
		beanCompare.run();
		
		assertEquals(3, results.size());
		
		DidoResultBean bean = (DidoResultBean) results.get(0);

		assertEquals(3, bean.getResultType());
		Map<String, Object> keys = bean.getKeys();
		
		assertEquals(1, keys.size());
		assertEquals("Apple", keys.get("type"));

		Map<String, Comparison<?>> comparisons = bean.getComparisons();

		assertEquals(2, comparisons.size());
		
		Object[] properties = comparisons.keySet().toArray();
		assertEquals("quantity", properties[0]);
		assertEquals("price", properties[1]);
		
		// second 
		bean = (DidoResultBean) results.get(1);

		assertEquals(3, bean.getResultType());
		
		// third
		bean = (DidoResultBean) results.get(2);

		assertEquals(3, bean.getResultType());
	}
	
	public void testSomeMissing() {

		ArooaSession session = new StandardArooaSession();
		
		SharedTestData testData = new SharedTestData();
				
		List<Object> results = new ArrayList<>();
		
		IterableBeansComparerType<Fruit> iterableComparer = 
				new IterableBeansComparerType<>();
		iterableComparer.setArooaSession(session);
		iterableComparer.setKeys(new String[] {"id"});
		iterableComparer.setValues(new String[] {"type"});
				
		BeanCompareJob<Iterable<Fruit>> beanCompare = new BeanCompareJob<>();
		beanCompare.setArooaSession(session);
		beanCompare.setInX(testData.getListFruitX());
		beanCompare.setInY(testData.getListFruitY());
		beanCompare.setResults(new DidoBeanResultHandler());
		beanCompare.acceptDestination(results::add);
		beanCompare.setComparer(iterableComparer);
		
		beanCompare.run();
		
		assertEquals(4, results.size());
		
		DidoResultBean bean = (DidoResultBean) results.get(0);

		assertEquals(0, bean.getResultType());
		Map<String, Comparison<?>> comparisons = bean.getComparisons();
		
		assertEquals(1, comparisons.size());
		assertEquals("Apple", comparisons.get("type").getX());

		// second 
		bean = (DidoResultBean) results.get(1);
	
		assertEquals(0, bean.getResultType());
		
		comparisons = bean.getComparisons();
		
		assertEquals("Banana", comparisons.get("type").getY());
		
		// third
		bean = (DidoResultBean) results.get(2);

		assertEquals(1, bean.getResultType());
		
		// fourth
		bean = (DidoResultBean) results.get(3);

		assertEquals(2, bean.getResultType());
	}
	
}
