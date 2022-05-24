package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.BeanCompareJob;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparer;
import org.oddjob.beancmpr.matchables.MatchableComparerFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.matchables.MultiValueComparer;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;

/**
 * A comparer that is able to compare two beans.
 * <p>
 * Note that this comparer implements {@link MultiItemComparer} even though
 * it will only ever one of each bean. This is so {@link BeanComparerType}
 * can implement {@link MultiItemComparerFactory} and so be one of
 * the comparer types that can be provided to {@link BeanCompareJob} to
 * allow this job to compare just two objects as well as lists and maps.
 * 
 * @see BeanComparerType
 * 
 * @author rob
 *
 */
public class BeanComparer<T> 
implements MultiItemComparer<T>, MultiValueComparer<T> {

	private final BeanPropertyComparerProvider comparerProvider;
	
	private final BeanCmprResultsHandler resultHandler;
	
	/** The thing that will create the {@link Matchable}s from the beans. */
	private final MatchableFactory<T> matchableFactory;
	
	/** The thing that will compare the two {@link Matchable}s created by
	 * the {@link MatchableFactory}. */
	private MatchableComparer comparer;
	
	/**
	 * Create a new instance.
	 * 
	 * @param matchableFactory
	 * @param comparerProvider
	 * @param resultHandler
	 */
	public BeanComparer(MatchableFactory<T> matchableFactory,
			BeanPropertyComparerProvider comparerProvider,
			BeanCmprResultsHandler resultHandler) {
		this.matchableFactory = matchableFactory;
		this.comparerProvider = comparerProvider;
		this.resultHandler = resultHandler;
	}
	
	@Override
	public BeanComparison<T> compare(T x, T y) {

		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}

		if (comparerProvider == null) {
			throw new IllegalStateException("Parent Comparers Not Yet Injected!");
		}
		
		Matchable matchableX = 
			matchableFactory.createMatchable(x);
		Matchable matchableY = 
			matchableFactory.createMatchable(y);

		if (comparer == null) {
			comparer = new MatchableComparerFactory(
						comparerProvider).createComparerFor(
								matchableX.getMetaData(), 
								matchableY.getMetaData());							
		}

		MultiValueComparison<Matchable> comparison =
				comparer.compare(matchableX, matchableY);
		
		if (resultHandler != null) {
			resultHandler.compared(comparison);
		}

		return new BeanComparison<T>(x, y, comparison);
	}					
	
	@Override
	public Class<Object> getType() {
		return Object.class;
	}
	
}
