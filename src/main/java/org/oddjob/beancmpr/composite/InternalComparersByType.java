package org.oddjob.beancmpr.composite;

import java.util.Map;

import org.apache.log4j.Logger;
import org.oddjob.beancmpr.Comparer;

/**
 * Used by {@link ComparersByTypeList}.
 * 
 * @author rob
 *
 */
class InternalComparersByType implements ComparersByType {

	private static final Logger logger = Logger.getLogger(InternalComparersByType.class);
	
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
				
				Comparer<T> comparer = (Comparer<T>) entry.getValue();
				
				logger.trace("Providing [" + comparer + "] for type " +
						type.getName());
				
				return comparer;
			}
		}
		
		return null;
	}
}