package org.oddjob.beancmpr.beans;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.Iterables;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

public class BeanComparerTest extends TestCase {

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
	
	public void testCompareTwoBeans() {
		
		BeanComparer comparer = new BeanComparer(
				new String[] { "type", "quantity" },
				new BeanUtilsPropertyAccessor(),
				new ComparersByPropertyOrType());
		
		Fruit beanX = new Fruit();
		beanX.setType("apple");
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setType("apple");
		beanY.setQuantity(3);
		
		MultiValueComparison<Object> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(false, comparison.getResult() == 0);
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(false, comparisons[1].getResult() == 0);
	}
	
	public void testCompareBothNullValues() {
		
		BeanComparer comparer = new BeanComparer(
				new String[] { "type", "quantity" },
				new BeanUtilsPropertyAccessor(),
				new ComparersByPropertyOrType());
		
		Fruit beanX = new Fruit();
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setQuantity(2);
		
		MultiValueComparison<Object> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(0, comparison.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(0, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
	}
	
	public void testCompareOneNullValues() {
		
		BeanComparer comparer = new BeanComparer(
				new String[] { "type", "quantity" },
				new BeanUtilsPropertyAccessor(),
				new ComparersByPropertyOrType());
		
		Fruit beanX = new Fruit();
		beanX.setType("apple");
		beanX.setQuantity(2);
		
		Fruit beanY = new Fruit();
		beanY.setQuantity(2);
		
		MultiValueComparison<Object> comparison = 
				comparer.compare(beanX, beanY);
		
		assertEquals(-1, comparison.getResult());
		
		Comparison<?>[] comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class);
		
		assertEquals(2, comparisons.length);
		
		assertEquals(-1, comparisons[0].getResult());
		assertEquals(0, comparisons[1].getResult());
	}
}
