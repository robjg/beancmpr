package org.oddjob.beancmpr.matchables;

/**
 * Provides a {@link MatchableFactory}.
 * 
 * @see BeanMatchableFactoryProvider
 * @see MapMatchableFactoryProvider
 * 
 * @author rob
 *
 * @param <T> The type to create a Matchable from.
 */
public interface MatchableFactoryProvider<T> {

	/**
	 * Provide a {@link MatchableFactory}.
	 * 
	 * @return A factory. never null.
	 */
	MatchableFactory<T> provideFactory();
}
