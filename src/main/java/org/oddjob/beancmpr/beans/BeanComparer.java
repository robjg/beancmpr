package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.DefaultMatchableComparer;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparer;
import org.oddjob.beancmpr.matchables.MatchableComparison;
import org.oddjob.beancmpr.matchables.MatchableFactory;

/**
 * A comparer that is able to compare two beans.
 * 
 * @author rob
 *
 */
public class BeanComparer implements Comparer<Object> {

	private final PropertyAccessor accessor;
	
	private final String[] valueProperties;
	
	private final BeanComparerProvider comparerProvider;
	
	private MatchableFactory<Object> matchableFactory;
	
	private MatchableComparer comparer;
	
	public BeanComparer(String[] valueProperties,
			PropertyAccessor accessor,
			BeanComparerProvider comparerProvider) {
		this.valueProperties = valueProperties;
		this.accessor = accessor;
		this.comparerProvider = comparerProvider;
	}
	
	
	@Override
	public MatchableComparison compare(Object x, Object y) {

		if (x == null || y == null) {
			return null;
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

			comparer = 
				new DefaultMatchableComparer(comparerProvider);							
		}

		Matchable matchableX = 
			matchableFactory.createMatchable(x);
		Matchable matchableY = 
			matchableFactory.createMatchable(y);

		return comparer.compare(matchableX, matchableY);
	}					
	
	@Override
	public Class<Object> getType() {
		return Object.class;
	}
	
}
