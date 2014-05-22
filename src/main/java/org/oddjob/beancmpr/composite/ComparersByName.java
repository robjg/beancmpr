package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

/**
 * Provide a {@link Comparer} for a property. 
 * 
 * @author rob
 *
 */
public interface ComparersByName {

	/**
	 * Provide a {@link Comparer}.
	 * 
	 * @param propertyName The property.
	 * @return The comparer. May be null.
	 */
	public Comparer<?> getComparerForProperty(String propertyName);
}
