package org.oddjob.beancmpr.composite;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.MockComparer;
import org.oddjob.beancmpr.composite.ComparersByTypeList;
import org.oddjob.beancmpr.composite.CompositeComparersByType;
import org.oddjob.beancmpr.composite.DefaultComparersByType;

import junit.framework.TestCase;

public class CompositeComparersByTypeTest extends TestCase {

	private class OurComparerFactory 
	implements ComparerFactory<OurComparer> {

		@Override
		public OurComparer createComparerWith(ComparersByType comparersByType) {
			return new OurComparer();
		}
	}
	
	private class OurComparer extends MockComparer<Integer> {
		
		@Override
		public Class<?> getType() {
			return Integer.class;
		}
	}
	
	public void testCustomOverridesDefault() throws ArooaConversionException {
		
		ComparersByTypeList comparerList = new ComparersByTypeList();
		comparerList.setSpecialisations(Integer.class.getName(), 
				new OurComparerFactory());
		
		CompositeComparersByType test = new CompositeComparersByType(
				comparerList.createComparersByTypeWith(
						new CompositeComparersByType()), 
				new DefaultComparersByType());
		
		Comparer<Integer> comparer = test.comparerFor(Integer.class);
		
		assertEquals(OurComparer.class, comparer.getClass());
	}
}
