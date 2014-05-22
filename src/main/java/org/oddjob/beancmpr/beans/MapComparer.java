package org.oddjob.beancmpr.beans;

import java.util.Map;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.multiitem.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

public class MapComparer 
implements MultiItemComparer<Map<?, ?>> {

	private final IterableBeansComparer<Map.Entry<?, ?>> comparer;
	
	public MapComparer(MatchableFactory<Map.Entry<?, ?>> matchableFactory,
			BeanPropertyComparerProvider comparerProvider,
			boolean sorted) {
		
		this.comparer = 
				new IterableBeansComparer<Map.Entry<?, ?>>(
						matchableFactory, comparerProvider);
		
		comparer.setSorted(sorted);
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
		return comparer.isSorted();
	}

}
