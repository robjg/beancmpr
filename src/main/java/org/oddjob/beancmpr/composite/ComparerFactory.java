package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

public interface ComparerFactory<T extends Comparer<?>> {

	public T createComparerWith(ComparersByType comparersByType);
}
