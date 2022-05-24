package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparer;


/**
 * Something that can compare two {@link Matchable}s based on the contents
 * of their values (not keys).
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
