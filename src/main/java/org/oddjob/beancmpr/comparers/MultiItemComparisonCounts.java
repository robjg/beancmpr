package org.oddjob.beancmpr.comparers;


/**
 * Stats.
 * 
 * @author rob
 *
 */
public interface MultiItemComparisonCounts {
	
	public int getXMissingCount();
	
	public int getYMissingCount();
		
	public int getDifferentCount();
	
	public int getMatchedCount();
	
	public int getBreaksCount();
	
	public int getComparedCount();
	
}
