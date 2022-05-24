package org.oddjob.beancmpr.multiitem;

import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;

public interface MultiItemComparerFactory<T> {

	public MultiItemComparer<T> createComparerWith(ComparersByType comparersByType, 
			BeanCmprResultsHandler resultHandler);
	
}
