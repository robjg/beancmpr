package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;

/**
 * A simple comparison where the result of a compare was that
 * two values are not equal.
 * 
 * @author rob
 *
 */
public class EqualityComparison<T> implements Comparison<T> {

	private final T x;
	private final T y;
	
	private final int result;
	
	@SuppressWarnings("unchecked")
	public EqualityComparison(T x, T y) {
		this.x = x;
		this.y = y;
		
		if (x instanceof Comparable<?>) {
			result = ((Comparable<T>) x).compareTo(y);
		}
		else if (x.equals(y)) {
			result = 0;
		}
		else {
			result = -1;
		}
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
		return result;
	}
	
	public String getSummaryRegardless() {
		return "" + x + (result == 0 ? "==" : "<>") + y;
	}
	
	@Override
	public String getSummaryText() {
		if (result == 0) {
			return "";
		}
		else {
			return getSummaryRegardless();
		}
	}
	
	@Override
	public String toString() {
		return "SimpleComparison " + getSummaryRegardless();
	}
	
}
