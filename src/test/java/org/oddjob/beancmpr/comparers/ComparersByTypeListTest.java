package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.ComparersByTypeList;

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
		
		test.setSpecialisations(MyType.class.getName(), comparer);
		
		ComparersByType comparersByType = test.toValue();
		
		assertSame(null, comparersByType.comparerFor(Object.class));
		assertSame(comparer, comparersByType.comparerFor(MyType.class));
		assertSame(null, comparersByType.comparerFor(String.class));
	}
	
	public void testFindBySuperType() throws ArooaConversionException {
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		test.setSpecialisations(Integer.class.getName(), new NumericComparer());
		
		ComparersByType comparersByType = test.toValue();
		
		Comparer<Integer> comparer = comparersByType.comparerFor(Integer.class);
		
		assertEquals(Number.class, comparer.getType());
	}
	
	public void testregisterBySubTypeFails() {
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		test.setSpecialisations(Object.class.getName(), new MyComparer());
		
		try {
			test.toValue();
			fail("Should fail.");
		} catch (ArooaConversionException e) {
			// expected
		}
		
	}
}
