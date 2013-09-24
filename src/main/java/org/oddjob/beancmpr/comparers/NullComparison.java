package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;

/**
 * The result of attempting to compare two values where one or both
 * of the values is null.
 * 
 * @author rob
 *
 * @param <T> The type of the values.
 */
public class NullComparison<T> implements Comparison<T> {

	private final T x;
	
	private final T y;
	
	public NullComparison(T x, T y) {
		this.x = x;
		this.y = y;
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
	public String getSummaryText() {
		return x == y ? "" : "Null Value";
	}
	
	@Override
	public int getResult() {
		return x == y ? 0 : x == null ? -1 : 1;
	}
}
