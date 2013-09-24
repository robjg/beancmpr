package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.CompositeComparersByType;
import org.oddjob.beancmpr.comparers.DefaultComparersByType;

/**
 * Collects together {@link ComparersByProperty} and {@link ComparersByType}
 * <p>
 * Also provides a base set of {@link DefaultComparersByType}.
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
	 * @param comparersByType If null defaults to 
	 * {@link DefaultComparersByType} otherwise creates a
	 * {@link CompositeComparersByType}
	 */
	public ComparersByPropertyOrType(
			ComparersByProperty comparersByProperty,
			ComparersByType comparersByType) {
		if (comparersByType == null) {
			this.comparersByType = new DefaultComparersByType(); 
		}
		else {
			this.comparersByType = new CompositeComparersByType(
					comparersByType, new DefaultComparersByType());
		}
		
		this.comparersByType.injectComparers(this.comparersByType);
		
		this.comparersByProperty = comparersByProperty;
	}
	
	/**
	 * Create a new instance with default values.
	 */
	public ComparersByPropertyOrType() {
		this(null, null);
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
		
		return comparersByType.comparerFor(type);
	}
}
