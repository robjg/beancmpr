package org.oddjob.beancmpr.composite;

import junit.framework.TestCase;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.composite.ComparersByNameOrTypeFactory;
import org.oddjob.beancmpr.composite.ComparersByTypeList;

public class ComparersByPropertyOrTypeFactoryTest extends TestCase {

	public void testSpecialisedByTypeRetrieval() throws ArooaConversionException {
		
		ComparersByTypeList list = new ComparersByTypeList();
		list.setClassLoader(getClass().getClassLoader());
		
		NumericComparer numericComparer = new NumericComparer();
		numericComparer.setPercentageTolerance(100);
		
		list.setSpecialisations("double", 
				new ComparerFactoryAdaptor<Comparer<?>>(numericComparer));
		
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
