package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

public class MockComparer<T> implements Comparer<T> {

	@Override
	public Comparison<T> compare(T x, T y) {
		throw new RuntimeException("Unexpected!");
	}
	
	@Override
	public Class<?> getType() {
		throw new RuntimeException("Unexpected!");
	}
}
