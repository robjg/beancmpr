package org.oddjob.beancmpr.comparers;

import java.util.Arrays;

import org.oddjob.beancmpr.Comparison;

/**
 * Compares to Arrays of Objects.
 * 
 * @author rob
 *
 */
public class ArrayComparer 
implements HierarchicalComparer<Object[]>{

	private final IterableComparer iterableComparer
		= new IterableComparer();
	
	@Override
	public void setComparersByType(ComparersByType comparersByType) {
		iterableComparer.setComparersByType(comparersByType);
	}
	
	@Override
	public Comparison<Object[]> compare(final Object[] x, final Object[] y) {
		
		if (x == null || y == null) {
			return null;
		}
	
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
