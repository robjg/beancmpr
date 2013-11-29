package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.CompositeComparersByType;
import org.oddjob.beancmpr.comparers.DefaultComparersByType;

/**
 * Collects together {@link ComparersByProperty} and {@link ComparersByType}
 * <p>
 * Used by {@link ComparersByPropertyOrTypeFactory}.
 * 
 * @author rob
 *
 */
public class ComparersByPropertyOrType implements ComparerProvider {
	
	private final ComparersByProperty comparersByProperty;
	
	private final ComparersByType comparersByType;
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparersByProperty May be null.
	 * @param comparersByType May be null.
	 * {@link DefaultComparersByType} otherwise creates a
	 * {@link CompositeComparersByType}
	 */
	public ComparersByPropertyOrType(
			ComparersByProperty comparersByProperty,
			ComparersByType comparersByType) {
		
		this.comparersByType = comparersByType; 
		this.comparersByProperty = comparersByProperty;
	}
	
	/**
	 * Create a new instance with default values. This is only used by 
	 * tests.
	 */
	public ComparersByPropertyOrType() {
		this(null, new DefaultComparersByType());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> Comparer<T> comparerFor(String property, Class<T> type) {

		Comparer<T> comparer = null;
		if (comparersByProperty != null) {
			comparer = 
				(Comparer<T>) comparersByProperty.getComparerForProperty(
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
