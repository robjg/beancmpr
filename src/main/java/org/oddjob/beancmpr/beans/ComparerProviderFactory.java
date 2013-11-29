package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.comparers.ComparersByType;

/**
 * Something that is able to create a {@link ComparerProivder}.
 * 
 * @author rob
 *
 */
public interface ComparerProviderFactory {

	/**
	 * Create a {@link ComparerProvider} providing defaults and injection
	 * of parent comparers where necessary.
	 * 
	 * @param parentComparers Parent comparers to be injected. May be
	 * null.
	 * 
	 * @return A {@code ComparerProvider}. Never null.
	 */
	public ComparerProvider createWith(ComparersByType parentComparers);
}
