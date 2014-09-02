package org.oddjob.beancmpr.matchables;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.MatchDefinition;

/**
 * Provide a {@link MatchableFactory} for a Java Bean.
 * 
 * @see BeanMatchableFactory
 * 
 * @author rob
 *
 * @param <T> The type of factory provided
 */
public class BeanMatchableFactoryProvider<T>
implements MatchableFactoryProvider<T> {
	
	/** The definition for the match. */ 
	private final MatchDefinition matchDefinition;

	/** Provide access to the properties of the bean. */
	private final PropertyAccessor propertyAccessor;
	
	/**
	 * Create a new instance.
	 * 
	 * @param propertyAccessor
	 * @param matchDefinition
	 */
	public BeanMatchableFactoryProvider(
			MatchDefinition matchDefinition,
			PropertyAccessor propertyAccessor) {
		
		this.matchDefinition = matchDefinition;
		this.propertyAccessor = propertyAccessor;
	}
	
	@Override
	public MatchableFactory<T> provideFactory() {
		return new BeanMatchableFactory<T>(
				matchDefinition, propertyAccessor);
	}
}
