package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.comparers.MultiItemComparisonStats;

/**
 * A {@link MatchableMatchProcessor} that counts matches, before passing the result
 * onto a delegate.
 * 
 * @author rob
 *
 */
public class ComparisonGatheringProcessor 
implements MatchableMatchProcessor {

	private final MatchableMatchProcessor delegate;
	
	private int xsMissing;
	
	private int ysMissing;

	private int different;
	
	private int same;
	
	public ComparisonGatheringProcessor(MatchableMatchProcessor delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		xsMissing += ys.getSize();
		if (delegate != null) {
			delegate.xMissing(ys);
		}
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		ysMissing += xs.getSize();
		if (delegate != null) {
			delegate.yMissing(xs);
		}
	}
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		if (comparison.getResult() == 0) {
			++same;
		}
		else {
			++different;
		}
		if (delegate != null) {
			delegate.compared(comparison);
		}
	}
	
	public MultiItemComparisonStats getComparison() {
		return new MultiItemComparisonStats(
				xsMissing, ysMissing, different, same);
	}
	
}
