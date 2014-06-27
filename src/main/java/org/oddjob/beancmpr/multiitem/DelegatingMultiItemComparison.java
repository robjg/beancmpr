package org.oddjob.beancmpr.multiitem;

import org.oddjob.beancmpr.Comparison;


/**
 * A {@link Comparison} that is the result of comparing many things.
 * 
 * @author rob
 *
 */
public class DelegatingMultiItemComparison<T> 
implements MultiItemComparison<T> {

	private final T x;
	
	private final T y;
	
	private final MultiItemComparison<?> delegate;
	
	public DelegatingMultiItemComparison(
			T x,
			T y,
			MultiItemComparison<?> delegate) {
		
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
	public int getXMissingCount() {
		return delegate.getXMissingCount();
	}
	
	@Override
	public int getYMissingCount() {
		return delegate.getYMissingCount();
	}
	
	@Override
	public int getMatchedCount() {
		return delegate.getMatchedCount();
	}
	
	@Override
	public int getDifferentCount() {
		return delegate.getDifferentCount();
	}
	
	@Override
	public int getBreaksCount() {
		return delegate.getBreaksCount();
	}
	
	@Override
	public int getComparedCount() {
		return delegate.getComparedCount();
	}
}
