package org.oddjob.beancmpr.composite;

import org.oddjob.beancmpr.Comparer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Comparer that uses a number of child compares to find
 * the correct comparer. The comparers are queried in the order
 * they are specified.
 * <p>
 * @author rob
 *
 */
public class CompositeComparersByType 
implements ComparersByType {

	private final List<ComparersByType> comparers =
            new ArrayList<>();
	
	public CompositeComparersByType(ComparersByType... comparers) {
		for (ComparersByType e : comparers) {
			if (e != null) {
				this.comparers.add(e);
			}
		}
	}
	
	@Override
	public <T> Comparer<T> comparerFor(Type type) {
		for (ComparersByType each : comparers) {
			Comparer<T> result = each.comparerFor(type);
			if (result != null) {
				return result;
			}
		}
		return null;
	}	
}
