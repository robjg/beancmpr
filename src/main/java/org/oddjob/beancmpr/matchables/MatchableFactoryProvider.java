package org.oddjob.beancmpr.matchables;

/**
 * Provides a {@link MatchableFactory}.
 * 
 * @see BeanMatchableFactoryProvider
 * @see MapMatchableFactoryProvider
 * 
 * @author rob
 *
 * @param <T>
 */
public interface MatchableFactoryProvider<T> {

	/**
	 * Provide a {@link MatchableFactory}.
	 * 
	 * @return A factory. never null.
	 */
	public MatchableFactory<T> provideFactory();
}
