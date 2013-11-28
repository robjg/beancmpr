package org.oddjob.beancmpr.comparers;

import java.util.Map;

import org.oddjob.beancmpr.Comparer;

class InternalComparersByType implements ComparersByType {

	private final Map<Class<?>, Comparer<?>> comparers;
	
	public InternalComparersByType(Map<Class<?>, Comparer<?>> comparers) {
		this.comparers = comparers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Comparer<T> comparerFor(Class<T> type) {
		
		for (Map.Entry<Class<?>, Comparer<?>> entry : 
				comparers.entrySet()) {
			
			if (entry.getKey().isAssignableFrom(type)) {
				return (Comparer<T>) entry.getValue();
			}
		}
		
		return null;
	}

	@Override
	public void injectComparers(ComparersByType comparers) {
		
		for (Comparer<?> comparer : this.comparers.values()) {
		
			if (comparer instanceof HierarchicalComparer) {
				((HierarchicalComparer) comparer)
						.injectComparers(comparers);
			}
		}
	}
}