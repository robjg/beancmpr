package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

public class ComparerFactoryAdaptor<T> 
implements ComparerFactory<T> {

	private final Comparer<T> comparer;
	
	public ComparerFactoryAdaptor(Comparer<T> comparer) {
		this.comparer = comparer;
	}
	
	@Override
	public Comparer<T> createComparerWith(ComparersByType ignored) {
		return comparer;
	}
	
}
