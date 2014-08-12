package org.oddjob.beancmpr.composite;

/**
 * Creates {@link ComparersByName} allowing predefined 
 * {@link ComparersByType} to be
 * used within the comparers provided here. 
 *
 * @see ComparersByNameType
 */
public interface ComparersByNameFactory {

	/**
	 * Create a {@link ComparersByName} instance.
	 * 
	 * @param comparersByType
	 * @return
	 */
	public ComparersByName createComparersByNameWith(
			ComparersByType comparersByType);
}
