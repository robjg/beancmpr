package org.oddjob.beancmpr.results;

import java.util.Iterator;

import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableMetaData;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.matchables.ValueIterable;
import org.oddjob.beancmpr.matchables.ValuePairIterable;

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
			populateMatchResultType(result, MatchResultType.Type.EQUAL);
		} else {
			populateMatchResultType(result, MatchResultType.Type.NOT_EQUAL);
		}
		
		populateKeys(result, definition.getKeyProperties(), x.getKeys());
		
		ValuePairIterable<String, Object> values = 
				new ValuePairIterable<String, Object>(
					definition.getValueProperties(), 
					x.getValues(), 
					y.getValues());
		
		Iterator<Comparison<?>> comparisonsIterator = 
			matchableComparison == null ? null :
				matchableComparison.getValueComparisons().iterator();
		
		for (ValuePairIterable.ValuePair<String, Object> set : values) {
			
			String propertyName = set.getCommon();
			
			Comparison<?> comparison = comparisonsIterator == null ?
					null : comparisonsIterator.next();
			
			populateXProperty(result, propertyName, set.getValueX());
			populateYProperty(result, propertyName, set.getValueY());
			populateComparison(result, propertyName, comparison);
		}
		
		ValuePairIterable<String, Object> others = 
				new ValuePairIterable<String, Object>(
					definition.getOtherProperties(), 
					x == null ? null : x.getOthers(), 
					y == null ? null : y.getOthers());
		
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

		populateMatchResultType(result, MatchResultType.Type.X_MISSING);
			
		populateKeys(result, definition.getKeyProperties(), y.getKeys());
		
		ValueIterable<Object> values = 
				new ValueIterable<Object>(
					definition.getValueProperties(), 
					y.getValues());
		
		for (ValueIterable.Value<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			populateYProperty(result, propertyName, set.getValue());
		}
		
		ValueIterable<Object> others = 
				new ValueIterable<Object>(
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

		populateMatchResultType(result, MatchResultType.Type.Y_MISSING);
		
		populateKeys(result, definition.getKeyProperties(), x.getKeys());
		
		ValueIterable<Object> values = 
				new ValueIterable<Object>(
					definition.getValueProperties(), 
					x.getValues());
		
		for (ValueIterable.Value<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			populateXProperty(result, propertyName, set.getValue());
		}
		
		ValueIterable<Object> others = 
				new ValueIterable<Object>(
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
				new ValueIterable<Object>(
					keyProperties, 
					keyValues);
		
		for (ValueIterable.Value<Object> set : keys) {
			Object value = set.getValue();
			
			populateKeyProperty(resultBean, set.getPropertyName(), value);
		}
	}	

	protected abstract void populateMatchResultType(
			Object resultBean, MatchResultType.Type matchResultType);
	
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
	 * @param propertyName
	 * @return
	 */
	protected static String upperCaseFirstLetter(String propertyName) {
		return propertyName.substring(0, 1).toUpperCase() + 
			propertyName.substring(1);
	}
}
