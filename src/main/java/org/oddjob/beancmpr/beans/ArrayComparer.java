package org.oddjob.beancmpr.beans;

import java.util.Arrays;

import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.multiitem.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

/**
 * Compares to Arrays of Objects.
 * 
 * @author rob
 *
 */
public class ArrayComparer 
implements MultiItemComparer<Object[]>{

	private final ComparersByType comparers;

	public ArrayComparer(ComparersByType comparers) {
		if (comparers == null) {
			throw new NullPointerException("No comparers");
		}
		this.comparers = comparers;
	}
	
	@Override
	public MultiItemComparison<Object[]> compare(final Object[] x, final Object[] y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
	
		IterableComparer<Object> iterableComparer = 
				new IterableComparer<Object>(comparers);
		
		final MultiItemComparison<Iterable<? extends Object>> iterableComparison = 
				iterableComparer.compare(Arrays.asList(x), Arrays.asList(y));
		
		return new DelegatingMultiItemComparison<Object[]>(x, y, iterableComparison);
	}
	
	@Override
	public Class<?> getType() {
		return Object[].class;
	}	
}
