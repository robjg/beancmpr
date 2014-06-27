package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NullComparison;

/**
 * Provide a comparison for two objects that are known to be comparable
 * using the given comparer.
 * 
 * 
 * @author rob
 *
 * @param <T>
 */
public class NullSafeComparer<T> {

	private final Comparer<T> comparer;

	private final String propertyName;
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparer A comparer. Must not be null.
	 * @param propertyName The property name. Used for error message.
	 */
	public NullSafeComparer(Comparer<T> comparer, String propertyName) {
		if (comparer == null) {
			throw new NullPointerException("No Comparer");
		}
		this.comparer = comparer;
		this.propertyName = propertyName;
	}
	
	/**
	 * Provide a comparison based on the comparer.
	 * 
	 * @param rawX The X as an Object. It will be cast to the given 
	 *             comparer type. Maybe be null.
	 * @param rawY The Y as an Object. It will be cast to a given 
	 *             comparer type. Maybe be null.
	 * 
	 * @return A Comparison of the correct type.
	 */
	public Comparison<T> castAndCompare(Object rawX, Object rawY) {
		
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) comparer.getType();

		T x = null;
		T y = null;
			
		try {
			x = type.cast(rawX);
		}
		catch (ClassCastException e) {
			throw new ClassCastException("Failed to cast X property [" + propertyName + 
					"] value [" + rawX + "] to class [" + type + "]");
		}
				
		try {
			y = type.cast
					(rawY);
		}
		catch (ClassCastException e) {
			throw new ClassCastException("Failed to cast Y property [" + propertyName + 
					"] value [" + rawY + "] to type [" + type + "]");
		}
			
		if (x == null || y == null) {
			return new NullComparison<T>(x, y);
		}
		else {
			return comparer.compare(x, y);
		}
	}
	
	public Comparer<T> getComparer() {
		return comparer;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
}
