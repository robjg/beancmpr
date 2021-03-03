package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.results.SharedNameResultBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Help with Property Types.
 * 
 * @author rob
 *
 */
public class PropertyTypeHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(
			SharedNameResultBeanFactory.class);

	/**
	 * Find a common base type for two property types.
	 * 
	 * @param propertyName
	 * @param firstType
	 * @param secondType
	 * @return
	 */
	public Class<?> typeFor(String propertyName,
			Class<?> firstType, Class<?> secondType) {
		

		Class<?> newType;
		
		if (firstType.isPrimitive()) {
			newType = ClassUtils.wrapperClassForPrimitive(firstType);
		}
		else {
			newType = firstType;
		}
		
		if (secondType.isPrimitive()) {
			secondType = ClassUtils.wrapperClassForPrimitive(secondType);
		}
		
		while (!newType.isAssignableFrom(secondType)) {
			newType = newType.getSuperclass();
		}
		
		if (logger.isTraceEnabled() && newType != firstType) {
			logger.trace("Changing Types for property " + propertyName +
					" from " + firstType.getName() + " to " +
					newType);
		}

		return newType;
	}
}
