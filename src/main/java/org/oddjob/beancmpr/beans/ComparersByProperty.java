package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparer;

/**
 * Provide a {@link Comparer} for a property. 
 * 
 * @author rob
 *
 */
public interface ComparersByProperty {

	/**
	 * Provide a {@link Comparer}.
	 * 
	 * @param propertyName The property.
	 * @return The comparer. May be null.
	 */
	public Comparer<?> getComparerForProperty(String propertyName);
}
