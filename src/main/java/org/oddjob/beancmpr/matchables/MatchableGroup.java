package org.oddjob.beancmpr.matchables;

/**
 * A group of {@link Matchable}s that have the same {@link MatchKey}.
 * 
 * @author rob
 *
 */
public interface MatchableGroup {

	public MatchKey getKey();
	
	public Iterable<Matchable> getGroup();
	
	public int getSize();
}
