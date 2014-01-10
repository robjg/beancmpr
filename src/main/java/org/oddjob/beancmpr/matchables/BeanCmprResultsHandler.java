package org.oddjob.beancmpr.matchables;



/**
 * Handles the result of comparing {@link Matchable}s.
 * 
 * @see OrderedMatchablesComparer.
 * 
 * @author Rob
 *
 */
public interface BeanCmprResultsHandler {
	
	/**
	 * Data is missing from X.
	 * 
	 * @param ys The y data.
	 */
	public void xMissing(MatchableGroup ys);
	
	/**
	 * Data is missing from Y.
	 * 
	 * @param xs The x data.
	 */
	public void yMissing(MatchableGroup xs);

	/**
	 * Two {@link Matchable}s have been compared
	 * by their keys.
	 * 
	 * @param comparison The result of the comparison.
	 */
	public void compared(MultiValueComparison<Matchable> comparison);

}
