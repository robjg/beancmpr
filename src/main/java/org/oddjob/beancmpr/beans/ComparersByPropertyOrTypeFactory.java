package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.CompositeComparersByType;
import org.oddjob.beancmpr.comparers.DefaultComparersByType;

/**
 * Create {@link ComparerProvider} by a combination of 
 * {@link ComparersByProperty} and {@link ComparersByType} and using
 * {@link DefaultComparersByType} where necessary.
 * <p>
 * The resulting {@code ComparerProvider} will always be able to provide
 * a {@link Comparer} for any type.
 * 
 * @author rob
 *
 */
public class ComparersByPropertyOrTypeFactory 
implements ComparerProviderFactory {

	private final ComparersByType comparersByType;
	
	private final ComparersByProperty comparersByProperty;

	public ComparersByPropertyOrTypeFactory() {
		this(null, null);
	}
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparersByProperty May be null.
	 * @param comparersByType If null then this will use just
	 * {@link DefaultComparersByType} otherwise creates a
	 * {@link CompositeComparersByType} with the defaults.
	 */
	public ComparersByPropertyOrTypeFactory(
			ComparersByProperty comparersByProperty,
			ComparersByType comparersByType) {
		
		this.comparersByType = comparersByType;
		this.comparersByProperty = comparersByProperty;
	}
	
	@Override
	public ComparerProvider createWith(ComparersByType parentComparers) {
		
		if (parentComparers == null) {
			parentComparers = new DefaultComparersByType();
		}
		
		ComparersByType comparersByType;
		
		if (this.comparersByType == null) {
			comparersByType = parentComparers;
		}
		else {
			comparersByType = new CompositeComparersByType(
					this.comparersByType, parentComparers);
			this.comparersByType.injectComparers(comparersByType);
		}
		
		if (this.comparersByProperty != null) {
			this.comparersByProperty.injectComparers(comparersByType);
		}
		
		return new ComparersByPropertyOrType(
				comparersByProperty, comparersByType);
	}
}
