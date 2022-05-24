package org.oddjob.beancmpr.matchables;

import java.util.List;

import org.oddjob.arooa.utils.Iterables;

public class ListMatchableGroup implements MatchableGroup {

	private final List<Matchable> matchables;
	
	public ListMatchableGroup(
			List<Matchable> matchables) {
		this.matchables = matchables;
		
		if (matchables.isEmpty()) {
			throw new IllegalArgumentException(
					"A Matchable Group must contain at least one Matchable");
		}
	}
	
	@Override
	public Iterable<?> getKeys() {
		return matchables.get(0).getKeys();
	}
	
	@Override
	public Iterable<Matchable> getGroup() {
		return matchables;
	}
	
	@Override
	public int getSize() {
		return matchables.size();
	}
	
	@Override
	public MatchableMetaData getMetaData() {
		return matchables.get(0).getMetaData();
	}

	@Override
	public String toString() {
		return MatchableGroup.class.getSimpleName() + 
			", " + Iterables.toString(getKeys());
	}
}
