package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;

/**
 * Compares to {@code Iterable}s.
 * 
 * @author rob
 *
 */
public class IterableComparerType<T> 
implements ComparerFactory<Iterable<T>> {

	private ComparersByTypeFactory comparersByType;
	
	@Override
	public IterableComparer<T> createComparerWith(
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
		
		return new IterableComparer<T>(comparersByType);
	}
	
	public void setComparersByType(ComparersByTypeFactory comparersByType) {
		this.comparersByType = comparersByType;
	}

	public ComparersByTypeFactory getComparersByType() {
		return comparersByType;
	}
}
