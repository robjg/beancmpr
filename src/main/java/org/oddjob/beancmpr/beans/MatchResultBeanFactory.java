package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparison;

/**
 * Creates a bean who's properties are the results.
 * 
 * @author rob
 *
 */
public interface MatchResultBeanFactory {

	
	public Object createResult(Matchable x, Matchable y, 
			MatchableComparison matchableComparison);	
	
}
