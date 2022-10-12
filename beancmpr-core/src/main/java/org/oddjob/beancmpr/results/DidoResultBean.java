package org.oddjob.beancmpr.results;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

import java.util.Map;

public class DidoResultBean {

	private final MatchableMetaData metaData;
	
	private final int resultType;
	
	private final Map<String, Object> keys;
	
	private final Map<String, Comparison<?>> comparisons;

	public DidoResultBean(
			MatchableMetaData metaData,
			MatchResultType resultType,
			Map<String, Object> keys,
			Map<String, Comparison<?>> comparisons) {
		
		this.metaData = metaData;
		this.resultType = resultType.ordinal();
		this.keys = keys;
		this.comparisons = comparisons;
	}
	
	public MatchableMetaData getMetaData() {
		return metaData;
	}
	
	public int getResultType() {
		return resultType;
	}

	public Map<String, Object> getKeys() {
		return keys;
	}

	public Map<String, Comparison<?>> getComparisons() {
		return comparisons;
	}
}
