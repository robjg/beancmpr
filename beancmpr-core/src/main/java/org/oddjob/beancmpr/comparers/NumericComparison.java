package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;

/**
 * Represent a comparison between two numeric values.
 * 
 * @author Rob
 *
 */
public interface NumericComparison extends Comparison<Number> {

	 double getPercentage();
	 
	 double getDelta();
}
