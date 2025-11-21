package org.oddjob.beancmpr.multiitem;


/**
 * Stats.
 * 
 * @author rob
 *
 */
public interface MultiItemComparisonCounts {
	
	int getXMissingCount();
	
	int getYMissingCount();
		
	int getDifferentCount();
	
	int getMatchedCount();
	
	int getBreaksCount();
	
	int getComparedCount();
	
}
