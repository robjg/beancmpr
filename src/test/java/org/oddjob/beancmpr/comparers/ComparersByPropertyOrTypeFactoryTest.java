package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.beans.ComparerProvider;
import org.oddjob.beancmpr.beans.ComparersByPropertyOrTypeFactory;

public class ComparersByPropertyOrTypeFactoryTest extends TestCase {

	public void testSpecialisedByTypeRetrieval() throws ArooaConversionException {
		
		ComparersByTypeList list = new ComparersByTypeList();
		list.setClassLoader(getClass().getClassLoader());
		
		NumericComparer numericComparer = new NumericComparer();
		numericComparer.setPercentageTolerance(100);
		
		list.setSpecialisations("double", numericComparer);
		
		ComparersByPropertyOrTypeFactory test = 
				new ComparersByPropertyOrTypeFactory(null, list.toValue());
		
		ComparerProvider provider = test.createWith(null);
		
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
