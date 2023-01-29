package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.ImmutableCollection;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparison;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

/**
 * The result of comparing two beans.
 * 
 * @author rob
 *
 */
public class BeanComparison<T> 
implements MultiValueComparison<T>, MultiItemComparison<T> {

	private final T x;
	
	private final T y;
	
	
	private final MultiValueComparison<?> delegate;
	
	/**
	 * Create a new instance.
	 * <p>
	 * This instance of a comparison of two beans relies on a delegate for
	 * the actual results of the comparison. This is because in the 
	 * current implementation the actual comparison is done by creating
	 * {@link Matchable} objects from the beans, and so the delegate
	 * will be an {@link MatchableComparison}.
	 * 
	 * @param x
	 * @param y
	 * @param delegate
	 */
	public BeanComparison(T x, T y,
				MultiValueComparison<?> delegate) {
		this.x = x;
		this.y = y;
		
		this.delegate = delegate;
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
		return delegate.getResult();
	}
	
	@Override
	public String getSummaryText() {
		return delegate.getSummaryText();
	}
	
	@Override
	public ImmutableCollection<Comparison<?>> getValueComparisons() {
		return delegate.getValueComparisons();
	}
	
	@Override
	public int getComparedCount() {
		return 1;
	}
	
	@Override
	public int getBreaksCount() {
		return getResult() == 0 ? 0 : 1;
	}
	
	@Override
	public int getDifferentCount() {
		return getResult() == 0 ? 0 : 1;
	}
	
	@Override
	public int getMatchedCount() {
		return getResult() == 0 ? 1 : 0;
	}
	
	@Override
	public int getXMissingCount() {
		return 0;
	}
	
	@Override
	public int getYMissingCount() {
		return 0;
	}
}
