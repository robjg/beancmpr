package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Used by {@link ComparersByTypeList} and {@link DefaultComparersByType}.
 * 
 * @author rob
 *
 */
class InternalComparersByType implements ComparersByType {

	private static final Logger logger = LoggerFactory.getLogger(InternalComparersByType.class);
	
	private final Map<Type, Comparer<?>> comparers;
	
	public InternalComparersByType(Map<Type, Comparer<?>> comparers) {
		this.comparers = comparers;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Comparer<T> comparerFor(Type type) {
		
		Comparer<T> exactMatch = (Comparer<T>) comparers.get(type);
		if (exactMatch != null) {
			return exactMatch;
		}
		
		for (Map.Entry<Type, Comparer<?>> entry :
				comparers.entrySet()) {
			
			if (TypeUtil.isAssignableFrom(entry.getKey(), type)) {
				
				Comparer<T> comparer = (Comparer<T>) entry.getValue();

                logger.trace("Providing [{}] for type {}", comparer, type.getTypeName());
				
				return comparer;
			}
		}
		
		return null;
	}
}