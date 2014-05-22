package org.oddjob.beancmpr.composite;

import java.util.ArrayList;
import java.util.List;

import org.oddjob.beancmpr.Comparer;

/**
 * A {@link Comparer that uses a number of child compares to find
 * the correct comparer. The comparers are queried in the order
 * they are specified.
 * 
 * @author rob
 *
 */
public class CompositeComparersByType 
implements ComparersByType {

	private final List<ComparersByType> comparers = 
			new ArrayList<ComparersByType>();
	
	public CompositeComparersByType(ComparersByType... comparers) {
		for (ComparersByType e : comparers) {
			if (e != null) {
				this.comparers.add(e);
			}
		}
	}
	
	@Override
	public <T> Comparer<T> comparerFor(Class<T> type) {
		for (ComparersByType each : comparers) {
			Comparer<T> result = each.comparerFor(type);
			if (result != null) {
				return result;
			}
		}
		return null;
	}	
}
