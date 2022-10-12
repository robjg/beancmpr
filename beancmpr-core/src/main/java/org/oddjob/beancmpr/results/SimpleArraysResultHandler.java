package org.oddjob.beancmpr.results;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SimpleArraysResultHandler implements CompareResultsHandler, CompareResultsHandlerFactory {

	public static class Row {
	
		private MatchResultType resultType;
		
		private Object[] keys;
		
		private Comparison<?>[] comparisons;
		
		public MatchResultType getResultType() {
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
	
	private final List<Row> results = new ArrayList<>();

	@Override
	public CompareResultsHandler createResultsHandlerTo(Consumer<Object> resultsConsumer) {
		return this;
	}

	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		Row row = new Row();
		
		if (comparison.getResult() == 0) {
			row.resultType = MatchResultType.EQUAL;
		}
		else {
			row.resultType = MatchResultType.NOT_EQUAL;
		}
		row.keys = Iterables.toArray(comparison.getX().getKeys());
		
		row.comparisons = Iterables.toArray(
				comparison.getValueComparisons(), Comparison.class); 
		
		results.add(row);
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		Row row = new Row();
		
		row.resultType = MatchResultType.X_MISSING;
		
		row.keys = Iterables.toArray(ys.getKeys());
		
		row.comparisons = new Comparison[0];

		results.add(row);
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		Row row = new Row();
		
		row.resultType = MatchResultType.Y_MISSING;
		
		row.keys = Iterables.toArray(xs.getKeys());
		
		row.comparisons = new Comparison[0];

		results.add(row);
	}
	
	public List<Row> getResults() {
		return results;
	}
}
