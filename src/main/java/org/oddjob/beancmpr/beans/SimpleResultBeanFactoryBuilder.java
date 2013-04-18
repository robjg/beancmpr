package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.reflect.PropertyAccessor;

/**
 * The {@link ResultBeanFactoryBuilder} for {@link SimpleResultBeanFactory}.
 * 
 * @author rob
 *
 */
public class SimpleResultBeanFactoryBuilder 
implements ResultBeanFactoryBuilder {

	@Override
	public ResultBeanFactory factoryFor(PropertyAccessor accessor,
			String xPropertyPrefix, String yPropertyPrefix) {

		return new SimpleResultBeanFactory(accessor, 
				xPropertyPrefix, yPropertyPrefix);
	}
	
}
