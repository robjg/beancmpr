package org.oddjob.beancmpr.matchables;

import java.util.Iterator;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.beans.BeanComparerProvider;
import org.oddjob.beancmpr.comparers.MultiItemComparison;
import org.oddjob.beancmpr.comparers.MultiItemComparisonStats;

/**
 * Compares two {@code Iterable}s of {@link MatchableGroup}s.
 * 
 * @See 
 * @author Rob.
 *
 */
public class OrderedMatchablesComparer 
implements Comparer<Iterable<MatchableGroup>> {
	
	private final DefaultMatchableComparer matchableComparer;
	
	private final ComparisonGatheringProcessor matchProcessor;
	
	public OrderedMatchablesComparer(
			PropertyAccessor accessor,
			BeanComparerProvider comparerProvider,
			BeanCmprResultsHandler matchNotifier) {
		
		this.matchableComparer = new DefaultMatchableComparer(comparerProvider);
		
		this.matchProcessor = new ComparisonGatheringProcessor(matchNotifier);
	}
	
	public MultiItemComparisonStats getMultiItemComparisonStats() {
		return matchProcessor;
	}
	
	@Override
	public MultiItemComparison<MatchableGroup> compare(
			Iterable<MatchableGroup> x, 
			Iterable<MatchableGroup> y) {
			
		Iterator<MatchableGroup> matchablesX = x.iterator();
		Iterator<MatchableGroup> matchablesY = y.iterator();
		
		MatchableGroup currentX = null;
		MatchableGroup currentY = null;
		
		while (true) {

			if (currentX == null && matchablesX.hasNext()) {
				currentX = matchablesX.next();
			}
			
			if (currentY == null && matchablesY.hasNext()) {
				currentY = matchablesY.next();
			}
			
			if (currentX == null && currentY == null) {
				break;
			}
			
			if (currentX == null) {
				matchProcessor.xMissing(currentY);
				currentY = null;
				continue;
			}
			
			if (currentY == null) {
				matchProcessor.yMissing(currentX);
				currentX = null;
				continue;
			}
			
			int compareKeys = currentX.getKey().compareTo(currentY.getKey());
			
			if (compareKeys < 0) {
				matchProcessor.yMissing(currentX);
				currentX = null;
				continue;				
			}
			
			if (compareKeys > 0) {
				matchProcessor.xMissing(currentY);
				currentY = null;
				continue;				
			}
			
			groupMatch(currentX, currentY);
			
			currentX = null;
			currentY = null;
		}
				
		return new MultiItemComparison<MatchableGroup>(x, y,
				matchProcessor);
	}
	
	/**
	 * Performs a match for two groups of {@link Matchable}s that are for 
	 * the same key.
	 * <p>
	 * 
	 * @param fromX
	 * @param fromY
	 */
	protected void groupMatch(MatchableGroup fromX, MatchableGroup fromY) {
		
		Iterable<Matchable> groupX = fromX.getGroup();
		Iterable<Matchable> groupY = fromY.getGroup();
		
		// Remove lines that match from each group.
		for (Iterator<Matchable> itX = groupX.iterator(); itX.hasNext(); ) {
			Matchable matchableX = itX.next();
			
			for (Iterator<Matchable> itY = groupY.iterator(); itY.hasNext(); ) {
				
				Matchable matchableY = itY.next();
				
				MultiValueComparison<Matchable> comparison = 
					matchableComparer.compare(matchableX, matchableY);
				
				if (comparison.getResult() == 0){
					
					matchProcessor.compared(comparison);
					
					itY.remove();
					itX.remove();
					
					break;
				}
			}
		}
		
		Iterator<Matchable> itX = groupX.iterator();
		Iterator<Matchable> itY = groupY.iterator();
		
		// Notify differences for remaining matchables in the order 
		// provided and remove until one group is empty.
		for (; itX.hasNext() && itY.hasNext(); ) {
			Matchable matchableX = itX.next();
			Matchable matchableY = itY.next();	
			
			matchProcessor.compared(matchableComparer.compare(
					matchableX, matchableY));
			
			itY.remove();
			itX.remove();
		}
		
		// If any left in X then they are missing from Y.
		if (itX.hasNext()) {
			matchProcessor.yMissing(fromX);
		}
		
		// If any left in Y then they are missing from X.
		if (itY.hasNext()) {
			matchProcessor.xMissing(fromY);
		}
	}
	
	@Override
	public Class<?> getType() {
		return Iterable.class;
	}
	
	public BeanCmprResultsHandler getMatchProcessor() {
		return matchProcessor;
	}

	public BeanComparerProvider getDifferentiatorProvider() {
		return matchableComparer.getComparerProvider();
	}
}
