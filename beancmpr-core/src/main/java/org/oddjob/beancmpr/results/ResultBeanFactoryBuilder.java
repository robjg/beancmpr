package org.oddjob.beancmpr.results;

import org.oddjob.arooa.reflect.PropertyAccessor;

/**
 * Allow for different {@link ResultBeanFactory}.
 * 
 * @see BeanCreatingResultHandler
 * 
 * @author rob
 *
 */
public interface ResultBeanFactoryBuilder {

	/**
	 * Build the factory.
	 * 
	 * @param accessor
	 * @param xPropertyPrefix
	 * @param yPropertyPrefix

	 * @return Not Null.
	 */
	public ResultBeanFactory factoryFor(PropertyAccessor accessor,
			String xPropertyPrefix, String yPropertyPrefix);
	
}
