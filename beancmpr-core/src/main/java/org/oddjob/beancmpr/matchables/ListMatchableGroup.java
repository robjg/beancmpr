package org.oddjob.beancmpr.matchables;

import org.oddjob.arooa.utils.Iterables;

import java.util.List;

public class ListMatchableGroup implements MatchableGroup {

	private final ImmutableCollection<Matchable> matchables;
	
	public ListMatchableGroup(
			List<Matchable> matchables) {
		this.matchables = ImmutableCollection.of(matchables);
		
		if (matchables.isEmpty()) {
			throw new IllegalArgumentException(
					"A Matchable Group must contain at least one Matchable");
		}
	}
	
	@Override
	public ImmutableCollection<Object> getKeys() {
		return matchables.get(0).getKeys();
	}
	
	@Override
	public ImmutableCollection<Matchable> getGroup() {
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
