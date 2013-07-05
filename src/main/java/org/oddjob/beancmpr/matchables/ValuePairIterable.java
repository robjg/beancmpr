package org.oddjob.beancmpr.matchables;

import java.util.Iterator;

import org.oddjob.beancmpr.matchables.ValuePairIterable.ValuePair;

/**
 * Provide an {@code Iterable} over two sets of values and their names.
 * 
 * @author rob
 *
 * @param <T> The type of the value.
 * 
 * @see ValueIterable
 */
public class ValuePairIterable<T> implements Iterable<ValuePair<T>> {

	private final Iterable<String> propertyNames;
	private final Iterable<? extends T> valuesX;
	private final Iterable<? extends T> valuesY;
	
	public ValuePairIterable(Iterable<String> propertyNames,
			Iterable<? extends T> valuesX, 
			Iterable<? extends T> valuesY) {
		
		this.propertyNames = propertyNames;
		this.valuesX = valuesX;
		this.valuesY = valuesY;
	}
	
	@Override
	public Iterator<ValuePair<T>> iterator() {
		return new Iterator<ValuePair<T>>() {
			
			final Iterator<String> namesIterator = propertyNames.iterator();
			
			final Iterator<? extends T> xIterator = 
				(valuesX == null ? null : valuesX.iterator());
			
			final Iterator<? extends T> yIterator =
				(valuesY == null ? null : valuesY.iterator());
			
			@Override
			public boolean hasNext() {
				return namesIterator.hasNext();
			}
			@Override
			public ValuePair<T> next() {
				
				final String name = namesIterator.next();
				
				final T x = 
					(xIterator == null ? null : xIterator.next());
				
				final T y = 
					(yIterator == null ? null : yIterator.next());
					
				return new ValuePair<T>() {
					
					@Override
					public T getValueY() {
						return y;
					}
					
					@Override
					public T getValueX() {
						return x;
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
		
	
	/**
	 * Provide a container object for the values.
	 *
	 * @param <T>
	 */
	public interface ValuePair<T> {
		
		public String getPropertyName();
		
		public T getValueX();
		
		public T getValueY();
	}
	
}
