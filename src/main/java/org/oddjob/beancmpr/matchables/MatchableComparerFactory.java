package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.List;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.beans.PropertyTypeHelper;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;

/**
 * Create a {@link MatchableComparer} base on the {@link Matchable}s 
 * meta data.
 * 
 * @author rob
 *
 */
public class MatchableComparerFactory {

	private final BeanPropertyComparerProvider comparerProvider;
	
	/**
	 *  Create an instance.
	 *  
	 * @param comparerProvider Used to provide {@link Comparer}s for 
	 * the fields. Must not be null.
	 */
	public MatchableComparerFactory(
			BeanPropertyComparerProvider comparerProvider) {
		
		if (comparerProvider == null) {
			throw new NullPointerException("ComparerProvider must be provded.");
		}
		
		this.comparerProvider = comparerProvider;
	}
	
	/**
	 * Create the {@link MatchableComparer}.
	 * 
	 * @param xMetaData The X meta data.
	 * @param yMetaData The Y meta data.
	 * 
	 * @return A comparer. Never null.
	 */
	public MatchableComparer createComparerFor(
			MatchableMetaData xMetaData, MatchableMetaData yMetaData) {
		
		if (xMetaData == null || yMetaData == null) {
			throw new NullPointerException("MetaData must not be null.");
		}
	
		final List<NullSafeComparer<?>> compares = 
				new ArrayList<NullSafeComparer<?>>();
		
		for (String propertyName: xMetaData.getValueProperties()) {

			Class<?> propertyType = new PropertyTypeHelper().typeFor(
					propertyName, 
					xMetaData.getPropertyType(propertyName),
					yMetaData.getPropertyType(propertyName));
			
			Comparer<?> comparer = comparerProvider.comparerFor(propertyName, 
					propertyType);
			
			if (comparer == null) {
				throw new NullPointerException("No comparer for name " +
						propertyName + ", type " + propertyType);
			}
			
			compares.add(createWithInferdType(comparer, propertyName));
		}		
		
		return new MatchableComparer() {
			
			@Override
			public MultiValueComparison<Matchable> compare(Matchable x,
					Matchable y) {
				
				if (x == null || y == null) {
					throw new NullPointerException("Matchables must not be null.");
				}

				List<Comparison<?>> comparisons = new ArrayList<Comparison<?>>();		
				
				ValuePairIterable<NullSafeComparer<?>, ?> values = 
					new ValuePairIterable<NullSafeComparer<?>, Object>(
						compares, x.getValues(), y.getValues());
			
				for (ValuePairIterable.ValuePair<NullSafeComparer<?>, ?> set : values) {

					Object valueX = set.getValueX();
					Object valueY = set.getValueY();
					
					NullSafeComparer<?> common = set.getCommon();
					
					Comparison<?> comparison = 
						common.castAndCompare(valueX, valueY);
					
					comparisons.add(comparison);			
				}		
				
				return new MatchableComparision(x, y, comparisons);
			}
			
			@Override
			public Class<?> getType() {
				return Matchable.class;
			}
			
		};
	}

	private static <T> NullSafeComparer<T> createWithInferdType(
			Comparer<T> comparer, String propertyName) {
		
		return new NullSafeComparer<T>(comparer, propertyName);
	}
	
}
