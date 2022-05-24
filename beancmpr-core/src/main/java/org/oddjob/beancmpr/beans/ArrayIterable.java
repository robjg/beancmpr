package org.oddjob.beancmpr.beans;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * Provide an Iterable from an Array.
 * 
 * @author rob
 *
 */
public class ArrayIterable implements Iterable<Object> {
	
	final Object array;
	
	/**
	 * Create a new Instance.
	 * 
	 * @param array An object that must be an array.
	 */
	public ArrayIterable(Object array) {
		if (array == null) {
			throw new NullPointerException("Array is null.");
		}
		
		if (!array.getClass().isArray()) {
			throw new IllegalArgumentException("Object [" + array + 
					"] of class [" + array.getClass().getName() + 
					"] is not an array.");
		}
		
		this.array = array;
	}
	
	@Override
	public Iterator<Object> iterator() {

		return new Iterator<Object>() {
			
			int index;

			@Override
			public boolean hasNext() {
				
				return index < Array.getLength(array);
			}
			
			@Override
			public Object next() {
				return Array.get(array, index++);
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
