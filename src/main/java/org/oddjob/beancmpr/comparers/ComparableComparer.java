package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * A generic {@link Comparer} for comparables.
 * <p>
 * 
 * @author Rob
 *
 */
public class ComparableComparer<T extends Comparable<T>> 
implements Comparer<T> {

	public Comparison<T> compare(final T x, final T y) {

		if (x == null || y == null) {
			throw new NullPointerException("X or Y Null.");
		}

		return new Comparison<T>() {
			
			final int result = x.compareTo(y);
			final String summary = result == 0 ? "" : "" + x + "<>" + y;
			
			@Override
			public int getResult() {
				return result;
			}
			
			@Override
			public String getSummaryText() {
				return summary;
			}
			
			@Override
			public T getX() {
				return x;
			}
			
			@Override
			public T getY() {
				return y;
			}
		};
	}
	
	@Override
	public Class<?> getType() {
		return Comparable.class;
	}
	
	public String toString() {
		return getClass().getName(); 
	}
}
