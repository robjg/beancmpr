package org.oddjob.beancmpr.beans;

import java.lang.reflect.Array;
import java.util.Iterator;

public class ArrayIterable implements Iterable<Object> {
	
	final Object array;
	
	public ArrayIterable(Object array) {
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
