package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.comparers.ComparersByType;

public interface ComparerProviderFactory {

	public ComparerProvider createWith(ComparersByType parentComparers);
}
