package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;
import org.oddjob.beancmpr.composite.DefaultComparersByType;

/**
 * Compares to Arrays of Objects. Provide a default comparer for Arrays
 * in {@link DefaultComparersByType} and used from 
 * {@link BeanArrayComparerType} when there are no properties specified
 * for the compare.
 * 
 * @author rob
 *
 */
public class ArrayComparerFactory 
implements ComparerFactory<Object> {

	/** Comparers for the comparison of the elements. */
	private ComparersByTypeFactory comparersByType;
	
	@Override
	public ArrayComparer createComparerWith(
			ComparersByType defaultComparersByType) {
		
		if (defaultComparersByType == null) {
			throw new NullPointerException("No default comparers.");
		}
		
		ComparersByType comparersByType;
		if (this.comparersByType == null) {
			comparersByType = defaultComparersByType;
		}
		else {
			comparersByType =
				this.comparersByType.createComparersByTypeWith(
						defaultComparersByType);
		}
		
		return new ArrayComparer(comparersByType);
	}	
	
	public void setComparersByType(ComparersByTypeFactory comparersByType) {
		this.comparersByType = comparersByType;
	}

	public ComparersByTypeFactory getComparersByType() {
		return comparersByType;
	}
	
}
