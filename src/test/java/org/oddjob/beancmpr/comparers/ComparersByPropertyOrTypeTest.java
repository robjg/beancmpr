package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.BeanComparer;
import org.oddjob.beancmpr.beans.ComparersByPropertyMap;
import org.oddjob.beancmpr.beans.ComparersByPropertyOrType;
import org.oddjob.beancmpr.beans.ComparersByPropertyOrTypeFactory;

public class ComparersByPropertyOrTypeTest extends TestCase {

	public static class Fruit {
		
		private final int quantity;
		
		Fruit(int quantity) {
			this.quantity = quantity;
		}
		
		public int getQuantity() {
			return quantity;
		}
	}
	
	private class OurIntegercomparer implements Comparer<Integer> {

		@Override
		public Comparison<Integer> compare(Integer x, Integer y) {
			return new MockComparison<Integer>() {
				@Override
				public int getResult() {
					return 0;
				}
			};
		}
		
		@Override
		public Class<?> getType() {
			return Integer.class;
		}
	}
	
	public void testThatTypeComparersInjectedIntoPropertyComparers() {
		
		BeanComparer beanComparer = new BeanComparer(
				new String[] { "quantity" }, new BeanUtilsPropertyAccessor(), 
				new ComparersByPropertyOrTypeFactory(null, null));
		
		ComparersByPropertyMap comparersByProperty =
				new ComparersByPropertyMap();
		comparersByProperty.setComparerForProperty("fruit", 
				beanComparer);

		ComparersByTypeList comparersByType = new ComparersByTypeList();
		comparersByType.setComparer(0, new OurIntegercomparer());
		
		beanComparer.injectComparers(comparersByType);
		
		ComparersByPropertyOrType test = 
				new ComparersByPropertyOrType(
						comparersByProperty, null);
		
		Comparer<Object> comparer = test.comparerFor("fruit", Object.class);
		
		Comparison<Object> comparison = comparer.compare(new Fruit(5), new Fruit(10));
		
		assertEquals(0, comparison.getResult());
	}
}
