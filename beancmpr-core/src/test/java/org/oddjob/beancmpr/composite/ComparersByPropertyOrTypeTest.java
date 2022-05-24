package org.oddjob.beancmpr.composite;

import junit.framework.TestCase;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.BeanComparerType;
import org.oddjob.beancmpr.comparers.MockComparison;

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
	
	public void testThatTypeComparersInjectedIntoPropertyComparers() throws ArooaConversionException {
		
		ArooaSession session = new StandardArooaSession();
		
		BeanComparerType<Object> beanComparer = new BeanComparerType<>();
		beanComparer.setValues(new String[] { "quantity" }); 
		beanComparer.setArooaSession(session);
		
		ComparersByNameType comparersByNameType =
				new ComparersByNameType();
		comparersByNameType.setComparers("fruit", beanComparer);

		ComparersByTypeList comparersByTypeList = new ComparersByTypeList();
		comparersByTypeList.setSpecialisations("java.lang.Integer", 
				new ComparerFactoryAdaptor<Integer>(
						new OurIntegercomparer()));
		
		ComparersByType comparersByType = 
				comparersByTypeList.createComparersByTypeWith(null);

		ComparersByName comparersByName =
				comparersByNameType.createComparersByNameWith(
						comparersByType);
		
		ComparersByNameOrType test = 
				new ComparersByNameOrType(
						comparersByName, null);
		
		Comparer<Object> comparer = test.comparerFor("fruit", Object.class);
		
		Comparison<Object> comparison = comparer.compare(new Fruit(5), new Fruit(10));
		
		assertEquals(0, comparison.getResult());
	}
}
