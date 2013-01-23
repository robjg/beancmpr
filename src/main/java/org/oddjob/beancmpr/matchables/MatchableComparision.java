package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.List;

import org.oddjob.beancmpr.Comparison;

/**
 * The result of comparing to {@link Matchable}s.
 * 
 * @author rob
 *
 */
public class MatchableComparision
implements MultiValueComparison<Matchable>  {
	
	private final List<Comparison<?>> comparisons = 
			new ArrayList<Comparison<?>>();
	
	private int result;
			
	private int equal;
	
	private final Matchable x;
	
	private final Matchable y;
		
	public MatchableComparision(
			Matchable x, Matchable y,
			Iterable<? extends Comparison<?>> comparisons) {
			
		this.x = x;
		this.y = y;
		
		for (Comparison<?> comparison : comparisons) {
			this.comparisons.add(comparison);
			
			int comparisonResult = comparison.getResult();
			if (comparisonResult == 0) {
				equal++;
			}
			else if (result == 0) {
				result = comparisonResult;
			}
		}
	}
	
	@Override
	public Matchable getX() {
		return x;
	}
	
	@Override
	public Matchable getY() {
		return y;
	}
	
	@Override
	public int getResult() {
		return result;
	}
	
	@Override
	public Iterable<Comparison<?>> getValueComparisons() {
		return comparisons;
	}
	
	@Override
	public String getSummaryText() {
		return "" + equal + "/" + comparisons.size() + " values equal";
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + getSummaryText();
	}
}
