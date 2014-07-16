package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.multiitem.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

/**
 * Compares to Arrays of Objects.
 * 
 * @see ArrayComparerFactory
 * 
 * @author rob
 *
 */
public class ArrayComparer 
implements MultiItemComparer<Object>{

	/** Comparers for the element comparison. */
	private final ComparersByType comparers;

	public ArrayComparer(ComparersByType comparers) {
		if (comparers == null) {
			throw new NullPointerException("No comparers");
		}
		this.comparers = comparers;
	}
	
	@Override
	public MultiItemComparison<Object> compare(final Object x, final Object y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
	
		IterableComparer<Object> iterableComparer = 
				new IterableComparer<>(comparers);

		Iterable<Object> xIt = new ArrayIterable(x);
		Iterable<Object> yIt = new ArrayIterable(y);
		
		final MultiItemComparison<Iterable<Object>> iterableComparison = 
				iterableComparer.compare(xIt, yIt);
		
		return new DelegatingMultiItemComparison<Object>(x, y, iterableComparison);
	}
	
	@Override
	public Class<?> getType() {
		return Object.class;
	}	
}
