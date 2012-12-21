package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparer;


/**
 * Something that can compare two {@link Matchable}s.
 * 
 * @author Rob
 *
 */
public interface MatchableComparer extends Comparer<Matchable> {

	public MatchableComparison compare(Matchable x, Matchable y);	
}
