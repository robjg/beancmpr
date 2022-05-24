package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;

public class MockComparison<T> implements Comparison<T> {

	@Override
	public int getResult() {
		throw new RuntimeException("Unexpected!");
	}
	
	@Override
	public String getSummaryText() {
		throw new RuntimeException("Unexpected!");
	}
	
	@Override
	public T getX() {
		throw new RuntimeException("Unexpected!");
	}
	
	@Override
	public T getY() {
		throw new RuntimeException("Unexpected!");
	}
}
