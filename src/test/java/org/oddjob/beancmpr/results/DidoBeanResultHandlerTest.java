package org.oddjob.beancmpr.results;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.BeanCompareJob;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.SharedTestData;

public class DidoBeanResultHandlerTest extends TestCase {

	public void testCreateResultsAllDifferent() {
		
		SharedTestData testData = new SharedTestData();
				
		List<Object> results = new ArrayList<Object>();
		
		BeanCompareJob beanCompare = new BeanCompareJob();
		beanCompare.setArooaSession(new StandardArooaSession());
		beanCompare.setInX(testData.fruitX);
		beanCompare.setInY(testData.fruitY);
		beanCompare.setKeyProperties(new String[] {"type"});
		beanCompare.setValueProperties(new String[] {"quantity", "price"});
		beanCompare.setResults(new DidoBeanResultHandler());
		beanCompare.acceptDestination(results);
		
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
		
		SharedTestData testData = new SharedTestData();
				
		List<Object> results = new ArrayList<Object>();
		
		BeanCompareJob beanCompare = new BeanCompareJob();
		beanCompare.setArooaSession(new StandardArooaSession());
		beanCompare.setInX(testData.fruitX);
		beanCompare.setInY(testData.fruitY);
		beanCompare.setKeyProperties(new String[] {"id"});
		beanCompare.setValueProperties(new String[] {"type"});
		beanCompare.setResults(new DidoBeanResultHandler());
		beanCompare.acceptDestination(results);
		
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
