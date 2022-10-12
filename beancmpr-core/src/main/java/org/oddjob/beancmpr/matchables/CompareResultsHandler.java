package org.oddjob.beancmpr.matchables;


/**
 * Handles the result of comparing {@link Matchable}s.
 * 
 * @see OrderedMatchablesComparer
 * 
 * @author Rob
 *
 */
public interface CompareResultsHandler {
	
	/**
	 * Data is missing from X.
	 * 
	 * @param ys The y data.
	 */
	void xMissing(MatchableGroup ys);
	
	/**
	 * Data is missing from Y.
	 * 
	 * @param xs The x data.
	 */
	void yMissing(MatchableGroup xs);

	/**
	 * Two {@link Matchable}s have been compared
	 * by their keys.
	 * 
	 * @param comparison The result of the comparison.
	 */
	void compared(MultiValueComparison<Matchable> comparison);

}
