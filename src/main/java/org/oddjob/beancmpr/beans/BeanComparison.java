package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

/**
 * The result of comparing two beans.
 * 
 * @author rob
 *
 */
public class BeanComparison implements MultiValueComparison<Object> {

	private final Object x;
	
	private final Object y;
	
	private final MultiValueComparison<?> delegate;
	
	public BeanComparison(Object x, Object y,
				MultiValueComparison<?> delegate) {
		this.x = x;
		this.y = y;
		
		this.delegate = delegate;
	}
	
	
	@Override
	public Object getX() {
		return x;
	}
	
	@Override
	public Object getY() {
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
	public Iterable<Comparison<?>> getValueComparisons() {
		return delegate.getValueComparisons();
	}
}
