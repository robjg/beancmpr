package org.oddjob.beancmpr.multiitem;

import org.oddjob.beancmpr.Comparer;

/**
 * A {@link Comparer} that is able to compare many things such as from
 * a list or an array.
 * 
 * @author rob
 *
 * @param <T> The type of things being compared (Note this is not the type
 * of the elements).
 */
public interface MultiItemComparer<T> extends Comparer<T> {

	@Override
	MultiItemComparison<T> compare(T x, T y);
}
