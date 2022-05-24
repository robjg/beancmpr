package org.oddjob.beancmpr.multiitem;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.MultiValueComparison;


/**
 * A {@link Comparison} that is the result of comparing many Objects such
 * as from a List. This should not be confused with a 
 * {@link MultiValueComparison} which is the result of comparing many thing
 * from the same object such as properties.
 * 
 * @see MultiItemComparer
 * 
 * @author rob
 *
 */
public interface MultiItemComparison<T> 
extends Comparison<T>, MultiItemComparisonCounts {

}
