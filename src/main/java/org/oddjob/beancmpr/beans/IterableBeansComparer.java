package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.comparers.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.comparers.MultiItemComparer;
import org.oddjob.beancmpr.comparers.MultiItemComparison;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.OrderedMatchablesComparer;
import org.oddjob.beancmpr.matchables.SortedBeanMatchables;
import org.oddjob.beancmpr.matchables.UnsortedBeanMatchables;

public class IterableBeansComparer<T> 
implements MultiItemComparer<Iterable<? extends T>> {

	private final MatchableFactory<T> matchableFactory;
	
	private final ComparerProvider comparerProvider;
	
	private BeanCmprResultsHandler resultsHandler;
	
	private boolean sorted;
	
	public IterableBeansComparer(MatchableFactory<T> matchableFactory,
			ComparerProvider comparerProvider) {
		
		if (matchableFactory == null) {
			throw new NullPointerException("MatchableFactory must be provided.");
		}
		
		if (comparerProvider == null) {
			throw new NullPointerException("ComparerProvider must be provided.");
		}

		this.matchableFactory = matchableFactory;
		this.comparerProvider = comparerProvider;
	}	
	
	@Override
	public MultiItemComparison<Iterable<? extends T>> compare(Iterable<? extends T> x, 
			Iterable<? extends T> y) {

		OrderedMatchablesComparer rec = new OrderedMatchablesComparer(
				comparerProvider,
				resultsHandler);
		
		Iterable<MatchableGroup> xGroups = getIterableMatchables(x, 
				matchableFactory, comparerProvider);
		
		Iterable<MatchableGroup> yGroups = getIterableMatchables(y, 
				matchableFactory, comparerProvider);
				
		MultiItemComparison<Iterable<? extends MatchableGroup>> groupComparison = 
				rec.compare(xGroups, yGroups);
		
		return new DelegatingMultiItemComparison<Iterable<? extends T>>(
				x, y, groupComparison);
	}
	
	private Iterable<MatchableGroup> getIterableMatchables(
			Iterable<? extends T> in, MatchableFactory<T> factory,
			ComparerProvider comparerProvider) {
		
		if (sorted) {
			return new SortedBeanMatchables<T>(in, factory, 
					comparerProvider);
		}
		else {
			return new UnsortedBeanMatchables<T>(in, factory,
					comparerProvider);
		}
	}
	
	@Override
	public Class<?> getType() {
		return Iterable.class;
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public BeanCmprResultsHandler getResultsHandler() {
		return resultsHandler;
	}

	public void setResultsHandler(BeanCmprResultsHandler resultsHandler) {
		this.resultsHandler = resultsHandler;
	}
}
