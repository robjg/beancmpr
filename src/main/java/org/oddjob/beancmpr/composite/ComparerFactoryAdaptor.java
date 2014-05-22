package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

public class ComparerFactoryAdaptor<T extends Comparer<?>> 
implements ComparerFactory<T> {

	private final T comparer;
	
	public ComparerFactoryAdaptor(T comparer) {
		this.comparer = comparer;
	}
	
	@Override
	public T createComparerWith(ComparersByType ignored) {
		return comparer;
	}
	
}
