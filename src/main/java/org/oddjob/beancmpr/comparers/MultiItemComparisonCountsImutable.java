package org.oddjob.beancmpr.comparers;

import java.io.Serializable;


/**
 * Stats.
 * 
 * @author rob
 *
 */
public class MultiItemComparisonCountsImutable 
implements MultiItemComparisonCounts, Serializable {
	
	private static final long serialVersionUID = 2014010600L;
	
	private final int xMissingCount;
	
	private final int yMissingCount;
		
	private final int differentCount;
	
	private final int matchedCount;
	
	private final int breaksCount;
	
	private final int comparedCount;
	
	public MultiItemComparisonCountsImutable(
			int xMissingCount,	
			int yMissingCount,		
			int differentCount,
			int matchedCount,
			int breaksCount,
			int comparedCount) {
		
		this.xMissingCount = xMissingCount;	
		this.yMissingCount = yMissingCount;		
		this.differentCount = differentCount;
		this.matchedCount = matchedCount;
		this.breaksCount = breaksCount;
		this.comparedCount = comparedCount;
	}
	
	public int getXMissingCount() {
		return xMissingCount;
	}
	
	public int getYMissingCount() {
		return yMissingCount;
	}
		
	public int getDifferentCount() {
		return differentCount;
	}
	
	public int getMatchedCount() {
		return matchedCount;
	}
	
	public int getBreaksCount() {
		return breaksCount;
	}
	
	public int getComparedCount() {
		return comparedCount;
	}
	
}
