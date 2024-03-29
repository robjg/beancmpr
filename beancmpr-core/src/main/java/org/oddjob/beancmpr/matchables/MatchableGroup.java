package org.oddjob.beancmpr.matchables;

/**
 * A group of {@link Matchable}s that have the same keys.
 * 
 * @author rob
 *
 */
public interface MatchableGroup {

	/**
	 * Get the keys for this group.
	 * 
	 * @return The keys. Will not be null.
	 */
	ImmutableCollection<Object> getKeys();
	
	/**
	 * Get the meta data for {@link Matchable}s in this group.
	 * 
	 * @return The meta data. Will not be null.
	 */
	MatchableMetaData getMetaData();
	
	/**
	 * Get the group.
	 * 
	 * @return An Iterable over the group. A group must contain at least
	 * one {@link Matchable}.
	 */
	ImmutableCollection<Matchable> getGroup();
	
	/**
	 * Get the size of the group.
	 * 
	 * @return The size.
	 */
	int getSize();
}
