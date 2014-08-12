package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

/**
 * Provide a {@link Comparer} for a property specified by the name of
 * the property. 
 * 
 * @author rob
 *
 */
public interface ComparersByName {

	/**
	 * Provide a {@link Comparer}.
	 * 
	 * @param propertyName The property name.
	 * @return The comparer. Will be null if none is specified for .
	 */
	public Comparer<?> getComparerForProperty(String propertyName);
}
