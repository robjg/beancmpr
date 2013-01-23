package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;


/**
 * A {@link Comparison} that is the result of comparing many things.
 * 
 * @author rob
 *
 */
public class MultiItemComparison<T> implements Comparison<Iterable<T>> {

	private final Iterable<T> x;
	
	private final Iterable<T> y;
	
	private final int xsMissing;
	
	private final int ysMissing;

	private final int different;
	
	private final int same;
		
	public MultiItemComparison(
			Iterable<T> x,
			Iterable<T> y,
			int xsMissing,
			int ysMissing,
			int different,
			int same) {
		
		this.x = x;
		this.y = y;
		
		this.xsMissing = xsMissing;
		this.ysMissing = ysMissing;
		this.different = different;
		this.same = same;
	}
	
	@Override
	public Iterable<T> getX() {
		return x;
	}
	
	@Override
	public Iterable<T> getY() {
		return y;
	}
	
	@Override
	public int getResult() {
		int result = xsMissing - ysMissing;
		
		if (result != 0) {
			return result;
		}
		else if (different == 0 ) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	@Override
	public String getSummaryText() {
		if (getResult() == 0) {
			return "Equal, " + same + " matched";
		}
		else {
			return "" + (xsMissing + ysMissing + different) +
			  " differences";
		}
	}
	
	public int getXsMissing() {
		return xsMissing;
	}
	
	public int getYsMissing() {
		return ysMissing;
	}
	
	public int getDifferent() {
		return different;
	}
	
	public int getSame() {
		return same;
	}
}
