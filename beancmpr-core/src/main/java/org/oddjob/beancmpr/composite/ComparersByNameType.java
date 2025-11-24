package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.beans.BeanArrayComparerType;
import org.oddjob.beancmpr.beans.BeanComparerType;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;
import org.oddjob.beancmpr.beans.MapComparerType;

import java.util.HashMap;
import java.util.Map;

/**
 * @oddjob.description Allows comparers to be specified by the names of 
 * a properties. This type is used by comparers such as 
 * {@link BeanArrayComparerType}, {@link IterableBeansComparerType},
 * {@link BeanComparerType}, or {@link MapComparerType}.
 * The name of the property is
 * given as the key. It must exactly match the incoming field name. If it
 * doesn't, Beancmpr will silently fall back on the default comparer. *
 * @author rob
 *
 */
public class ComparersByNameType 
implements ComparersByNameFactory {

	/**
	 * @oddjob.property comparers
	 * @oddjob.description Comparers keyed by property name.
	 * @oddjob.required No. But pointless if missing.
	 */
	public final Map<String, ComparerFactory<?>> comparersByName =
			new HashMap<>();


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
				new HashMap<>();
		
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
