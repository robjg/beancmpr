package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparison;


/**
 * A {@link Comparison} that is the result of comparing many things.
 * 
 * @author rob
 *
 */
public interface MultiItemComparison<T> 
extends Comparison<T>, MultiItemComparisonCounts {

}
