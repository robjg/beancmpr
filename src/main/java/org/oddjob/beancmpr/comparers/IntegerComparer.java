package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * A {@link Comparer} for integers.
 * <p>
 * 
 * @author Rob
 *
 */
public class IntegerComparer implements Comparer<Integer> {

	public Comparison<Integer> compare(final Integer x, final Integer y) {

		if (x == null || y == null) {
			throw new NullPointerException("X or Y Null.");
		}

		return new Comparison<Integer>() {
			
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
			public Integer getX() {
				return x;
			}
			
			@Override
			public Integer getY() {
				return y;
			}
		};
	}
	
	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}
	
	public String toString() {
		return getClass().getName(); 
	}
}
