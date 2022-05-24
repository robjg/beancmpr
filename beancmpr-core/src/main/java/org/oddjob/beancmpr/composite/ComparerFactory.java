package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

public interface ComparerFactory<T> {

	public Comparer<T> createComparerWith(
			ComparersByType comparersByType);
}
