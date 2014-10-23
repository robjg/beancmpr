package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

/**
 * Collects together {@link ComparersByName} and {@link ComparersByType}
 * <p>
 * Used by {@link ComparersByNameOrTypeFactory}.
 * 
 * @author rob
 *
 */
public class ComparersByNameOrType implements BeanPropertyComparerProvider {
	
	private final ComparersByName comparersByName;
	
	private final ComparersByType comparersByType;
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparersByProperty May be null.
	 * @param comparersByType May be null.
	 * {@link DefaultComparersByType} otherwise creates a
	 * {@link CompositeComparersByType}
	 */
	public ComparersByNameOrType(
			ComparersByName comparersByProperty,
			ComparersByType comparersByType) {
		
		this.comparersByType = comparersByType; 
		this.comparersByName = comparersByProperty;
	}
	
	/**
	 * Create a new instance with default values. This is only used by 
	 * tests.
	 */
	public ComparersByNameOrType() {
		this(null, new DefaultComparersByType());
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Comparer<T> comparerFor(String property, Class<T> type) {

		Comparer<T> comparer = null;
		if (comparersByName != null) {
			comparer = 
				(Comparer<T>) comparersByName.getComparerForName(
						property);
		}
		
		if (comparer != null) {
			return comparer;
		}
		
		if (comparersByType != null) {
			return comparersByType.comparerFor(type);
		}

		return null;
	}
}
