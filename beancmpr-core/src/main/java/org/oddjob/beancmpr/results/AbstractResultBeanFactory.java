package org.oddjob.beancmpr.results;

import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.*;

import java.util.Iterator;

/**
 * Base implementation for creating result beans.
 * 
 * @author rob
 *
 */
public abstract class AbstractResultBeanFactory 
implements ResultBeanFactory {
	
	
	@Override
	public Object createComparisonResult(
			MultiValueComparison<Matchable> matchableComparison) {
			
		Matchable x = matchableComparison.getX();
		Matchable y = matchableComparison.getY();
		
		ArooaClass resultClass = getClassProvider().classForComparison(
				 matchableComparison);
		MatchableMetaData definition = x.getMetaData();
		
		Object result = resultClass.newInstance();		
		
		if (matchableComparison.getResult() == 0) {
			populateMatchResultType(result, MatchResultType.EQUAL);
		} else {
			populateMatchResultType(result, MatchResultType.NOT_EQUAL);
		}
		
		populateKeys(result, definition.getKeyProperties(), x.getKeys());
		
		ValuePairIterable<String, Object> values =
				new ValuePairIterable<>(
						definition.getValueProperties(),
						x.getValues(),
						y.getValues());
		
		Iterator<Comparison<?>> comparisonsIterator =
				matchableComparison.getValueComparisons().iterator();
		
		for (ValuePairIterable.ValuePair<String, Object> set : values) {
			
			String propertyName = set.getCommon();
			
			Comparison<?> comparison = comparisonsIterator.next();
			
			populateXProperty(result, propertyName, set.getValueX());
			populateYProperty(result, propertyName, set.getValueY());
			populateComparison(result, propertyName, comparison);
		}
		
		ValuePairIterable<String, Object> others =
				new ValuePairIterable<>(
						definition.getOtherProperties(),
						x.getOthers(),
						y.getOthers());
		
		for (ValuePairIterable.ValuePair<String, Object> set : others) {
						
			String propertyName = set.getCommon();
			
			populateXProperty(result, propertyName, set.getValueX());
			populateYProperty(result, propertyName, set.getValueY());
		}
		
		return result;
	}
		
	@Override
	public Object createXMissingResult(Matchable y) {

		ArooaClass resultClass = getClassProvider().classForY(y);
		MatchableMetaData definition = y.getMetaData();
		
		Object result = resultClass.newInstance();

		populateMatchResultType(result, MatchResultType.X_MISSING);
			
		populateKeys(result, definition.getKeyProperties(), y.getKeys());
		
		ValueIterable<Object> values =
				new ValueIterable<>(
						definition.getValueProperties(),
						y.getValues());
		
		for (ValueIterable.Value<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			populateYProperty(result, propertyName, set.getValue());
		}
		
		ValueIterable<Object> others =
				new ValueIterable<>(
						definition.getOtherProperties(),
						y.getOthers());
		
		for (ValueIterable.Value<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			populateYProperty(result, propertyName, set.getValue());
		}
		
		return result;
	}
	
	@Override
	public Object createYMissingResult(Matchable x) {
		
		ArooaClass resultClass = getClassProvider().classForX(x);
		MatchableMetaData definition = x.getMetaData();
		
		Object result = resultClass.newInstance();

		populateMatchResultType(result, MatchResultType.Y_MISSING);
		
		populateKeys(result, definition.getKeyProperties(), x.getKeys());
		
		ValueIterable<Object> values =
				new ValueIterable<>(
						definition.getValueProperties(),
						x.getValues());
		
		for (ValueIterable.Value<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			populateXProperty(result, propertyName, set.getValue());
		}
		
		ValueIterable<Object> others =
				new ValueIterable<>(
						definition.getOtherProperties(),
						x.getOthers());
		
		for (ValueIterable.Value<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			populateXProperty(result, propertyName, set.getValue());
		}
		
		return result;
	}
	
	protected interface ResultBeanClassProvider {
		
		ArooaClass classForComparison(
				MultiValueComparison<Matchable> matchableComparison);
				
		ArooaClass classForX(Matchable x);

		ArooaClass classForY(Matchable y);
	}
	
	abstract protected ResultBeanClassProvider getClassProvider();
	
	private void populateKeys(Object resultBean, 
			Iterable<String> keyProperties, Iterable<?> keyValues) {
		
		ValueIterable<Object> keys =
				new ValueIterable<>(
						keyProperties,
						keyValues);
		
		for (ValueIterable.Value<Object> set : keys) {
			Object value = set.getValue();
			
			populateKeyProperty(resultBean, set.getPropertyName(), value);
		}
	}	

	protected abstract void populateMatchResultType(
			Object resultBean, MatchResultType matchResultType);
	
	protected abstract void populateKeyProperty(Object resultBean, 
			String property, Object value);
	
	protected abstract void populateXProperty(Object resultBean,
			String property, Object value);
	
	protected abstract void populateYProperty(Object resultBean,
			String property, Object value);
	
	protected abstract void populateComparison(Object resultBean,
			String property, Comparison<?> comparison);
	
	/**
	 * Utility method to upper case the first letter of a property name.
	 * 
	 * @param propertyName A name.
	 * @return The new name.
	 */
	protected static String upperCaseFirstLetter(String propertyName) {
		return propertyName.substring(0, 1).toUpperCase() + 
			propertyName.substring(1);
	}
}
