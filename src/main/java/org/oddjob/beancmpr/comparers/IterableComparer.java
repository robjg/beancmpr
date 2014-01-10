package org.oddjob.beancmpr.comparers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * Compares to {@code Iterable}s.
 * 
 * @author rob
 *
 */
public class IterableComparer<T> 
implements HierarchicalComparer, MultiItemComparer<Iterable<? extends T>>{

	private ComparersByType comparers;
	
	private ComparersByType parentComparers;
	
	public void setComparersByType(ComparersByType comparersByType) {
		this.comparers = comparersByType;
	}
	
	@Override
	public void injectComparers(ComparersByType comparers) {
		this.parentComparers = comparers;
	}
	
	@Override
	public MultiItemComparison<Iterable<? extends T>> compare(
			Iterable<? extends T> x, Iterable<? extends T> y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
				
		List<T> yCopy = new ArrayList<T>();
		for (T o : y) {
			yCopy.add(o);
		}
		
		int same = 0;
		int different = 0;
		int xsMissing = 0;
		int ysMissing = 0;

		int xCount = 0;
		int yCount = yCopy.size();
		
		ComparersByType parentComparers = this.parentComparers;
		if (parentComparers == null) {
			parentComparers = new DefaultComparersByType();
		}
		ComparersByType comparers = new CompositeComparersByType(
				this.comparers, parentComparers);
		
		for (T eX : x) {
			
			if (!yCopy.isEmpty()) {
				
				boolean found = false;
				
				for (Iterator<T> itY = yCopy.iterator(); itY.hasNext(); ) {
					
					T eY = itY.next();
					
					@SuppressWarnings("unchecked")
					Comparer<T> comparer = (Comparer<T>) 
						comparers.comparerFor(eX.getClass());
					
					Comparison<T> eComparison = 
						comparer.compare(eX, eY);
					
					if (eComparison.getResult() == 0) {
						itY.remove();
						found = true;
						break;
					}
				}
				
				if (found) {
					++same;
				}
				else {
					++different;
				}			
			}
			
			++xCount;
		}
		
		if (yCount > xCount) {
			xsMissing = yCount - xCount;
		}
		
		if (xCount > yCount) {
			ysMissing = xCount - yCount;
			different -= ysMissing;
		}
		
		MultiItemComparisonCounts counts = 
				new MultiItemComparisonCountsImutable(
						xsMissing, 
						ysMissing, 
						different, 
						same, 
						xsMissing + ysMissing + different, 
						xsMissing + ysMissing + different + same);
		
		return new DelegatingMultiItemComparison<Iterable<? extends T>>(
				x, y, counts);
	}
	
	@Override
	public Class<?> getType() {
		return Iterable.class;
	}	
}
