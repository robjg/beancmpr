package org.oddjob.beancmpr.multiitem;

import org.oddjob.beancmpr.Comparison;


/**
 * A {@link Comparison} that is the result of comparing many things.
 * 
 * @author rob
 *
 */
public class MultiItemComparisonFromCounts<T> 
implements MultiItemComparison<T> {

	private final T x;
	
	private final T y;
	
	private final MultiItemComparisonCounts multiItemStats;
	
	private final int result;
	
	private final String summaryText;
	
	public MultiItemComparisonFromCounts(
			T x,
			T y,
			MultiItemComparisonCounts multiItemStats) {
		
		this.x = x;
		this.y = y;
		
		this.multiItemStats = multiItemStats;
		
		this.result = multiItemStats.getBreaksCount();
		
		if (this.result == 0) {
			this.summaryText = "";
		}
		else {
			this.summaryText = "" + multiItemStats.getBreaksCount() +
					" differences";
		}
	}

	@Override
	public T getX() {
		return x;
	}
	
	@Override
	public T getY() {
		return y;
	}
	
	@Override
	public int getResult() {
		return result;
	}
	
	@Override
	public String getSummaryText() {
		return summaryText;
	}
	
	@Override
	public int getXMissingCount() {
		return multiItemStats.getXMissingCount();
	}
	
	@Override
	public int getYMissingCount() {
		return multiItemStats.getYMissingCount();
	}
	
	@Override
	public int getMatchedCount() {
		return multiItemStats.getMatchedCount();
	}
	
	@Override
	public int getDifferentCount() {
		return multiItemStats.getDifferentCount();
	}
	
	@Override
	public int getBreaksCount() {
		return multiItemStats.getBreaksCount();
	}
	
	@Override
	public int getComparedCount() {
		return multiItemStats.getComparedCount();
	}
}
