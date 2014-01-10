package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.BeanOverview;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.MatchDefinition;

/**
 * A {@link MatchableFactory} that creates {@link Matchable}s from beans.
 * 
 * @author rob
 *
 */
public class BeanMatchableFactory implements MatchableFactory<Object> {

	private final MatchDefinition definition;
	
	private final PropertyAccessor accessor;
	
	private MatchableMetaData metaData;
	
	/**
	 * Create a new instance.
	 * 
	 * @param definition
	 * @param accessor
	 */
	public BeanMatchableFactory(MatchDefinition definition,
			PropertyAccessor accessor) {
		this.definition = definition;
		this.accessor = accessor;
	}
	
	@Override
	public Matchable createMatchable(Object bean) {

		if (bean == null) {
			return null;
		}
		
		if (metaData == null) {
			metaData = new SimpleMatchableMeta(definition, 
					typesFor(bean));
		}
		
		List<?> keys = strip(bean, definition.getKeyProperties());		
		List<?> comparables = strip(bean, definition.getValueProperties());
		List<?> others = strip(bean, definition.getOtherProperties());
		
		SimpleMatchable matchable = 
			new SimpleMatchable(keys, comparables, others, metaData);
		
		return matchable;
	}
	
	/**
	 * Create a list of the given property values.
	 * 
	 * @param bean
	 * @param names
	 * 
	 * @return The property values. Never null.
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> strip(Object bean, Iterable<String> names) {
		
		List<T> values = new ArrayList<T>();
		
		if (names != null) {
			for (String name : names) {
				values.add((T) accessor.getProperty(bean, name));
			}
		}
		
		return values;
	}
	
	/**
	 * Create a property to type map.
	 * 
	 * @param bean
	 * @return
	 */
	private Map<String, Class<?>> typesFor(Object bean) {
		
		Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		
		ArooaClass arooaClass = accessor.getClassName(bean);
		
		BeanOverview overview = arooaClass.getBeanOverview(accessor);

		addTypes(definition.getKeyProperties(), overview, types);
		addTypes(definition.getValueProperties(), overview, types);
		addTypes(definition.getOtherProperties(), overview, types);
		
		return types;
	}	
	
	/**
	 * Used by the above.
	 * 
	 * @param propertyNames
	 * @param overview
	 * @param types
	 */
	private void addTypes(Iterable<String> propertyNames, 
			BeanOverview overview, Map<String, Class<?>> types) {
		if (propertyNames != null) {
			for (String name : propertyNames) {
				types.put(name, overview.getPropertyType(name));
			}
		}		
	}
		
}

