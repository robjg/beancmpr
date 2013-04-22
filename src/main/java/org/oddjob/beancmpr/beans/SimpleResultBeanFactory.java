package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparison;

/**
 * Creates a very simple match result bean.
 * 
 * @author rob
 *
 */
public class SimpleResultBeanFactory extends SharedNameResultBeanFactory
implements ResultBeanFactory {

	private final ResultBeanClassProvider classProvider = 
			new AbstractResultBeanClassProvider() {
		
		@Override
		protected Class<?> classForComparison() {
			return String.class;
		}
		
		@Override
		protected Class<?> classForResultType() {
			return String.class;
		}
		
		@Override
		protected PropertyAccessor getPropertyAccessor() {
			return accessor;
		}
	};
	
	private final PropertyAccessor accessor;
	
	public SimpleResultBeanFactory(PropertyAccessor accessor,
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
				matchResultType.getText());
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
				comparison == null ? null : comparison.getSummaryText());
	}
}
