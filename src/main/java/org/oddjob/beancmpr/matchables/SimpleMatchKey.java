package org.oddjob.beancmpr.matchables;

import java.util.Comparator;
import java.util.Iterator;

/**
 * A simple implementation of {@link MatchKey}.
 * 
 * @author rob
 *
 */
public class SimpleMatchKey implements Comparable<SimpleMatchKey>{

	private final Iterable<?> keys;

	private final Comparator<Iterable<?>> comparator;
	
	/**
	 * Constructor.
	 * 
	 * @param components The component values of the key.
	 */
	public SimpleMatchKey(Iterable<?> components,
			Comparator<Iterable<?>> comparator) {
		this.keys = components;
		this.comparator = comparator;
	}
	
	public Iterable<?> getKeys() {
		return keys;
	}
	
	public boolean equals(Object other) {
		if (other == this)
		    return true;
		
		if (!(other instanceof SimpleMatchKey))
		    return false;

		return compareTo((SimpleMatchKey) other) == 0;
	}

	public int hashCode() {
		int hashCode = 1;
		Iterator<?> i = keys.iterator();
		while (i.hasNext()) {
		    Object obj = i.next();
		    hashCode = 31 * hashCode + (obj==null ? 0 : obj.hashCode());
		}
		return hashCode;
	}

	public int compareTo(SimpleMatchKey other) throws ClassCastException {
		
		return comparator.compare(keys, other.getKeys());
	}	
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ", keys=" + keys;
	}
}
