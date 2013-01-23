package org.oddjob.beancmpr.beans;

import java.util.Iterator;

import org.oddjob.arooa.beanutils.MagicBeanClassCreator;
import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableMetaData;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.matchables.ValueIterable;
import org.oddjob.beancmpr.matchables.ValuePairIterable;

/**
 * Creates a very simple match result bean.
 * 
 * @author rob
 *
 */
public class SimpleResultBeanFactory implements MatchResultBeanFactory {

	public static final String MATCH_RESULT_TYPE_PROPERTY = "matchResultType";
	
	public static final String DEFAULT_X_PROPERTY_PREFIX = "x";
	
	public static final String DEFAULT_Y_PROPERTY_PREFIX = "y";
	
	public static final String COMPARISON_PROPERTY_SUFFIX = "Comparison";
	
	private ArooaClass resultClass;
	
	private MatchableMetaData definition;
	
	private final PropertyAccessor accessor;
	
	private final String xPropertyPrefix;
	
	private final String yPropertyPrefix;
	
	public SimpleResultBeanFactory(PropertyAccessor accessor,
			String xPropertyPrefix, String yPropertyPrefix) {
		this.accessor = accessor;
		
		if (xPropertyPrefix == null) {
			this.xPropertyPrefix = DEFAULT_X_PROPERTY_PREFIX;
		}
		else {
			this.xPropertyPrefix = xPropertyPrefix;
		}
		
		if (yPropertyPrefix == null) {
			this.yPropertyPrefix = DEFAULT_Y_PROPERTY_PREFIX;
		}
		else {
			this.yPropertyPrefix = yPropertyPrefix;
		}
	}	
	
	private void checkInit(Matchable either) {
		
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
			accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
					MatchResultType.EQUAL);
		} else {
			accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
					MatchResultType.NOT_EQUAL);
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
			
			accessor.setProperty(result, 
					xify(propertyName), set.getValueX());
			accessor.setProperty(result, 
					yify(propertyName), set.getValueY());
			accessor.setProperty(result, 
					propertyName + COMPARISON_PROPERTY_SUFFIX, 
					comparison == null ? null : comparison.getSummaryText());
		}
		
		ValuePairIterable<Object> others = 
				new ValuePairIterable<Object>(
					definition.getOtherProperties(), 
					x == null ? null : x.getOthers(), 
					y == null ? null : y.getOthers());
		
		for (ValuePairIterable.ValuePair<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			accessor.setProperty(result, 
					xify(propertyName), set.getValueX());
			accessor.setProperty(result, 
					yify(propertyName), set.getValueY());
		}
		
		return result;
	}
		
	@Override
	public Object createXMissingResult(Matchable y) {

		checkInit(y);
		
		Object result = resultClass.newInstance();

		accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
				MatchResultType.X_MISSING);
			
		populateKeys(result, y.getKeys());
		
		ValueIterable<Object> values = 
				new ValueIterable<Object>(
					definition.getValueProperties(), 
					y.getValues());
		
		for (ValueIterable.Value<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			accessor.setProperty(result, 
					yify(propertyName), set.getValue());
		}
		
		ValueIterable<Object> others = 
				new ValueIterable<Object>(
					definition.getOtherProperties(), 
					y.getOthers());
		
		for (ValueIterable.Value<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			accessor.setProperty(result, 
					yify(propertyName), set.getValue());
		}
		
		return result;
	}
	
	@Override
	public Object createYMissingResult(Matchable x) {
		
		checkInit(x);
		
		Object result = resultClass.newInstance();

		accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
				MatchResultType.Y_MISSING);
		
		populateKeys(result, x.getKeys());
		
		ValueIterable<Object> values = 
				new ValueIterable<Object>(
					definition.getValueProperties(), 
					x.getValues());
		
		for (ValueIterable.Value<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			accessor.setProperty(result, 
					xify(propertyName), set.getValue());
		}
		
		ValueIterable<Object> others = 
				new ValueIterable<Object>(
					definition.getOtherProperties(), 
					x.getOthers());
		
		for (ValueIterable.Value<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			accessor.setProperty(result, 
					xify(propertyName), set.getValue());
		}
		
		return result;
	}
	
	void populateKeys(Object bean, Iterable<?> keyValues) {
		
		ValueIterable<Object> keys = 
				new ValueIterable<Object>(
					definition.getKeyProperties(), 
					keyValues);
		
		for (ValueIterable.Value<Object> set : keys) {
			Object value = set.getValue();
			accessor.setProperty(bean, set.getPropertyName(), value);
		}
		
	}
	
	
	public ArooaClass classFor(MatchableMetaData metaData) {
		
		MagicBeanClassCreator magicDef = new MagicBeanClassCreator(
				"MatchResultBean");
		
		magicDef.addProperty(MATCH_RESULT_TYPE_PROPERTY, MatchResultType.class);
		
		for (String key : metaData.getKeyProperties()) {			
			
			magicDef.addProperty(key, metaData.getPropertyType(key));
		}
		
		for (String propertyName : metaData.getValueProperties()) {
			
			Class<?> valueType = metaData.getPropertyType(propertyName);
			magicDef.addProperty(xify(propertyName), valueType);
			magicDef.addProperty(yify(propertyName), valueType);
			magicDef.addProperty(propertyName + COMPARISON_PROPERTY_SUFFIX, 
					String.class);
		}
		
		for (String propertyName : metaData.getOtherProperties()) {
			
			Class<?> valueType = metaData.getPropertyType(propertyName);
			magicDef.addProperty(xify(propertyName), valueType);
			magicDef.addProperty(yify(propertyName), valueType);
		}
		
		return magicDef.create();
	}
	
	protected String xify(String propertyName) {
		return xPropertyPrefix + upperCaseFirstLetter(propertyName);
	}
	
	protected String yify(String propertyName) {
		return yPropertyPrefix + upperCaseFirstLetter(propertyName);
	}
	
	protected String upperCaseFirstLetter(String propertyName) {
		return propertyName.substring(0, 1).toUpperCase() + 
			propertyName.substring(1);
	}
}
