package org.oddjob.beancmpr.matchables;

import java.util.Iterator;

import org.oddjob.beancmpr.matchables.ValuePairIterable.ValuePair;

/**
 * Provide an {@code Iterable} over two sets of values and a shared common 
 * thing, such as their name.
 * 
 * @author rob
 *
 * @param <V> The type of the value.
 * 
 * @see ValueIterable
 */
public class ValuePairIterable<C, V> implements Iterable<ValuePair<C, V>> {

	private final Iterable<? extends C> common;
	private final Iterable<? extends V> valuesX;
	private final Iterable<? extends V> valuesY;
	
	public ValuePairIterable(Iterable<? extends C> propertyNames,
			Iterable<? extends V> valuesX, 
			Iterable<? extends V> valuesY) {
		
		this.common = propertyNames;
		this.valuesX = valuesX;
		this.valuesY = valuesY;
	}
	
	@Override
	public Iterator<ValuePair<C, V>> iterator() {
		return new Iterator<ValuePair<C, V>>() {
			
			final Iterator<? extends C> namesIterator = common.iterator();
			
			final Iterator<? extends V> xIterator = 
				(valuesX == null ? null : valuesX.iterator());
			
			final Iterator<? extends V> yIterator =
				(valuesY == null ? null : valuesY.iterator());
			
			@Override
			public boolean hasNext() {
				return namesIterator.hasNext();
			}
			@Override
			public ValuePair<C, V> next() {
				
				final C common = namesIterator.next();
				
				final V x = 
					(xIterator == null ? null : xIterator.next());
				
				final V y = 
					(yIterator == null ? null : yIterator.next());
					
				return new ValuePair<C, V>() {
					
					@Override
					public V getValueY() {
						return y;
					}
					
					@Override
					public V getValueX() {
						return x;
					}
					
					@Override
					public C getCommon() {
						return common;
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
	public interface ValuePair<C, T> {
		
		public C getCommon();
		
		public T getValueX();
		
		public T getValueY();
	}
	
}
