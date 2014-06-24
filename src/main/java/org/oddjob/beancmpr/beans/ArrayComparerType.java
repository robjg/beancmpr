package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;

/**
 * Compares to Arrays of Objects.
 * 
 * @author rob
 *
 */
public class ArrayComparerType 
implements ComparerFactory<Object[]> {

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
