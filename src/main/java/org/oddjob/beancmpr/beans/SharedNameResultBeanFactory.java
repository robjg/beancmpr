package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.beans.MatchResultType.Type;


/**
 * Provides super classes with implementation for naming of properties of 
 * a {@link ResultBeanFactory}.
 * 
 * @author rob
 *
 */
abstract public class SharedNameResultBeanFactory extends AbstractResultBeanFactory
implements ResultBeanFactory {

	public static final String MATCH_RESULT_TYPE_PROPERTY = "matchResultType";
	
	public static final String DEFAULT_X_PROPERTY_PREFIX = "x";
	
	public static final String DEFAULT_Y_PROPERTY_PREFIX = "y";
	
	public static final String COMPARISON_PROPERTY_SUFFIX = "Comparison";
	
	private final String xPropertyPrefix;
	
	private final String yPropertyPrefix;
	
	public SharedNameResultBeanFactory(
			String xPropertyPrefix, String yPropertyPrefix) {
		
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
	
	protected String xify(String propertyName) {
		return xPropertyPrefix + upperCaseFirstLetter(propertyName);
	}
	
	protected String yify(String propertyName) {
		return yPropertyPrefix + upperCaseFirstLetter(propertyName);
	}
	
	@Override
	protected final void populateMatchResultType(Object resultBean,
			Type matchResultType) {
		
		switch (matchResultType) {
		case X_MISSING: 
			populateMatchResultType(resultBean, new MatchResultType.XMissing() {
				@Override
				public String getText() {
					return xPropertyPrefix + "_MISSING";
				}
			});
			break;
		case Y_MISSING: 
			populateMatchResultType(resultBean, new MatchResultType.YMissing() {
				@Override
				public String getText() {
					return yPropertyPrefix + "_MISSING";
				}
			});
			break;
		case NOT_EQUAL: 
			populateMatchResultType(resultBean, new MatchResultType.NotEqual() {
				@Override
				public String getText() {
					return "NOT_EQUAL";
				}
			});
			break;
		case EQUAL: 
			populateMatchResultType(resultBean, new MatchResultType.Equal() {
				@Override
				public String getText() {
					return "EQUAL";
				}
			});
			break;
		}
	}
	
	abstract protected void populateMatchResultType(Object resultBean,
			MatchResultType matchResultType);
}
