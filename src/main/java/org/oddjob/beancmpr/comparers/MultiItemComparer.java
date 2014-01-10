package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;

public interface MultiItemComparer<T> extends Comparer<T> {

	@Override
	public MultiItemComparison<T> compare(T x, T y);
}
