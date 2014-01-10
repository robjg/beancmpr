package org.oddjob.beancmpr.comparers;

import java.util.Arrays;

/**
 * Compares to Arrays of Objects.
 * 
 * @author rob
 *
 */
public class ArrayComparer 
implements HierarchicalComparer, MultiItemComparer<Object[]>{

	private ComparersByType comparers;
	
	private ComparersByType parentComparers;
	
	public void setComparersByType(ComparersByType comparersByType) {
		this.comparers = comparersByType;
	}

	@Override
	public void injectComparers(ComparersByType comparersByType) {
		this.parentComparers = comparersByType;
		
	}
	
	@Override
	public MultiItemComparison<Object[]> compare(final Object[] x, final Object[] y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
	
		IterableComparer<Object> iterableComparer = 
				new IterableComparer<Object>();
		iterableComparer.setComparersByType(comparers);
		iterableComparer.injectComparers(parentComparers);
		
		final MultiItemComparison<Iterable<? extends Object>> iterableComparison = 
				iterableComparer.compare(Arrays.asList(x), Arrays.asList(y));
		
		return new DelegatingMultiItemComparison<Object[]>(x, y, iterableComparison);
	}
	
	@Override
	public Class<?> getType() {
		return Object[].class;
	}	
}
