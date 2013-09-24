package org.oddjob.beancmpr.beans;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.results.SharedNameResultBeanFactory;

/**
 * Help with Property Types.
 * 
 * @author rob
 *
 */
public class PropertyTypeHelper {

	private static final Map<Class<?>, Class<?>> primitiveTypeWrappers = 
			new HashMap<Class<?>, Class<?>>(8);
	
	static {
		Class<?>[] primatives = {
				boolean.class, byte.class, char.class, double.class,
				float.class, int.class, long.class, short.class };

		Class<?>[] wrappers = {
				Boolean.class, Byte.class, Character.class, Double.class,
				Float.class, Integer.class, Long.class, Short.class };
		
		for (int i = 0; i < 8; ++i) {
			primitiveTypeWrappers.put(primatives[i], wrappers[i]);
		}		
	}

	
	private static final Logger logger = Logger.getLogger(
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
