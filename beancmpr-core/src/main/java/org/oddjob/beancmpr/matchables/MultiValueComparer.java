package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparer;

/**
 * A {@link Comparer} that compares many values of two things. The things
 * will most likely be the properties of two objects but could be the
 * columns in two rows etc.
 * 
 * @author rob
 *
 * @param <T> The type of the things being compared.
 */
public interface MultiValueComparer<T> extends Comparer<T> {

	@Override
	public MultiValueComparison<T> compare(T x, T y);
}
