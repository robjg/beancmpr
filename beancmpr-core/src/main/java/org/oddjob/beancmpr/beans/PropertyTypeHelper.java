package org.oddjob.beancmpr.beans;

import org.oddjob.beancmpr.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * Help with Property Types.
 * 
 * @author rob
 *
 */
public class PropertyTypeHelper {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyTypeHelper.class);

	/**
	 * Find a common base type for two property types.
	 * 
	 * @param propertyName The property. For logging only.
	 * @param firstType First type.
	 * @param secondType Second type.
	 *
	 * @return The base type.
	 */
	public Type typeFor(String propertyName,
						Type firstType, Type secondType) {

		firstType = TypeUtil.wrapperOf(firstType);
		secondType = TypeUtil.wrapperOf(secondType);

		Type newType = firstType;
		

		while (!TypeUtil.isAssignableFrom(newType, secondType)) {
			newType = TypeUtil.getParentType(newType);
		}
		
		if (logger.isTraceEnabled() && newType != firstType) {
            logger.trace("Changing Types for property {} from {} to {}",
					propertyName, firstType.getTypeName(), newType.getTypeName());
		}

		return newType;
	}

}
