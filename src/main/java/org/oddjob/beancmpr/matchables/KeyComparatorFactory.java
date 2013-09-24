package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.ComparerProvider;
import org.oddjob.beancmpr.beans.PropertyTypeHelper;

/**
 * Create a {@code Comparator} for the keys of {@link Matchable}s based
 * on their {@link MatchableMetaData}.
 * 
 * @author rob
 *
 */
public class KeyComparatorFactory {

	private final ComparerProvider comparerProvider;
	
	/**
	 * Create a new instance.
	 * 
	 * @param comparerProvider Used to provide comparisons for the key
	 * fields.
	 */
	public KeyComparatorFactory(
			ComparerProvider comparerProvider) {
		this.comparerProvider = comparerProvider;
	}
	
	/**
	 * Create a comparer.
	 * 
	 * @param xMetaData The X meta data.
	 * @param yMetaData The Y meta data.
	 * 
	 * @return A Comparator. Never null.
	 */
	public Comparator<Iterable<?>> createComparerFor(
			MatchableMetaData xMetaData, MatchableMetaData yMetaData) {
		
		final List<InferredTypeCompare<?>> compares = 
				new ArrayList<InferredTypeCompare<?>>();
		
		if (xMetaData == null || yMetaData == null) {
			throw new NullPointerException("Matchable Meta Data must not be null.");
		}
	
		for (String propertyName: xMetaData.getKeyProperties()) {

			Class<?> propertyType = new PropertyTypeHelper().typeFor(propertyName, 
					xMetaData.getPropertyType(propertyName),
					yMetaData.getPropertyType(propertyName));
			
			compares.add(createWithinferdType(	
				comparerProvider.comparerFor(propertyName, 
						propertyType), propertyName));
		}		
		
		return new Comparator<Iterable<?>>() {

			@Override
			public int compare(Iterable<?> x, Iterable<?> y) {
				
				if (x == null || y == null) {
					throw new NullPointerException("Keys must not be null.");
				}

				ValuePairIterable<InferredTypeCompare<?>, ?> values = 
					new ValuePairIterable<InferredTypeCompare<?>, Object>(
						compares, x, y);
			
				for (ValuePairIterable.ValuePair<InferredTypeCompare<?>, ?> set : values) {

					Object valueX = set.getValueX();
					Object valueY = set.getValueY();
					
					InferredTypeCompare<?> common = set.getCommon();
					
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

	private static <T> InferredTypeCompare<T> createWithinferdType(
			Comparer<T> comparer, String propertyName) {
		
		return new InferredTypeCompare<T>(comparer, propertyName);
	}
	
}
