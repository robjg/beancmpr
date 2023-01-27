package org.oddjob.beancmpr.composite;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.comparers.NumericComparer;

class ComparersByPropertyOrTypeFactoryTest extends TestCase {

	@Test
	void testSpecialisedByTypeRetrieval() {
		
		ComparersByTypeList list = new ComparersByTypeList();
		list.setClassLoader(getClass().getClassLoader());
		
		NumericComparer numericComparer = new NumericComparer();
		numericComparer.setPercentageTolerance(100);
		
		list.setSpecialisations("double",
				new ComparerFactoryAdaptor<>(numericComparer));
		
		ComparersByNameOrTypeFactory test = 
				new ComparersByNameOrTypeFactory(null, list);
		
		BeanPropertyComparerProvider provider = test.createWith(null);
		
		Comparer<Long> comparerLong = provider.comparerFor(null, Long.class);
		
		assertEquals(Comparable.class, comparerLong.getType());
		
		Comparer<? super Double> comparerDouble = 
				provider.comparerFor(null, Double.class);
		
		assertEquals(100.0, ((NumericComparer) 
				comparerDouble).getPercentageTolerance(), 0.1);
		
		Comparer<? super Float> comparerFloat = 
				provider.comparerFor(null, Float.class);
		
		assertEquals(0.0, ((NumericComparer) 
				comparerFloat).getPercentageTolerance(), 0.1);
	}
}
