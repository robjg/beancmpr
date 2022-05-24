package org.oddjob.beancmpr.matchables;


public class MockMatchable implements Matchable {

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
