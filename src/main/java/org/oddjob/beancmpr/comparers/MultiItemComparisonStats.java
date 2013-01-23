package org.oddjob.beancmpr.comparers;



/**
 * Stats.
 * 
 * @author rob
 *
 */
public class MultiItemComparisonStats {

	private final int xsMissing;
	
	private final int ysMissing;

	private final int different;
	
	private final int same;
		
	public MultiItemComparisonStats(
			int xsMissing,
			int ysMissing,
			int different,
			int same) {
		
		this.xsMissing = xsMissing;
		this.ysMissing = ysMissing;
		this.different = different;
		this.same = same;
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
