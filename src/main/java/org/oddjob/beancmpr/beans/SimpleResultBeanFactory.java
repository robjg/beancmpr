package org.oddjob.beancmpr.beans;

import java.util.Iterator;

import org.oddjob.arooa.beanutils.MagicBeanClassCreator;
import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparison;
import org.oddjob.beancmpr.matchables.MatchableIterable;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

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
	
	public Object createResult(Matchable x, Matchable y, 
			MatchableComparison matchableComparison) {
			
		MatchableMetaData definition;
		if (x == null) {
			definition = y.getMetaData();
		}
		else {
			definition = x.getMetaData();
		}
		
		if (resultClass == null) {
			resultClass = classFor(definition);
		}
		
		Object result = resultClass.newInstance();

		if (x == null) {
			accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
					MatchResultType.X_MISSING);
		} else if (y == null) {
			accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
					MatchResultType.Y_MISSING);
		} else if (matchableComparison.isEqual()) {
			accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
					MatchResultType.EQUAL);
		} else {
			accessor.setProperty(result, MATCH_RESULT_TYPE_PROPERTY, 
					MatchResultType.NOT_EQUAL);
		}
		
		
		MatchableIterable<Object> keys = 
				new MatchableIterable<Object>(
					definition.getKeyProperties(), 
					x == null ? null : x.getKeys(), 
					y == null ? null : y.getKeys());
		
		for (MatchableIterable.MatchableSet<Object> set : keys) {
			Object value;
			if (x != null) {
				value = set.getValueX();
			}
			else {
				value = set.getValueY();
			}
			accessor.setProperty(result, set.getPropertyName(), value);
		}
		
		MatchableIterable<Object> values = 
				new MatchableIterable<Object>(
					definition.getValueProperties(), 
					x == null ? null : x.getValues(), 
					y == null ? null : y.getValues());
		
		Iterator<Comparison> comparisonsIterator = 
			matchableComparison == null ? null :
				matchableComparison.getValueComparisons().iterator();
		
		for (MatchableIterable.MatchableSet<Object> set : values) {
			
			String propertyName = set.getPropertyName();
			
			Comparison comparison = comparisonsIterator == null ?
					null : comparisonsIterator.next();
			
			accessor.setProperty(result, 
					xify(propertyName), set.getValueX());
			accessor.setProperty(result, 
					yify(propertyName), set.getValueY());
			accessor.setProperty(result, 
					propertyName + COMPARISON_PROPERTY_SUFFIX, 
					comparison == null ? null : comparison.getSummaryText());
		}
		
		MatchableIterable<Object> others = 
				new MatchableIterable<Object>(
					definition.getOtherProperties(), 
					x == null ? null : x.getOthers(), 
					y == null ? null : y.getOthers());
		
		for (MatchableIterable.MatchableSet<Object> set : others) {
						
			String propertyName = set.getPropertyName();
			
			accessor.setProperty(result, 
					xify(propertyName), set.getValueX());
			accessor.setProperty(result, 
					yify(propertyName), set.getValueY());
		}
		
		return result;
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
