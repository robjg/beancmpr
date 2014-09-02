package org.oddjob.beancmpr.beans;

import java.util.Map;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableFactoryProvider;
import org.oddjob.beancmpr.multiitem.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

/**
 * Compare two maps. The contents of the maps are converted to 
 * {@link Matchable}s in order to make the comparison.
 * 
 * @see MapComparerType
 * 
 * @author rob
 *
 * @param <K> The type of the key in the maps.
 * @param <V> The type of the value in the maps.
 */
public class MapComparer<K, V> 
implements MultiItemComparer<Map<K, V>> {

	/** Comparer to delegate to with the map entries. */
	private final IterableBeansComparer<Map.Entry<K,V>> comparer;
	
	/**
	 * Create a new instance. 
	 * 
	 * @param matchableFactoryProvider
	 * @param comparerProvider
	 * @param sorted
	 */
	public MapComparer(
			MatchableFactoryProvider<Map.Entry<K,V>> matchableFactoryProvider,
			BeanPropertyComparerProvider comparerProvider,
			boolean sorted) {
		
		this(matchableFactoryProvider, comparerProvider, sorted, null);
	}
	
	/**
	 * Create a new instance.
	 * 
	 * @param matchableFactoryProvider
	 * @param comparerProvider
	 * @param sorted
	 * @param resultHandler
	 */
	public MapComparer(
			MatchableFactoryProvider<Map.Entry<K,V>> matchableFactoryProvider,
			BeanPropertyComparerProvider comparerProvider,
			boolean sorted,
			BeanCmprResultsHandler resultHandler) {
		
		this.comparer = 
				new IterableBeansComparer<Map.Entry<K,V>>(
						matchableFactoryProvider, 
						comparerProvider,
						sorted,
						resultHandler);
	}
		
	@Override
	public MultiItemComparison<Map<K, V>> compare(
			Map<K, V> x, Map<K, V> y) {

		MultiItemComparison<Iterable<Map.Entry<K, V>>> comparison = 
				comparer.compare(x.entrySet(), y.entrySet());
		
		return new DelegatingMultiItemComparison<Map<K,V>>(x, y, comparison);
	}
		
	@Override
	public Class<?> getType() {
		return Map.class;
	}

	/**
	 * Is this comparer for a sorted map.
	 * 
	 * @return
	 */
	public boolean isSorted() {
		return comparer.isSorted();
	}

}
