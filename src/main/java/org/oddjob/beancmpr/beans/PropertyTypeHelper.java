package org.oddjob.beancmpr.beans;

import org.apache.log4j.Logger;

public class PropertyTypeHelper {

	private static final Logger logger = Logger.getLogger(
			SharedNameResultBeanFactory.class);
	
	public Class<?> typeFor(String propertyName,
			Class<?> firstType, Class<?> secondType) {
		

		Class<?> newType = firstType;
		
		while (!newType.isAssignableFrom(secondType)) {
			newType = newType.getSuperclass();
		}
		
		if (logger.isDebugEnabled() && newType != firstType) {
			logger.debug("Changing Types for property " + propertyName +
					" from " + firstType.getName() + " to " +
					newType);
		}

		return newType;
	}
}
