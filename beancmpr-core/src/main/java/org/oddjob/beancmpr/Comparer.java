package org.oddjob.beancmpr;

/**
 * Something that is able to compare two things of a type T.
 * 
 * @author Rob
 *
 * @param <T> The type.
 */
public interface Comparer<T> {

	/**
	 * Compare two things.
	 * 
	 * @param x One thing.
	 * @param y The other thing.
	 * 
	 * @return A Comparison. Will not be null.
	 * 
	 */
	Comparison<T> compare(T x, T y);
	
	/**
	 * The type of the things.  
	 * <p>
	 * Note that we would have liked the return type
	 * to be {@code Class<T>} but we could not then have had a 
	 * {@code Comparer<Iterable<MatchableGroup>>} because it's not possible to
	 * return a type of {@code Class<Iterable<MatchableGroup>>} in Java.
	 * If there is a solution to this then please let us know.
	 * 
	 * @return The type. Must not be null.
	 */
	Class<?> getType();
}
