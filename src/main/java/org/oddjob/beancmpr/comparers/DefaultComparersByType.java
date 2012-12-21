package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;

/**
 * Default {@link Comparer}s.
 * 
 * @author rob
 *
 */
public class DefaultComparersByType implements ComparersByType {

	private final ComparersByTypeList comparers = 
		new ComparersByTypeList();
	
	public DefaultComparersByType() {
		
		int index = 0;
		
		comparers.setComparer(index++, new NumericComparer());		
		comparers.setComparer(index++, new ArrayComparer());
		comparers.setComparer(index++, new SimpleIterableComparer());
		comparers.setComparer(index++, new EqualsComparer());
	}
		
	@Override
	public <T> Comparer<T> comparerFor(Class<T> type) {
		return comparers.comparerFor(type);
	}
	
	@Override
	public void injectComparers(ComparersByType comparers) {
		this.comparers.injectComparers(comparers);
	}
}
