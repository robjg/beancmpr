package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.PropertyTypeHelper;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;

/**
 * Create a {@code Comparator} for the keys of {@link Matchable}s based
 * on their {@link MatchableMetaData}.
 * 
 * @author rob
 *
 */
public class KeyComparatorFactory {

	private final BeanPropertyComparerProvider comparerProvider;
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparerProvider Used to provide comparisons for the key
	 * fields.
	 */
	public KeyComparatorFactory(
			BeanPropertyComparerProvider comparerProvider) {
		this.comparerProvider = comparerProvider;
	}
	
	/**
	 * Create a Comparator.
	 * 
	 * @param xMetaData The X meta data.
	 * @param yMetaData The Y meta data.
	 * 
	 * @return A Comparator. Never null.
	 */
	public Comparator<Iterable<?>> createComparatorFor(
			MatchableMetaData xMetaData, MatchableMetaData yMetaData) {
		
		final List<NullSafeComparer<?>> compares = 
				new ArrayList<NullSafeComparer<?>>();
		
		if (xMetaData == null || yMetaData == null) {
			throw new NullPointerException("Matchable Meta Data must not be null.");
		}
	
		for (String propertyName: xMetaData.getKeyProperties()) {

			Class<?> propertyType = new PropertyTypeHelper().typeFor(propertyName, 
					xMetaData.getPropertyType(propertyName),
					yMetaData.getPropertyType(propertyName));
			
			Comparer<?> comparer = comparerProvider.comparerFor(propertyName, 
					propertyType);
					
			if (comparer == null) {
				throw new IllegalStateException("No comparer for [" + 
						propertyName + "] of type [" + 
						propertyType.getName() + "]");
			}
			
			compares.add(createWithinferdType(comparer, propertyName));
		}		
		
		return new Comparator<Iterable<?>>() {

			@Override
			public int compare(Iterable<?> x, Iterable<?> y) {
				
				if (x == null || y == null) {
					throw new NullPointerException("Keys must not be null.");
				}

				ValuePairIterable<NullSafeComparer<?>, ?> values = 
					new ValuePairIterable<NullSafeComparer<?>, Object>(
						compares, x, y);
			
				for (ValuePairIterable.ValuePair<NullSafeComparer<?>, ?> set : values) {

					Object valueX = set.getValueX();
					Object valueY = set.getValueY();
					
					NullSafeComparer<?> common = set.getCommon();
					
					Comparison<?> comparison = 
						common.castAndCompare(valueX, valueY);
					
					int result = comparison.getResult();
					if (result != 0) {
						return result;
					}
				}		
				
				return 0;
			}			
		};
	}

	private static <T> NullSafeComparer<T> createWithinferdType(
			Comparer<T> comparer, String propertyName) {
		
		return new NullSafeComparer<T>(comparer, propertyName);
	}
	
}
