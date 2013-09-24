package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.beans.ComparerProvider;

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
	 * @param comparerProvider Provide comparers.
	 */
	public UnsortedBeanMatchables(Iterable<? extends T> iterable, 
			MatchableFactory<T> factory,
			ComparerProvider comparerProvider) {
		
		Comparator<Iterable<?>> keyComparator = null;
		
		for (T thing : iterable) {
			Matchable matchable = factory.createMatchable(thing);
			
			if (keyComparator == null) {
				
				MatchableMetaData metaData = matchable.getMetaData();
				
				keyComparator = new KeyComparatorFactory(
						comparerProvider).createComparerFor(
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
				
				return new MatchableGroup() {
					
					@Override
					public Iterable<?> getKeys() {
						return next.getKey().getKeys();
					}
					
					@Override
					public Iterable<Matchable> getGroup() {
						return next.getValue();
					}
					
					@Override
					public MatchableMetaData getMetaData() {
						return next.getValue().get(0).getMetaData();
					}
					
					@Override
					public int getSize() {
						return next.getValue().size();
					}
					
					@Override
					public String toString() {
						return MatchableGroup.class.getSimpleName() + 
							": " + Iterables.toString(getKeys()) + ", size=" + getSize();
					}

				};
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}	
}
