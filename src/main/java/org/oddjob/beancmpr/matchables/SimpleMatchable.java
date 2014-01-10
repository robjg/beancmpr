package org.oddjob.beancmpr.matchables;

import java.util.List;

/**
 * A simple implementation of a {@link Matchable}.
 * 
 * @author rob
 */
public class SimpleMatchable implements Matchable {

	private final List<?> keys;
		
	private final List<?> values;
	
	private final List<?> others;
	
	private final MatchableMetaData metaData;
	
	/**
	 * Only Constructor.
	 * 
	 * @param keys The key values.
	 * @param values The values to compare.
	 * @param others Other values.
	 */
	public SimpleMatchable(List<?> keys, List<?> values, 
			List<?> others, MatchableMetaData metaData) {
		
		this.keys = keys;
		this.values = values;
		this.others = others;
		
		this.metaData = metaData; 
	}
	
	@Override
	public Iterable<?> getKeys() {
		return keys;
	}
	
	@Override
	public Iterable<?> getValues() {
		return values;
	}
	
	@Override
	public Iterable<?> getOthers() {
		return others;
	}

	public MatchableMetaData getMetaData() {
		return metaData;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ", key=" + keys + 
				", values = " + values + ", other=" + others;
	}
}