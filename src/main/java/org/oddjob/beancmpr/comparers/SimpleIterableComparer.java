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
public class SimpleIterableComparer<T> 
implements HierarchicalComparer<Iterable<T>>{

	private ComparersByType comparers;
	
	@Override
	public void setComparersByType(ComparersByType comparersByType) {
		this.comparers = comparersByType;
	}
	
	@Override
	public MultiItemComparison<T> compare(Iterable<T> x, Iterable<T> y) {
		
		if (x == null || y == null) {
			return null;
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
		
		return new MultiItemComparison<T>(x, y,
				xsMissing, ysMissing, different, same);
	}
	
	@Override
	public Class<?> getType() {
		return Iterable.class;
	}	
}
