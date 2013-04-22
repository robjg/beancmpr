package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;


/**
 * A {@link Comparison} that is the result of comparing many things.
 * 
 * @author rob
 *
 */
public class IterableComparison<T> 
implements Comparison<Iterable<? extends T>>, MultiItemComparisonCounts {

	private final Iterable<? extends T> x;
	
	private final Iterable<? extends T> y;
	
	private final MultiItemComparisonCounts multiItemStats;
	
	public IterableComparison(
			Iterable<? extends T> x,
			Iterable<? extends T> y,
			MultiItemComparisonCounts multiItemStats) {
		
		this.x = x;
		this.y = y;
		
		this.multiItemStats = multiItemStats;
	}
	
	@Override
	public Iterable<? extends T> getX() {
		return x;
	}
	
	@Override
	public Iterable<? extends T> getY() {
		return y;
	}
	
	@Override
	public int getResult() {
		int result = multiItemStats.getXMissingCount() - 
				multiItemStats.getYMissingCount();
		
		if (result != 0) {
			return result;
		}
		else if (multiItemStats.getBreaksCount() == 0 ) {
			return 0;
		}
		else {
			return -1;
		}
	}
	
	@Override
	public String getSummaryText() {
		if (getResult() == 0) {
			return "Equal, " + multiItemStats.getMatchedCount() + 
					" matched";
		}
		else {
			return "" + multiItemStats.getBreaksCount() +
			  " differences";
		}
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