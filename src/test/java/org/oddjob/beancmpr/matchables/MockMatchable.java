package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.matchables.MatchKey;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

public class MockMatchable implements Matchable {

	@Override
	public MatchKey getKey() {
		throw new RuntimeException("Unexpected from " + getClass().getName());
	}
	
	@Override
	public Iterable<?> getKeys() {
		throw new RuntimeException("Unexpected from " + getClass().getName());
	}
	
	@Override
	public MatchableMetaData getMetaData() {
		throw new RuntimeException("Unexpected from " + getClass().getName());
	}
	
	@Override
	public Iterable<?> getOthers() {
		throw new RuntimeException("Unexpected from " + getClass().getName());
	}
	
	@Override
	public Iterable<?> getValues() {
		throw new RuntimeException("Unexpected from " + getClass().getName());
	}
}
