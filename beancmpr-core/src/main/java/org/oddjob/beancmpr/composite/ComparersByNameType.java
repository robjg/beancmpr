package org.oddjob.beancmpr.composite;

import java.util.HashMap;
import java.util.Map;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.beans.BeanArrayComparerType;
import org.oddjob.beancmpr.beans.BeanComparerType;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;
import org.oddjob.beancmpr.beans.MapComparerType;

/**
 * @oddjob.description Allows comparers to be specified by the names of 
 * a properties. This type is used by comparers such as 
 * {@link BeanArrayComparerType}, {@link IterableBeansComparerType},
 * {@link BeanComparerType}, or {@link MapComparerType}.
 * 
 * @author rob
 *
 */
public class ComparersByNameType 
implements ComparersByNameFactory {

	/**
	 * @oddjob.property
	 * @oddjob.description Comparer keys by property name.
	 * @oddjob.required No. But pointless if missing.
	 */
	public final Map<String, ComparerFactory<?>> comparersByName = 
		new HashMap<String, ComparerFactory<?>>();


	public void setComparers(String property, ComparerFactory<?> comparer) {
		comparersByName.put(property, comparer);
	}
	
	public ComparerFactory<?> getComparers(String propertyName) {
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
	public Comparer<?> getComparerForName(String propertyName) {
		return comparersByName.get(propertyName);
	}
}
