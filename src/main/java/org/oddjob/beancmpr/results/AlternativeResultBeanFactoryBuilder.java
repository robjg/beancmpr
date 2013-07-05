package org.oddjob.beancmpr.results;

import org.oddjob.arooa.reflect.PropertyAccessor;

/**
 * The {@link ResultBeanFactoryBuilder} for {@link AlternativeResultBeanFactory}.
 * 
 * @author rob
 *
 */
public class AlternativeResultBeanFactoryBuilder 
implements ResultBeanFactoryBuilder {

	@Override
	public ResultBeanFactory factoryFor(PropertyAccessor accessor,
			String xPropertyPrefix, String yPropertyPrefix) {

		return new AlternativeResultBeanFactory(accessor, 
				xPropertyPrefix, yPropertyPrefix);
	}
	
}
