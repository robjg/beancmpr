package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.ComparisonStoppedException;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparisonFromCounts;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Compares two {@code Iterable}s of {@link MatchableGroup}s.
 * 
 * @See 
 * @author Rob.
 *
 */
public class OrderedMatchablesComparer 
implements MultiItemComparer<Iterable<MatchableGroup>> {

	private final ComparisonGatheringProcessor matchProcessor;
	
	private final BeanPropertyComparerProvider comparerProvider;
	
	private MatchableComparer matchableComparer;
	
	private Comparator<Iterable<?>> keyComparator;
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparerProvider To provide {@link Comparer}s. Must not be
	 * null.
	 * @param resultsHandler To process results. May be null.
	 */
	public OrderedMatchablesComparer(
			BeanPropertyComparerProvider comparerProvider,
			CompareResultsHandler resultsHandler) {
		
		if (comparerProvider == null) {
			throw new NullPointerException("ComparerProvider must be provded.");
		}
		
		this.matchProcessor = new ComparisonGatheringProcessor(resultsHandler);
		
		this.comparerProvider = comparerProvider;
	}
	
	@Override
	public MultiItemComparison<Iterable<MatchableGroup>> compare(
			Iterable<MatchableGroup> x, 
			Iterable<MatchableGroup> y)
	throws ComparisonStoppedException {
			
		Iterator<? extends MatchableGroup> matchablesX = x.iterator();
		Iterator<? extends MatchableGroup> matchablesY = y.iterator();
		
		MatchableGroup currentX = null;
		MatchableGroup currentY = null;
		
		while (true) {

			if (Thread.interrupted()) {
				throw new ComparisonStoppedException(getClass().getSimpleName() + 
						" detected interrupt.");
			}
			
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
			
			if (matchableComparer == null) {
				matchableComparer = new MatchableComparerFactory(
						comparerProvider).createComparerFor(
								currentX.getMetaData(), currentY.getMetaData());
			}
			if (keyComparator == null) {
				keyComparator = new KeyComparatorFactory(
						comparerProvider).createComparatorFor(
								currentX.getMetaData(), currentY.getMetaData());
			}
			
			int compareKeys = keyComparator.compare(
					currentX.getKeys(), currentY.getKeys());
			
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
				
		return new MultiItemComparisonFromCounts<>(
                x, y,
                matchProcessor);
	}
	
	/**
	 * Performs a match for two groups of {@link Matchable}s that are for 
	 * the same key.
	 * <p>
	 * 
	 * @param fromX Group X.
	 * @param fromY Group Y.
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
        while (itX.hasNext() && itY.hasNext()) {
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
	
	public CompareResultsHandler getMatchProcessor() {
		return matchProcessor;
	}

	public BeanPropertyComparerProvider getDifferentiatorProvider() {
		return comparerProvider;
	}
}
