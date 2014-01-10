package org.oddjob.beancmpr.beans;

import java.util.Map;

import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.comparers.HierarchicalComparer;
import org.oddjob.beancmpr.comparers.MultiItemComparer;
import org.oddjob.beancmpr.comparers.MultiItemComparison;
import org.oddjob.beancmpr.matchables.MatchableFactory;

public class MapComparer 
implements MultiItemComparer<Map<?, ?>>, HierarchicalComparer {

	private MatchableFactory<Map.Entry<?, ?>> matchableFactory;
	
	private boolean sorted;
	
	private final ComparerProviderFactory comparerProviderFactory;
	
	private MultiItemComparer<Iterable<? extends Map.Entry<?, ?>>> comparer;
	
	public MapComparer(MatchableFactory<Map.Entry<?, ?>> matchableFactory,
			ComparerProviderFactory comparerProviderFactory) {
		this.matchableFactory = matchableFactory;
		this.comparerProviderFactory = comparerProviderFactory;
	}
		
	@Override
	public void injectComparers(ComparersByType parentComparers) {
		ComparerProvider comparerProvider = comparerProviderFactory.createWith(
				parentComparers);
		
		IterableBeansComparer<Map.Entry<?, ?>> beansComparer = 
				new IterableBeansComparer<Map.Entry<?, ?>>(
						matchableFactory, comparerProvider);
		
		beansComparer.setSorted(sorted);
		
		this.comparer = beansComparer;
	}
	
	@Override
	public MultiItemComparison<Map<?, ?>> compare(Map<?, ?> x, Map<?, ?> y) {

		MultiItemComparison<Iterable<? extends Map.Entry<?, ?>>> comparison = 
				comparer.compare(x.entrySet(), y.entrySet());
		
		return new DelegatingMultiItemComparison<Map<?,?>>(x, y, comparison);
	}
		
	@Override
	public Class<?> getType() {
		return Map.class;
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

}
