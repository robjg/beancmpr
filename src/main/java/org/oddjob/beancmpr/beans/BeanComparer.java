package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.HierarchicalComparer;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparer;
import org.oddjob.beancmpr.matchables.MatchableComparerFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;

/**
 * A comparer that is able to compare two beans.
 * 
 * @author rob
 *
 */
public class BeanComparer implements Comparer<Object>, HierarchicalComparer {

	private final PropertyAccessor accessor;
	
	private final String[] valueProperties;
	
	private final ComparerProviderFactory comparerProviderFactory;
	
	private ComparerProvider comparerProvider;
	
	private MatchableFactory<Object> matchableFactory;
	
	private MatchableComparer comparer;
	
	/**
	 * Create a new instance.
	 * 
	 * @param valueProperties
	 * @param accessor
	 * @param comparerProvider
	 */
	public BeanComparer(String[] valueProperties,
			PropertyAccessor accessor,
			ComparerProviderFactory comparerProviderFactory) {
		this.valueProperties = valueProperties;
		this.accessor = accessor;
		this.comparerProviderFactory = comparerProviderFactory;
	}
	
	@Override
	public void injectComparers(ComparersByType parentComparers) {
		comparerProvider = comparerProviderFactory.createWith(
				parentComparers);
	}
	
	@Override
	public BeanComparison compare(Object x, Object y) {

		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}

		if (comparerProvider == null) {
			throw new IllegalStateException("Parent Comparers Not Yet Injected!");
		}
		
		if (matchableFactory == null) {

			String[] properties = 
					BeanComparer.this.valueProperties;

			if (properties == null) {
				properties = accessor.getBeanOverview(
						x.getClass()).getProperties();
			}

			matchableFactory = new BeanMatchableFactory(
					new SimpleMatchDefinition(
							null, properties, null), 
							accessor);
		}

		Matchable matchableX = 
			matchableFactory.createMatchable(x);
		Matchable matchableY = 
			matchableFactory.createMatchable(y);

		if (comparer == null) {
			comparer = 
				new MatchableComparerFactory(
						comparerProvider).createComparerFor(
								matchableX.getMetaData(), 
								matchableY.getMetaData());							
		}
		
		return new BeanComparison(x, y, 
				comparer.compare(matchableX, matchableY));
	}					
	
	@Override
	public Class<Object> getType() {
		return Object.class;
	}
	
}
