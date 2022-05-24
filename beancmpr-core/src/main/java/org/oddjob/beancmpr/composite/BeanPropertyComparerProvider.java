package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

/**
 * Provides {@link Comparer}s for a bean class.
 * 
 * @author Rob
 *
 */
public interface BeanPropertyComparerProvider {

	/**
	 * Provide a {@link Comparer} for the given property
	 * and type.
	 * 
	 * @param property The name of the property.
	 * @param type The type of the property.
	 * 
	 * @return Never null.
	 */
	public <T> Comparer<T> comparerFor(String property, Class<T> type);
}
