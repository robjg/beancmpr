package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.multiitem.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Compares two {@code Iterable}s.
 * 
 * @see IterableBeansComparerType
 * 
 * @author rob
 *
 */
public class IterableComparer<T> 
implements MultiItemComparer<Iterable<T>>{

	private final ComparersByType comparers;
	
	public IterableComparer(ComparersByType comparers) {
		if (comparers == null) {
			throw new NullPointerException("No comparers");
		}
		this.comparers = comparers;
	}
	
	@Override
	public MultiItemComparison<Iterable<T>> compare(
			Iterable<T> x, Iterable<T> y) {

		Objects.requireNonNull(x, "X is null");
		Objects.requireNonNull(y, "Y is null.");
				
		List<T> yCopy = new ArrayList<>();
		for (T o : y) {
			yCopy.add(o);
		}
		
		int same = 0;
		int different = 0;
		int xsMissing = 0;
		int ysMissing = 0;

		int xCount = 0;
		int yCount = yCopy.size();
		
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
		
		return new MultiItemComparisonFromCounts<>(
				x, y, counts);
	}
	
	@Override
	public Class<?> getType() {
		return Iterable.class;
	}	
}
