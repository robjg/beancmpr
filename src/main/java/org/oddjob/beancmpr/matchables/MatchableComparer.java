package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparer;


/**
 * Something that can compare two {@link Matchable}s.
 * 
 * @author Rob
 *
 */
public interface MatchableComparer extends Comparer<Matchable> {

	/*
	 * (non-Javadoc)
	 * @see org.oddjob.beancmpr.Comparer#compare(java.lang.Object, java.lang.Object)
	 */
	public MultiValueComparison<Matchable> compare(Matchable x, Matchable y);	
}
