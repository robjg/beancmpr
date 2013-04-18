package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.beanutils.MagicBeanClassCreator;
import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

/**
 * Creates a very match result bean where the comparison is the object (
 * i.e. of type {@link Comparison}) not it's summary text.
 * <p>
 * Note the developer was really running out of naming ideas when this
 * class was created.
 * 
 * @see SimpleResultBeanFactory
 * 
 * @author rob
 *
 */
public class AlternativeResultBeanFactory extends SharedNameResultBeanFactory
implements ResultBeanFactory {

	private final PropertyAccessor accessor;
	
	public AlternativeResultBeanFactory(PropertyAccessor accessor,
			String xPropertyPrefix, String yPropertyPrefix) {
		super(xPropertyPrefix, yPropertyPrefix);
		
		this.accessor = accessor;
	}		
	
	public ArooaClass classFor(MatchableMetaData metaData) {
		
		MagicBeanClassCreator magicDef = new MagicBeanClassCreator(
				"MatchResultBean");
		
		magicDef.addProperty(MATCH_RESULT_TYPE_PROPERTY, 
				MatchResultType.class);
		
		for (String key : metaData.getKeyProperties()) {			
			
			magicDef.addProperty(key, metaData.getPropertyType(key));
		}
		
		for (String propertyName : metaData.getValueProperties()) {
			
			Class<?> valueType = metaData.getPropertyType(propertyName);
			magicDef.addProperty(xify(propertyName), valueType);
			magicDef.addProperty(yify(propertyName), valueType);
			magicDef.addProperty(propertyName + COMPARISON_PROPERTY_SUFFIX, 
					Comparison.class);
		}
		
		for (String propertyName : metaData.getOtherProperties()) {
			
			Class<?> valueType = metaData.getPropertyType(propertyName);
			magicDef.addProperty(xify(propertyName), valueType);
			magicDef.addProperty(yify(propertyName), valueType);
		}
		
		return magicDef.create();
	}
	
	@Override
	protected void populateMatchResultType(Object resultBean,
			MatchResultType matchResultType) {
		
		accessor.setProperty(resultBean, MATCH_RESULT_TYPE_PROPERTY, 
				matchResultType);
	}
	
	@Override
	protected void populateKeyProperty(Object resultBean, String property,
			Object value) {
		
		accessor.setProperty(resultBean, property, value);
	}
	
	@Override
	protected void populateXProperty(Object resultBean, String property,
			Object value) {
		
		accessor.setProperty(resultBean, 
				xify(property), value);
		
	}
	
	@Override
	protected void populateYProperty(Object resultBean, String property,
			Object value) {
		
		accessor.setProperty(resultBean, 
				yify(property), value);
		
	}
	
	@Override
	protected void populateComparison(Object resultBean, String property,
			Comparison<?> comparison) {

		accessor.setProperty(resultBean, 
				property + COMPARISON_PROPERTY_SUFFIX, 
				comparison);
	}	
}
