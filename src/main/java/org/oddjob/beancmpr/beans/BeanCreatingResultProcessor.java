package org.oddjob.beancmpr.beans;

import java.util.Collection;

import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

/**
 * Creates result beans for matches.
 * 
 * @author rob
 *
 */
public class BeanCreatingResultProcessor implements BeanCmprResultsHandler {

	private final MatchResultBeanFactory factory;
	
	private final Collection<Object> out;
	
	public BeanCreatingResultProcessor(
			MatchResultBeanFactory matchResultBeanFactory,
			Collection<Object> collection) {
		
		factory = matchResultBeanFactory;
		
		this.out = collection;
	}
		
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		out.add(factory.createComparisonResult(comparison));
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		for (Matchable y : ys.getGroup()) {			
			out.add(factory.createXMissingResult(y));
		}
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		for (Matchable x : xs.getGroup()) {
			out.add(factory.createYMissingResult(x));
		}
	}	
}
