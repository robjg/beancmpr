package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.MatchDefinition;

/**
 * Meta data that describe a {@link Matchable}.
 * <p>
 * This is a {@link MatchDefinition} that is able to provide a type
 * for each property.
 * 
 * @author rob
 *
 */
public interface MatchableMetaData extends MatchDefinition {

	/**
	 * Get the property type for a given property name.
	 * 
	 * @param name The name of the property.
	 * @return The type. Must not be null if the property exists.
	 */
	Class<?> getPropertyType(String name);
}
