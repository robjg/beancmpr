package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.HierarchicalComparer;

/**
 * Provide a {@link Comparer} for a property. 
 * 
 * @author rob
 *
 */
public interface ComparersByProperty extends HierarchicalComparer {

	/**
	 * Provide a {@link Comparer}.
	 * 
	 * @param propertyName The property.
	 * @return The comparer. May be null.
	 */
	public Comparer<?> getComparerForProperty(String propertyName);
}
