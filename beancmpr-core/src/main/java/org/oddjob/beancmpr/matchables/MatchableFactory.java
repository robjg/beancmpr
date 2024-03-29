package org.oddjob.beancmpr.matchables;

/**
 * Creates a {@link Matchable} out of an object.
 * 
 * @author rob
 *
 * @param <T> The type of object.
 */
public interface MatchableFactory<T> {

	/**
	 * Create a Matchable.
	 * 
	 * @param object
	 * 
	 * @return The Matchable. Will be null if the object is null.
	 */
	Matchable createMatchable(T object);
	
}
