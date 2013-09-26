package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;

import junit.framework.TestCase;

public class CompositeComparersByTypeTest extends TestCase {

	private class OurComparer extends MockComparer<Integer> {
		
		@Override
		public Class<?> getType() {
			return Integer.class;
		}
	}
	
	public void testCustomOverridesDefault() {
		
		ComparersByTypeList comparerList = new ComparersByTypeList();
		comparerList.setComparer(0, new OurComparer());
		
		CompositeComparersByType test = new CompositeComparersByType(
				comparerList, new DefaultComparersByType());
		
		Comparer<Integer> comparer = test.comparerFor(Integer.class);
		
		assertEquals(OurComparer.class, comparer.getClass());
	}
}
