package org.oddjob.beancmpr.composite;


/**
 * Create {@link BeanPropertyComparerProvider} by a combination of 
 * {@link ComparersByName} and {@link ComparersByType} and using
 * {@link DefaultComparersByType} where necessary.
 * <p>
 * The resulting {@code ComparerProvider} will always be able to provide
 * a {@link Comparer} for any type.
 * 
 * @author rob
 *
 */
public class ComparersByNameOrTypeFactory 
implements BeanPropertyComparerProviderFactory {

	private final ComparersByTypeFactory comparersByType;
	
	private final ComparersByNameFactory comparersByName;

	public ComparersByNameOrTypeFactory() {
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
	public ComparersByNameOrTypeFactory(
			ComparersByNameFactory comparersByProperty,
			ComparersByTypeFactory comparersByType) {
		
		this.comparersByType = comparersByType;
		this.comparersByName = comparersByProperty;
	}
	
	@Override
	public BeanPropertyComparerProvider createWith(ComparersByType parentComparers) {
		
		if (parentComparers == null) {
			parentComparers = new DefaultComparersByType();
		}
		
		ComparersByType comparersByType;
		
		if (this.comparersByType == null) {
			comparersByType = parentComparers;
		}
		else {
			comparersByType = new CompositeComparersByType(
					this.comparersByType.createComparersByTypeWith(parentComparers), 
					parentComparers);
		}
		
		ComparersByName comparersByProperty = null;
		
		if (this.comparersByName != null) {
			comparersByProperty = 
					this.comparersByName.createComparersByNameWith(
							comparersByType);
		}
		
		return new ComparersByNameOrType(
				comparersByProperty, comparersByType);
	}
}
