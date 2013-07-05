package org.oddjob.beancmpr.matchables;

import java.util.Iterator;

import org.oddjob.beancmpr.matchables.ValueIterable.Value;

/**
 * Provide an {@code Iterable} over two sets of values and their names.
 * 
 * @author rob
 *
 * @param <T>
 * 
 * @see ValuePairIterable
 */
public class ValueIterable<T> implements Iterable<Value<T>> {

	private final Iterable<String> propertyNames;
	private final Iterable<? extends T> values;
	
	public ValueIterable(Iterable<String> propertyNames,
			Iterable<? extends T> values) {
		
		this.propertyNames = propertyNames;
		this.values = values;
	}
	
	@Override
	public Iterator<Value<T>> iterator() {
		return new Iterator<Value<T>>() {
			
			final Iterator<String> namesIterator = propertyNames.iterator();
			
			final Iterator<? extends T> iterator = 
				(values == null ? null : values.iterator());
			
			@Override
			public boolean hasNext() {
				return namesIterator.hasNext();
			}
			@Override
			public Value<T> next() {
				
				final String name = namesIterator.next();
				
				final T value = 
					(iterator == null ? null : iterator.next());
				
				return new Value<T>() {
					
					@Override
					public T getValue() {
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
		
		public String getPropertyName();
		
		public T getValue();
	}
	
}
