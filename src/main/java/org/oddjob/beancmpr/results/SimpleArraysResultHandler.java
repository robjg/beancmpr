package org.oddjob.beancmpr.results;

import java.util.ArrayList;
import java.util.List;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

public class SimpleArraysResultHandler implements BeanCmprResultsHandler {

	public static class Row {
	
		private MatchResultType.Type resultType;
		
		private Object[] keys;
		
		private Comparison<?>[] comparisons;
		
		public MatchResultType.Type getResultType() {
			return resultType;
		}
		
		public Object[] getKeys() {
			return keys;
		}
		
		public Object getKey(int index) {
			return keys[index];
		}
		
		public Comparison<?>[] getComparisons() {
			return comparisons;
		}
		
		public Comparison<?> getComparison(int index) {
			return comparisons[index];
		}
	}
	
	private List<Row> results = new ArrayList<Row>();
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		Row row = new Row();
		
		if (comparison.getResult() == 0) {
			row.resultType = MatchResultType.Type.EQUAL;
		}
		else {
			row.resultType = MatchResultType.Type.NOT_EQUAL;
		}
		row.keys = Iterables.toArray(comparison.getX().getKeys());
		
		row.comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class); 
		
		results.add(row);
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		Row row = new Row();
		
		row.resultType = MatchResultType.Type.X_MISSING;
		
		row.keys = Iterables.toArray(ys.getKeys());
		
		row.comparisons = new Comparison[0];

		results.add(row);
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		Row row = new Row();
		
		row.resultType = MatchResultType.Type.Y_MISSING;
		
		row.keys = Iterables.toArray(xs.getKeys());
		
		row.comparisons = new Comparison[0];

		results.add(row);
	}
	
	public List<Row> getResults() {
		return results;
	}
}
