package org.oddjob.beancmpr.matchables;

import java.util.Map;
import java.util.Map.Entry;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.MatchDefinition;

/**
 * Provide a {@link MatchableFactory} for a Map.
 * 
 * @see MapMatchableFactory
 * 
 * @author rob
 *
 * @param <T> The type of factory provided
 */
public class MapMatchableFactoryProvider<K, V>
implements MatchableFactoryProvider<Map.Entry<K, V>>{

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
	public MapMatchableFactoryProvider(
			MatchDefinition matchDefinition,
			PropertyAccessor propertyAccessor) {
		
		this.matchDefinition = matchDefinition;
		this.propertyAccessor = propertyAccessor;
	}
	
	@Override
	public MatchableFactory<Entry<K, V>> provideFactory() {
		
		return new MapMatchableFactory<>(matchDefinition, propertyAccessor);
	}
}
