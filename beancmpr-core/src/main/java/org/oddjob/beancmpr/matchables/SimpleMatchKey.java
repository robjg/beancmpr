package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;

import java.util.Comparator;
import java.util.function.Function;

/**
 * A simple implementation of {@link MatchKey}.
 * 
 * @author rob
 *
 */
public class SimpleMatchKey implements MatchKey<SimpleMatchKey> {

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

	public static Function<Iterable<?>, SimpleMatchKey>
	creatorFrom(BeanPropertyComparerProvider comparerProvider,
                                                       MatchableMetaData metaData) {
		Comparator<Iterable<?>> keyComparator = new KeyComparatorFactory(
				comparerProvider).createComparatorFor(
				metaData, metaData);

		return keys -> new SimpleMatchKey(keys, keyComparator);
	}

	@Override
	public Iterable<?> getKeys() {
		return keys;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == this)
		    return true;
		
		if (!(other instanceof SimpleMatchKey))
		    return false;

		return compareTo((SimpleMatchKey) other) == 0;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
        for (Object obj : keys) {
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
		return hashCode;
	}

	@Override
	public int compareTo(SimpleMatchKey other) throws ClassCastException {
		
		return comparator.compare(keys, other.getKeys());
	}	
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ", keys=" + keys;
	}
}
