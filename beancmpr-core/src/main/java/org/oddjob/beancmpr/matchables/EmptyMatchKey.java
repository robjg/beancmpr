package org.oddjob.beancmpr.matchables;

import java.util.Collections;

public class EmptyMatchKey implements MatchKey<EmptyMatchKey> {

	@Override
	public Iterable<?> getKeys() {
		return Collections.emptyList();
	}
	
	@Override
	public int compareTo(EmptyMatchKey o) {
		return 0;
	}
}
