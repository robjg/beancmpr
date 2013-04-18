package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

/**
 * Creates a bean who's properties are the results.
 * 
 * @author rob
 *
 */
public interface ResultBeanFactory {

	/**
	 * Create a result bean for a comparison between two beans.
	 * 
	 * @param matchableComparison
	 * @return
	 */
	public Object createComparisonResult(
			MultiValueComparison<Matchable> matchableComparison);	
	
	public Object createXMissingResult(Matchable y);
	
	public Object createYMissingResult(Matchable x);
}
