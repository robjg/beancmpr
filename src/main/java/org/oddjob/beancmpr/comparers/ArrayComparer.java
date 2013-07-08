package org.oddjob.beancmpr.comparers;

import java.util.Arrays;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * Compares to Arrays of Objects.
 * 
 * @author rob
 *
 */
public class ArrayComparer 
implements HierarchicalComparer, Comparer<Object[]>{

	private ComparersByType comparers;
	
	private ComparersByType parentComparers;
	
	public void setComparersByType(ComparersByType comparersByType) {
		this.comparers = comparersByType;
	}

	@Override
	public void injectComparers(ComparersByType comparersByType) {
		this.parentComparers = comparersByType;
		
	}
	
	@Override
	public Comparison<Object[]> compare(final Object[] x, final Object[] y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
	
		IterableComparer<Object> iterableComparer = 
				new IterableComparer<Object>();
		iterableComparer.setComparersByType(comparers);
		iterableComparer.injectComparers(parentComparers);
		
		final IterableComparison<Object> iterableComparison = 
				iterableComparer.compare(Arrays.asList(x), Arrays.asList(y));
		
		return new Comparison<Object[]>() {

			@Override
			public Object[] getX() {
				return x;
			}

			@Override
			public Object[] getY() {
				return y;
			}

			@Override
			public int getResult() {
				return iterableComparison.getResult();
			}

			@Override
			public String getSummaryText() {
				return iterableComparison.getSummaryText();
			}
		};
	}
	
	@Override
	public Class<?> getType() {
		return Object[].class;
	}	
}
