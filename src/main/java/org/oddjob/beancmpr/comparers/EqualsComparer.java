package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * A comparer that uses the equals method of an object.
 * <p>
 * If either or both of the objects are null the result of the compare
 * will be null.
 * 
 * @author rob
 *
 */
public class EqualsComparer implements Comparer<Object> {
	
	public Comparison compare(Object x, Object y) {
		if (x == null || y == null) {
			return null;
		}
		
		if (x.equals(y)) {
			return new AreEqual();
		}
		else {
			return new AreNotEqual(x, y);
		}
	}
	
	@Override
	public Class<Object> getType() {
		return Object.class;
	}
}
