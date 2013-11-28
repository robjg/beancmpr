package org.oddjob.beancmpr.comparers;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.Comparer;

import junit.framework.TestCase;

public class CompositeComparersByTypeTest extends TestCase {

	private class OurComparer extends MockComparer<Integer> {
		
		@Override
		public Class<?> getType() {
			return Integer.class;
		}
	}
	
	public void testCustomOverridesDefault() throws ArooaConversionException {
		
		ComparersByTypeList comparerList = new ComparersByTypeList();
		comparerList.setSpecialisations(Integer.class.getName(), new OurComparer());
		
		CompositeComparersByType test = new CompositeComparersByType(
				comparerList.toValue(), new DefaultComparersByType());
		
		Comparer<Integer> comparer = test.comparerFor(Integer.class);
		
		assertEquals(OurComparer.class, comparer.getClass());
	}
}
