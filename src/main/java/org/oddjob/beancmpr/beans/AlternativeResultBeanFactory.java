package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparison;

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

	private final ResultBeanClassProvider classProvider = 
			new AbstractResultBeanClassProvider() {
		
		@Override
		protected Class<?> classForResultType() {
			return MatchResultType.class;
		}
		
		@Override
		protected Class<?> classForComparison() {
			return Comparison.class;
		}
		
		@Override
		protected PropertyAccessor getPropertyAccessor() {
			return accessor;
		}
	};
	
	private final PropertyAccessor accessor;
	
	public AlternativeResultBeanFactory(PropertyAccessor accessor,
			String xPropertyPrefix, String yPropertyPrefix) {
		super(xPropertyPrefix, yPropertyPrefix);
		
		this.accessor = accessor;
	}		
	
	@Override
	protected ResultBeanClassProvider getClassProvider() {
		return classProvider;
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
