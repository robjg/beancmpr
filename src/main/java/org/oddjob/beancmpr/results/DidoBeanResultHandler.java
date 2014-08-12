package org.oddjob.beancmpr.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.MatchableMetaData;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.matchables.ValueIterable;

/**
 * @oddjob.description A handler that will generate results in format
 * used by Dido. Dido (Data In Data Out) is currently experimental.
 * It can be found at <a href="http://sourceforge.net/projects/di-do>
 * http://sourceforge.net/projects/di-do</a>.
 * 
 * @author rob
 *
 */
public class DidoBeanResultHandler 
implements BeanCmprResultsHandler, PlaysWithBeanbus {

	/**
	 * @oddjob.property
	 * @oddjob.description A collection that result beans will be added to.
	 * @oddjob.required No.
	 */
	private Collection<? super Object> out;
		
	/**
	 * @oddjob.property
	 * @oddjob.description If true then result beans will not be created
	 * when a comparison results in a match. If false result beans
	 * for all comparisons will be created.
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean ignoreMatches;
	
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		if (ignoreMatches && comparison.getResult() == 0) {
			return;
		}
		out.add(createComparisonResult(comparison));
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		for (Matchable y : ys.getGroup()) {			
			out.add(createXMissingResult(y));
		}
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		for (Matchable x : xs.getGroup()) {
			out.add(createYMissingResult(x));
		}
	}	

	public Collection<? super Object> getOut() {
		return out;
	}

	@Override
	public void setOut(Collection<? super Object> out) {
		this.out = out;
	}

	public boolean isIgnoreMatches() {
		return ignoreMatches;
	}

	public void setIgnoreMatches(boolean ignoreMatches) {
		this.ignoreMatches = ignoreMatches;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + 
				", ignoreMatches=" + ignoreMatches;
	}
	
	protected Object createXMissingResult(Matchable y) {
				
		List<XMissingComparison> comparisons = 
				new ArrayList<XMissingComparison>();
		
		for (Object yValue : y.getValues()) {
			comparisons.add(new XMissingComparison(yValue));
		}
		
		MatchableMetaData metaData = y.getMetaData();
				
		return new DidoResultBean(
				metaData,
				MatchResultType.Type.X_MISSING,
				keyMap(metaData.getKeyProperties(),
						y.getKeys()),
				comparisonMap(metaData.getValueProperties(),
						comparisons)
			);
	}
	
	protected Object createYMissingResult(Matchable x) {

		List<YMissingComparison> comparisons = 
				new ArrayList<YMissingComparison>();
		
		for (Object xValue : x.getValues()) {
			comparisons.add(new YMissingComparison(xValue));
		}
		
		MatchableMetaData metaData = x.getMetaData();
		
		return new DidoResultBean(
				metaData,
				MatchResultType.Type.Y_MISSING,
				keyMap(metaData.getKeyProperties(),
						x.getKeys()),
				comparisonMap(metaData.getValueProperties(),
						comparisons)
			);
	}
	
	
	protected Object createComparisonResult(
			MultiValueComparison<Matchable> matchableComparison) {

		MatchableMetaData metaData = 
				matchableComparison.getX().getMetaData();
		
		return new DidoResultBean(
				metaData,
				matchableComparison.getResult() == 0 ? 
						MatchResultType.Type.EQUAL : 
							MatchResultType.Type.NOT_EQUAL,
				keyMap(metaData.getKeyProperties(), 
						matchableComparison.getX().getKeys()),
				comparisonMap(metaData.getValueProperties(),
						matchableComparison.getValueComparisons())
			);
	}
	
	
	private Map<String, Object> keyMap(Iterable<String> keyProperties, 
			Iterable<?> keyValues) {
		
		Map<String, Object> keyMap = new LinkedHashMap<String, Object>();

		ValueIterable<Object> keys = 
				new ValueIterable<Object>(
					keyProperties, 
					keyValues);
		
		for (ValueIterable.Value<Object> set : keys) {

			Object value = set.getValue();
			
			keyMap.put(set.getPropertyName(), value);
		}
		
		return keyMap;
	}
	
	private Map<String, Comparison<?>> comparisonMap(
			Iterable<String> comparisonProperties, 
			Iterable<? extends Comparison<?>> comparisonValues) {
		
		Map<String, Comparison<?>> comparisonMap = 
				new LinkedHashMap<String, Comparison<?>>();

		ValueIterable<Comparison<?>> comparisons = 
				new ValueIterable<Comparison<?>>(
					comparisonProperties, 
					comparisonValues);
		
		for (ValueIterable.Value<Comparison<?>> set : comparisons) {

			Comparison<?> value = set.getValue();
			
			comparisonMap.put(set.getPropertyName(), value);
		}
		
		return comparisonMap;
	}
	
	public static class XMissingComparison implements Comparison<Object> {

		private final Object yValue;
		
		public XMissingComparison(Object yValue) {
			this.yValue = yValue;
		}
		
		@Override
		public Object getX() {
			return null;
		}
		
		@Override
		public Object getY() {
			return yValue;
		}
		
		@Override
		public int getResult() {
			return 1;
		}
		
		@Override
		public String getSummaryText() {
			return null;
		}
	}
	
	public static class YMissingComparison implements Comparison<Object> {

		private final Object xValue;
		
		public YMissingComparison(Object xValue) {
			this.xValue = xValue;
		}
		
		@Override
		public Object getX() {
			return xValue;
		}
		
		@Override
		public Object getY() {
			return null;
		}
		
		@Override
		public int getResult() {
			return -1;
		}
		
		@Override
		public String getSummaryText() {
			return null;
		}
	}
}
