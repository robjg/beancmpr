package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.matchables.ValueIterable.Value;

import java.util.Iterator;

/**
 * Provide an {@code Iterable} over a sets of values and their names.
 * 
 * @author rob
 *
 * @param <V> The type of the value.
 * 
 * @see ValuePairIterable
 */
public class ValueIterable<V> implements Iterable<Value<V>> {

	private final Iterable<String> propertyNames;
	private final Iterable<? extends V> values;
	
	public ValueIterable(Iterable<String> propertyNames,
			Iterable<? extends V> values) {
		
		this.propertyNames = propertyNames;
		this.values = values;
	}
	
	@Override
	public Iterator<Value<V>> iterator() {
		return new Iterator<>() {

			final Iterator<String> namesIterator = propertyNames.iterator();

			final Iterator<? extends V> iterator =
					(values == null ? null : values.iterator());

			@Override
			public boolean hasNext() {
				return namesIterator.hasNext();
			}

			@Override
			public Value<V> next() {

				final String name = namesIterator.next();

				final V value =
						(iterator == null ? null : iterator.next());

				return new Value<>() {

					@Override
					public V getValue() {
						return value;
					}

					@Override
					public String getPropertyName() {
						return name;
					}
				};
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
		
	public interface Value<T> {
		
		String getPropertyName();
		
		T getValue();
	}
	
}
