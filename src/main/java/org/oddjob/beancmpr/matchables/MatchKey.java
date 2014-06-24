package org.oddjob.beancmpr.matchables;

public interface MatchKey<T extends MatchKey<T>> extends Comparable<T> {

	public Iterable<?> getKeys();
}
