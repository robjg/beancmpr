package org.oddjob.beancmpr.composite;

import junit.framework.TestCase;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.ArrayComparer;
import org.oddjob.beancmpr.beans.BeanArrayComparerType;
import org.oddjob.beancmpr.comparers.EqualityComparer;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.comparers.TextComparer;

public class ComparersByTypeListTest extends TestCase {

	private class MyType {
		
	}
	
	private class MyComparer implements Comparer<MyType> {
		
		@Override
		public Comparison<MyType> compare(MyType x, MyType y) {
			throw new RuntimeException("Unexepected");
		}
		
		@Override
		public Class<?> getType() {
			return MyType.class;
		}
	}
	
	public void testFindByType() throws ArooaConversionException {
		
		ComparersByTypeList test = new ComparersByTypeList();
		test.setClassLoader(getClass().getClassLoader());
		
		MyComparer comparer = new MyComparer();
		
		test.setSpecialisations(MyType.class.getName(), 
				new ComparerFactoryAdaptor<MyType>(comparer));
		
		ComparersByType comparersByType = 
				test.createComparersByTypeWith(null);
		
		assertSame(null, comparersByType.comparerFor(Object.class));
		assertSame(comparer, comparersByType.comparerFor(MyType.class));
		assertSame(null, comparersByType.comparerFor(String.class));
	}
	
	public void testDefaultComparersProvided() {
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		ComparersByType comparers = test.createComparersByTypeWith(
				new DefaultComparersByType());
		
		Comparer<String> textComparer = comparers.comparerFor(String.class);
		assertEquals(EqualityComparer.class, textComparer.getClass());
	}
	
	public void testFindBySuperType() throws ArooaConversionException {
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		test.setSpecialisations(Integer.class.getName(), 
				new ComparerFactoryAdaptor<Number>(
						new NumericComparer()));
		
		ComparersByType comparersByType = 
				test.createComparersByTypeWith(null);
		
		Comparer<Integer> comparer = comparersByType.comparerFor(Integer.class);
		
		assertEquals(Number.class, comparer.getType());
	}
	
	public void testRegisterBySubTypeOfSpecialisationFails() {
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		test.setSpecialisations(Object.class.getName(),
				new ComparerFactoryAdaptor<MyType>(new MyComparer()));
		
		try {
			test.createComparersByTypeWith(null);
			fail("Should fail.");
		} catch (IllegalArgumentException e) {
			// expected
			assertEquals("Can't add specialised comparer for " + 
					MyType.class + " as it is not assignable from " +
					Object.class,
					e.getMessage());
		}
		
	}
	
	public void testProvidesArrayComparerForArraysOfElementSubType() {
		
		TextComparer textComparer = new TextComparer();
		textComparer.setIgnoreCase(true);
		
		ComparersByTypeList arrayComparers = new ComparersByTypeList();
		arrayComparers.setSpecialisations(String.class.getName(), 
				new ComparerFactoryAdaptor<String>(textComparer));
		
		BeanArrayComparerType arrayComparer = new BeanArrayComparerType();
		arrayComparer.setComparersByType(arrayComparers);
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		test.setSpecialisations(Object[].class.getName(), arrayComparer);
		
		ComparersByType comparers = test.createComparersByTypeWith(
				new DefaultComparersByType());
		
		// sanity check that array comparer isn't being returned for everything
		Comparer<String> stringComparer = comparers.comparerFor(String.class);		
		assertEquals(EqualityComparer.class, stringComparer.getClass());
		
		Comparer<String[]> comparer = comparers.comparerFor(String[].class);
		
		assertEquals(ArrayComparer.class, comparer.getClass());
		
		Comparison<String[]> comparision = comparer.compare(
				new String[] { "GREEN" }, new String[] { "green" });
		
		assertEquals(0, comparision.getResult());
	}
}
