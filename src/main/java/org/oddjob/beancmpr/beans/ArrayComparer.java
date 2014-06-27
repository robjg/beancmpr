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
public class ArrayComparer<T> 
implements MultiItemComparer<T[]>{

	private final ComparersByType comparers;

	public ArrayComparer(ComparersByType comparers) {
		if (comparers == null) {
			throw new NullPointerException("No comparers");
		}
		this.comparers = comparers;
	}
	
	@Override
	public MultiItemComparison<T[]> compare(final T[] x, final T[] y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
	
		IterableComparer<T> iterableComparer = 
				new IterableComparer<T>(comparers);
		
		Iterable<T> itX = Arrays.asList(x);
		Iterable<T>	itY = Arrays.asList(y);
		
		final MultiItemComparison<Iterable<T>> iterableComparison = 
				iterableComparer.compare(itX, itY);
		
		return new DelegatingMultiItemComparison<T[]>(x, y, iterableComparison);
	}
	
	@Override
	public Class<?> getType() {
		return Object[].class;
	}	
}
