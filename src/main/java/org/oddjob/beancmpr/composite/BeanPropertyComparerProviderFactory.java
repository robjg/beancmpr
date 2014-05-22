package org.oddjob.beancmpr.composite;


/**
 * Something that is able to create a {@link ComparerProivder}.
 * 
 * @author rob
 *
 */
public interface BeanPropertyComparerProviderFactory {

	/**
	 * Create a {@link BeanPropertyComparerProvider} providing defaults and injection
	 * of parent comparers where necessary.
	 * 
	 * @param parentComparers Parent comparers to be injected. May be
	 * null.
	 * 
	 * @return A {@code ComparerProvider}. Never null.
	 */
	public BeanPropertyComparerProvider createWith(ComparersByType parentComparers);
}
