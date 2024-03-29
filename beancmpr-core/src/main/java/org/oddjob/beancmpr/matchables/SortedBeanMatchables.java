package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;

/**
 * An adapter that converts an {@code Iterable} of beans to 
 * an {@code Iterable} of {@link MatchableGroup}s suitable for use by
 * an {@link OrderedMatchablesComparer}.
 * <p>
 * The beans must be in order as given by their key property
 * values.
 * 
 * @author rob
 *
 */
public class SortedBeanMatchables<T> implements Iterable<MatchableGroup> {
	
	/** The thing we are wrapping as a {@link MatchableGroup} */
	private final Iterable<? extends T> iterable;
	
	/** This will create the {@link Matchable} from the elements of the
	 * iterable. */
	private final MatchableFactory<T> factory;
	
	/** Used to create the Comparator for the key. */
	private final BeanPropertyComparerProvider comparerProvider;
	
	/**
	 * Create a new instance.
	 * 
	 * @param iterable
	 * @param factory
	 * @param comparerProivder
	 */
	public SortedBeanMatchables(Iterable<? extends T> iterable, 
			MatchableFactory<T> factory,
			BeanPropertyComparerProvider comparerProivder) {
		this.iterable = iterable;
		this.factory = factory;
		this.comparerProvider = comparerProivder;
	}
		
	@Override
	public Iterator<MatchableGroup> iterator() {
		
		return new Iterator<MatchableGroup>() {

			private MatchableMetaData metaData;
			
			private Comparator<Iterable<?>> keyComparator;
			
			private Matchable last;

			private Iterator<? extends T> iterator = iterable.iterator();
			
			@Override
			public boolean hasNext() {
				return last != null || iterator.hasNext();
			}
			
			@Override
			public MatchableGroup next() {
				
				final List<Matchable> group = new ArrayList<Matchable>();
				
				if (last != null) {
					group.add(last);
					last = null;
				}
				
				while (iterator.hasNext()) {
					
					if (Thread.currentThread().isInterrupted()) {
						break;
					}
					
					Matchable next = factory.createMatchable(iterator.next());

					if (metaData == null) {
						metaData = next.getMetaData();
						
						keyComparator = new KeyComparatorFactory(
								comparerProvider).createComparatorFor(
										metaData, metaData);
					}
					
					if (group.size() > 0) {
						
						Iterable<?> currentKeys = group.get(0).getKeys();
						Iterable<?> nextKeys = next.getKeys();
						
						int comparison = keyComparator.compare(
								currentKeys, nextKeys);

						if (comparison > 0) {
							throw new IllegalArgumentException(
									"Source must be in ascending order, however " +
									Iterables.toString(nextKeys) +
									"<" +
									Iterables.toString(currentKeys));
						}
						else if (comparison < 0) {
							last = next;
							break;	
						}
					}
					
					group.add(next);
				}

				if (group.size() == 0) {
					throw new NoSuchElementException(
							"No next - did you call hasNext()?");
				}
				
				return new ListMatchableGroup(group);
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}	
}
