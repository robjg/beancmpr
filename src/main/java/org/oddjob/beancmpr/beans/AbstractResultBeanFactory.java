package org.oddjob.beancmpr.beans;

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
	
	private ArooaClass resultClass;
	
	private MatchableMetaData definition;
		
	protected void checkInit(Matchable either) {
		
		if (resultClass != null) {
			return;
		}
		
		this.definition = either.getMetaData();
			
		this.resultClass = classFor(definition);
	}
	
	
	@Override
	public Object createComparisonResult(
			MultiValueComparison<Matchable> matchableComparison) {
			
		Matchable x = matchableComparison.getX();
		Matchable y = matchableComparison.getY();
		
		checkInit(x);
				
		Object result = resultClass.newInstance();
		
		if (matchableComparison.getResult() == 0) {
			populateMatchResultType(result, MatchResultType.Type.EQUAL);
		} else {
			populateMatchResultType(result, MatchResultType.Type.NOT_EQUAL);
		}
		
		populateKeys(result, x.getKeys());
		
		ValuePairIterable<Object> values = 
				new ValuePairIterable<Object>(
					definition.getValueProperties(), 
					x.getValues(), 
					y.getValues());
		
		Iterator<Comparison<?>> comparisonsIterator = 
			matchableComparison == null ? null :
				matchableComparison.getValueComparisons().iterator();
		
		for (ValuePairIterable.ValuePair<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			Comparison<?> comparison = comparisonsIterator == null ?
					null : comparisonsIterator.next();
			
			populateXProperty(result, propertyName, set.getValueX());
			populateYProperty(result, propertyName, set.getValueY());
			populateComparison(result, propertyName, comparison);
		}
		
		ValuePairIterable<Object> others = 
				new ValuePairIterable<Object>(
					definition.getOtherProperties(), 
					x == null ? null : x.getOthers(), 
					y == null ? null : y.getOthers());
		
		for (ValuePairIterable.ValuePair<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			populateXProperty(result, propertyName, set.getValueX());
			populateYProperty(result, propertyName, set.getValueY());
		}
		
		return result;
	}
		
	@Override
	public Object createXMissingResult(Matchable y) {

		checkInit(y);
		
		Object result = resultClass.newInstance();

		populateMatchResultType(result, MatchResultType.Type.X_MISSING);
			
		populateKeys(result, y.getKeys());
		
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
		
		checkInit(x);
		
		Object result = resultClass.newInstance();

		populateMatchResultType(result, MatchResultType.Type.Y_MISSING);
		
		populateKeys(result, x.getKeys());
		
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
	
	protected void populateKeys(Object resultBean, Iterable<?> keyValues) {
		
		ValueIterable<Object> keys = 
				new ValueIterable<Object>(
					definition.getKeyProperties(), 
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
	
	protected abstract ArooaClass classFor(MatchableMetaData metaData);
	
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
