package org.oddjob.beancmpr.results;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

import java.util.Iterator;

public class RowSetResultHandler implements CompareResultsHandler {

	private final SimpleArraysResultHandler resultHandler =
			new SimpleArraysResultHandler();
	
	private Iterator<SimpleArraysResultHandler.Row> results;
	
	private SimpleArraysResultHandler.Row current;
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		resultHandler.compared(comparison);
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		resultHandler.xMissing(ys);
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		resultHandler.yMissing(xs);
	}
	
	public boolean next() {
		if (current == null) {
			results = resultHandler.getResults().iterator();
		}
		
		if (results.hasNext()) {
			current = results.next();
			return true;
		}
		else {
			current = null;
			return false;
		}
	}
	
	public MatchResultType getResultType() {
		return current.getResultType();
	}
	
	public Object getKey(int index) {
		return current.getKey(index);
	}
	
	public Comparison<?> getComparison(int index) {
		return current.getComparison(index);
	}
	
}
