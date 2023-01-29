package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.MatchDefinition;

/**
 * Something that's matchable against another <code>Matchable</code> 
 * for the same {@link MatchDefinition}.
 * 
 * @author Rob
 *
 */
public interface Matchable {

	/**
	 * Get the keys.
	 * 
	 * @return The keys. Never null.
	 * 
	 */
	ImmutableCollection<Object> getKeys();
	
	/**
	 * Get the values for comparison.
	 * 
	 * @return The values to compare. Never null.
	 * 
	 */
	ImmutableCollection<Object> getValues();
	
	/**
	 * Get the others.
	 * 
	 * @return The others. Never null.
	 * 
	 */
	ImmutableCollection<Object> getOthers();
	
	/**
	 * Get the meta data that describe this matchable.
	 * 
	 * @return The meta data. Will not be null.
	 */
	MatchableMetaData getMetaData();
}
