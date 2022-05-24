package org.oddjob.beancmpr.matchables;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.MatchDefinition;

/**
 * A simple implementation of {@link MatchableMetaData}.
 * 
 * @author rob
 *
 */
public class SimpleMatchableMeta implements MatchableMetaData {

	private final Iterable<String> keyProperties;
	
	private final Iterable<String> valueProperties;
	
	private final Iterable<String> otherProperties;
	
	private final Map<String, Class<?>> types;

	public SimpleMatchableMeta(Iterable<String> keyProperties,
		Iterable<String> valueProperties, Iterable<String> otherProperties,
		Map<String, Class<?>> types) {
		
		if (keyProperties == null) {
			this.keyProperties = Collections.emptyList();
		}
		else {
			this.keyProperties = keyProperties;
		}
		
		if (valueProperties == null) {
			this.valueProperties = Collections.emptyList();
		}
		else {
			this.valueProperties = valueProperties;
		}
		
		if (otherProperties == null) {
			this.otherProperties = Collections.emptyList();
		}
		else {
			this.otherProperties = otherProperties;
		}
		
		this.types = new HashMap<String, Class<?>>();
		for (Map.Entry<String, Class<?>> entry: types.entrySet()) {
			Class<?> type = entry.getValue();
			if (type.isPrimitive()) {
				type = ClassUtils.wrapperClassForPrimitive(type);
			}
			this.types.put(entry.getKey(), type);
		}
	}
	
	public SimpleMatchableMeta(MatchDefinition definition,
			Map<String, Class<?>> types) {

		this(definition.getKeyProperties(),
				definition.getValueProperties(),
				definition.getOtherProperties(),
				types); 
	}
	
	@Override
	public Iterable<String> getKeyProperties() {
		return keyProperties;
	}
	
	@Override
	public Iterable<String> getValueProperties() {
		return valueProperties;
	}
	
	@Override
	public Iterable<String> getOtherProperties() {
		return otherProperties;
	}
	
	@Override
	public Class<?> getPropertyType(String name) {
		return types.get(name);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": for " + types.size() + 
				" types";
	}
}
