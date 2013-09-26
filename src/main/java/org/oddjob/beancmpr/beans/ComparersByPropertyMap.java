package org.oddjob.beancmpr.beans;

import java.util.HashMap;
import java.util.Map;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.HierarchicalComparer;

/**
 * Collects {@link Comparer}s by property name.
 * 
 * @author rob
 *
 */
public class ComparersByPropertyMap implements ComparersByProperty {

	public final Map<String, Comparer<?>> comparersByProperty = 
		new HashMap<String, Comparer<?>>();


	public void setComparerForProperty(String property, Comparer<?> comparer) {
		comparersByProperty.put(property, comparer);
	}
	
	@Override
	public Comparer<?> getComparerForProperty(String propertyName) {
		return comparersByProperty.get(propertyName);
	}
	
	@Override
	public void injectComparers(ComparersByType comparers) {
		for (Comparer<?> comparer : comparersByProperty.values()) {
			if (comparer instanceof HierarchicalComparer) {
				((HierarchicalComparer) comparer).injectComparers(comparers);
			}
		}
	}
}
