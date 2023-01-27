package org.oddjob.beancmpr.composite;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.comparers.MockComparer;

public class CompositeComparersByTypeTest extends TestCase {

	private static class OurComparerFactory
	implements ComparerFactory<Integer> {

		@Override
		public OurComparer createComparerWith(ComparersByType comparersByType) {
			return new OurComparer();
		}
	}
	
	private static class OurComparer extends MockComparer<Integer> {
		
		@Override
		public Class<?> getType() {
			return Integer.class;
		}
	}
	
	@Test
	void testCustomOverridesDefault() {
		
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
