package org.oddjob.beancmpr.multiitem;


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
