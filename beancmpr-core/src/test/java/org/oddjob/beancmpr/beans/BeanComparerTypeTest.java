package org.oddjob.beancmpr.beans;

import org.junit.jupiter.api.Test;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

class BeanComparerTypeTest extends TestCase {

	public static class Fruit {
		
		private String type;
		
		private int quantity;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
	}
	
	@Test
	void testCompareTwoBeansWithTwoProperties() {
		
		ArooaSession session = new StandardArooaSession();
	
		BeanComparerType<Fruit> test = new BeanComparerType<>();
		test.setArooaSession(session);
		test.setValues(new String[] { "type", "quantity" });
		
		BeanComparer<Fruit> comparer = test.createComparerWith(null);
				
		Fruit beanX = new Fruit();
		beanX.setType("apple");
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setType("apple");
		beanY.setQuantity(3);
		
		BeanComparison<Fruit> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(false, comparison.getResult() == 0);
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(false, comparisons[1].getResult() == 0);
		
		assertEquals(1, comparison.getComparedCount());
		assertEquals(1, comparison.getBreaksCount());
		assertEquals(1, comparison.getDifferentCount());
		assertEquals(0, comparison.getMatchedCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
	}
	
	@Test
	void testCompareTwoBeansWithNoPropertiesSpecified() {
		
		ArooaSession session = new StandardArooaSession();
	
		BeanComparerType<Fruit> test = new BeanComparerType<>();
		test.setArooaSession(session);
		
		BeanComparer<Fruit> comparer = test.createComparerWith(null);
				
		Fruit beanX = new Fruit();
		beanX.setType("apple");
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setType("apple");
		beanY.setQuantity(2);
		
		BeanComparison<Fruit> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(false, comparison.getResult() == 0);
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(1, comparisons.length);
		
		assertEquals(false, comparisons[0].getResult() == 0);
		
		assertEquals(1, comparison.getComparedCount());
		assertEquals(1, comparison.getBreaksCount());
		assertEquals(1, comparison.getDifferentCount());
		assertEquals(0, comparison.getMatchedCount());
		assertEquals(0, comparison.getXMissingCount());
		assertEquals(0, comparison.getYMissingCount());
		
		// Test when same bean
		
		comparison = 
				comparer.compare(beanX, beanX);
		
		assertEquals(true, comparison.getResult() == 0);
		
		comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(1, comparisons.length);
		
		assertEquals(true, comparisons[0].getResult() == 0);
	}
	
	@Test
	void testCompareEachBeanHasSamePropertyWithANullValue() {
		
		ArooaSession session = new StandardArooaSession();
		
		BeanComparerType<Fruit> test = new BeanComparerType<>();
		test.setArooaSession(session);
		test.setValues(new String[] { "type", "quantity" });
		
		BeanComparer<Fruit> comparer = test.createComparerWith(null);
		
		Fruit beanX = new Fruit();
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setQuantity(2);
		
		MultiValueComparison<Fruit> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(0, comparison.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
	}
	
	@Test
	void testCompareOneBeanHasOneNullValue() {
		
		ArooaSession session = new StandardArooaSession();
		
		BeanComparerType<Fruit> test = new BeanComparerType<>();
		test.setArooaSession(session);
		test.setValues(new String[] { "type", "quantity" });
				
		BeanComparer<Fruit> comparer = test.createComparerWith(null);
		
		Fruit beanX = new Fruit();
		beanX.setType("apple");
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setType(null);
		beanY.setQuantity(2);
		
		MultiValueComparison<Fruit> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(1, comparison.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(1, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
	}
	
	@Test
	void testWhenCompareValuesAreNullThrowNullPointerException() {
		
		ArooaSession session = new StandardArooaSession();
		
		BeanComparerType<Fruit> test = new BeanComparerType<>();
		test.setArooaSession(session);
		
		BeanComparer<Fruit> comparer = test.createComparerWith(null);
		
		try {
			comparer.compare(null, null);
			fail("Should throw NPE.");
		}
		catch (NullPointerException e) {
			// expected
		}
	}
}
