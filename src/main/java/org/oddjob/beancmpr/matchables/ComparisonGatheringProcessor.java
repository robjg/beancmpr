package org.oddjob.beancmpr.matchables;

import java.util.concurrent.atomic.AtomicInteger;

import org.oddjob.beancmpr.comparers.MultiItemComparisonStats;

/**
 * A {@link BeanCmprResultsHandler} that counts matches, before passing the result
 * onto a delegate.
 * 
 * @author rob
 *
 */
public class ComparisonGatheringProcessor 
implements BeanCmprResultsHandler, MultiItemComparisonStats {

	private final BeanCmprResultsHandler delegate;
	
	private final AtomicInteger xMissingCount = new AtomicInteger();
	
	private final AtomicInteger yMissingCount = new AtomicInteger();

	private final AtomicInteger differentCount = new AtomicInteger();
	
	private final AtomicInteger matchedCount = new AtomicInteger();
		
	public ComparisonGatheringProcessor(BeanCmprResultsHandler delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		xMissingCount.addAndGet(ys.getSize());
		
		if (delegate != null) {
			delegate.xMissing(ys);
		}
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		yMissingCount.addAndGet(xs.getSize());
		
		if (delegate != null) {
			delegate.yMissing(xs);
		}
	}
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		if (comparison.getResult() == 0) {
			matchedCount.incrementAndGet();
		}
		else {
			differentCount.incrementAndGet();
		}
		
		if (delegate != null) {
			delegate.compared(comparison);
		}
	}
	
	@Override
	public int getXMissingCount() {
		return xMissingCount.get();
	}
	
	@Override
	public int getYMissingCount() {
		return yMissingCount.get();
	}
	
	@Override
	public int getMatchedCount() {
		return matchedCount.get();
	}
	
	@Override
	public int getDifferentCount() {
		return differentCount.get();
	}
	
	@Override
	public int getBreaksCount() {
		return xMissingCount.get() + yMissingCount.get() + 
				differentCount.get();
	}
	
	@Override
	public int getComparedCount() {
		return getBreaksCount() + matchedCount.get();
	}
}
