package org.oddjob.beancmpr.composite;

import java.util.HashMap;
import java.util.Map;

import org.oddjob.beancmpr.Comparer;

/**
 * @oddjob.description Collects {@link Comparer}s by property name.
 * 
 * @author rob
 *
 */
public class ComparersByNameType 
implements ComparersByNameFactory {

	/**
	 * @oddjob.property
	 * @oddjob.description Map of name to the comparer type.
	 */
	public final Map<String, ComparerFactory<?>> comparersByName = 
		new HashMap<String, ComparerFactory<?>>();


	public void setComparerForProperty(String property, ComparerFactory<?> comparer) {
		comparersByName.put(property, comparer);
	}
	
	public ComparerFactory<?> getComparerForProperty(String propertyName) {
		return comparersByName.get(propertyName);
	}
	
	@Override
	public ComparersByName createComparersByNameWith(
			ComparersByType comparersByType) {
		
		Map<String, Comparer<?>> comparers =
				new HashMap<String, Comparer<?>>();
		
		for (Map.Entry<String, ComparerFactory<?>> entry : 
				comparersByName.entrySet()) {

			Comparer<?> comparer = entry.getValue().createComparerWith(
					comparersByType);
			
			comparers.put(entry.getKey(), comparer);
		}
		
		return new InternalComparersByName(comparers);
	}
	
}

class InternalComparersByName implements ComparersByName {
	
	public final Map<String, Comparer<?>> comparersByName;
	
	public InternalComparersByName(Map<String, Comparer<?>> comparersByName) {
		this.comparersByName = comparersByName;
	}
	
	@Override
	public Comparer<?> getComparerForProperty(String propertyName) {
		return comparersByName.get(propertyName);
	}
}
