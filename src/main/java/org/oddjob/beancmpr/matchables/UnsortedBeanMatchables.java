package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;

/**
 * Takes an unsorted {@code Iterable} of beans and provides an 
 * {code Iterable} of {@link OrderedMatchables}s suitable for use by
 * an {@link OrderedMatchablesComparer}.
 * 
 * @param <T>
 * @author Rob
 *
 */
public class UnsortedBeanMatchables<T> 
implements Iterable<MatchableGroup> {
	
	/** Collect groups of matchables sorted by key. */
	private final SortedMap<SimpleMatchKey, List<Matchable>> data = 
		new TreeMap<SimpleMatchKey, List<Matchable>>();
	
	/**
	 * Constructor.
	 * 
	 * @param iterable The beans
	 * @param factory The factory to create the {link Matchable}s.
	 * @param comparerProvider Provide comparers to create key comparator.
	 */
	public UnsortedBeanMatchables(Iterable<? extends T> iterable, 
			MatchableFactory<T> factory,
			BeanPropertyComparerProvider comparerProvider) {
		
		Comparator<Iterable<?>> keyComparator = null;
		
		for (T thing : iterable) {
			
			if (Thread.currentThread().isInterrupted()) {
				break;
			}
			
			Matchable matchable = factory.createMatchable(thing);
			
			if (keyComparator == null) {
				
				MatchableMetaData metaData = matchable.getMetaData();
				
				keyComparator = new KeyComparatorFactory(
						comparerProvider).createComparatorFor(
								metaData, metaData);
			}
						
			SimpleMatchKey key = new SimpleMatchKey(
					matchable.getKeys(), keyComparator);
			
			
			List<Matchable> group = data.get(key);
			
			if (group == null) {
				group = new ArrayList<Matchable>();
				this.data.put(key, group);
			}
			group.add(matchable);
		}		
	}
	
	@Override
	public Iterator<MatchableGroup> iterator() {
		
		return new Iterator<MatchableGroup>() {
			
			private Iterator<Map.Entry<SimpleMatchKey, List<Matchable>>> iterator 
				= data.entrySet().iterator();
			
			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}
			
			@Override
			public MatchableGroup next() {
				
				final Map.Entry<SimpleMatchKey, List<Matchable>> next = 
					iterator.next();
				
				return new ListMatchableGroup(next.getValue());
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}	
}
