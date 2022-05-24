package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactoryProvider;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.OrderedMatchablesComparer;
import org.oddjob.beancmpr.matchables.SortedBeanMatchables;
import org.oddjob.beancmpr.matchables.UnsortedBeanMatchables;
import org.oddjob.beancmpr.multiitem.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

/**
 * Compare any two collections of beans.
 * 
 * @author rob
 *
 * @param <T>
 */
public class IterableBeansComparer<T>
implements MultiItemComparer<Iterable<T>> {

	/** Provider a {@link MatchableFactory}. */
	private final MatchableFactoryProvider<T> matchableFactoryProvider;
	
	/** Provide comparers for property names or by type. */
	private final BeanPropertyComparerProvider comparerProvider;
	
	/** Handler results. */
	private final BeanCmprResultsHandler resultsHandler;
	
	/** Are the iterables sorted? */
	private final boolean sorted;
	
	
	public IterableBeansComparer(
			MatchableFactoryProvider<T> matchableFactoryProvider,
			BeanPropertyComparerProvider comparerProvider,
			boolean sorted) {
		
		this(matchableFactoryProvider, comparerProvider, sorted, null);
	}
	
	public IterableBeansComparer(
			MatchableFactoryProvider<T> matchableFactoryProvider,
			BeanPropertyComparerProvider comparerProvider,
			boolean sorted,
			BeanCmprResultsHandler resultsHandler) {
		
		if (matchableFactoryProvider == null) {
			throw new NullPointerException("MatchDefinition must be provided.");
		}
		
		if (comparerProvider == null) {
			throw new NullPointerException("ComparerProvider must be provided.");
		}

		this.matchableFactoryProvider = matchableFactoryProvider;
		this.comparerProvider = comparerProvider;
		this.sorted = sorted;
		this.resultsHandler = resultsHandler;
	}	
		
	@Override
	public MultiItemComparison<Iterable<T>> compare(
			Iterable<T> x, Iterable<T> y) {

		OrderedMatchablesComparer rec = new OrderedMatchablesComparer(
				comparerProvider,
				resultsHandler);
		
		Iterable<MatchableGroup> xGroups = getIterableMatchables(x, 
				matchableFactoryProvider.provideFactory(), 
				comparerProvider);
		
		Iterable<MatchableGroup> yGroups = getIterableMatchables(y, 
				matchableFactoryProvider.provideFactory(), 
				comparerProvider);
				
		MultiItemComparison<Iterable<MatchableGroup>> groupComparison = 
				rec.compare(xGroups, yGroups);
		
		return new DelegatingMultiItemComparison<Iterable<T>>(
				x, y, groupComparison);
	}
	
	/**
	 * Helper method to provide the {@link MatchableGroup}s.
	 * 
	 * @param in
	 * @param factory
	 * @param comparerProvider
	 * @return
	 */
	private Iterable<MatchableGroup> getIterableMatchables(
			Iterable<? extends T> in, MatchableFactory<T> factory,
			BeanPropertyComparerProvider comparerProvider) {
		
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

	public BeanCmprResultsHandler getResultsHandler() {
		return resultsHandler;
	}
}
