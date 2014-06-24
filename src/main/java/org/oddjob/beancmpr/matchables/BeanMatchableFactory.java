package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.Arrays;
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
public class BeanMatchableFactory<T> implements MatchableFactory<T> {

	/** The name of the matchable value when the value is the entire value 
	 * of the entry. */
	public static final String VALUE_NAME = "value";
	
	private final MatchDefinition definition;
	
	private final PropertyAccessor accessor;
	
	private final MatchableFactory<T> delegate;
	
	private volatile MatchableMetaData metaData;
	
	/**
	 * Create a new instance.
	 * 
	 * @param definition
	 * @param accessor
	 */
	public BeanMatchableFactory(final MatchDefinition definition,
			PropertyAccessor accessor) {
		this.definition = definition;
		this.accessor = accessor;
		
		if (definition.getKeyProperties() == null &&
				definition.getValueProperties() == null) {
			
			delegate = new MatchableFactory<T>() {
				@Override
				public Matchable createMatchable(T bean) {
					List<?> keys = null;
					List<?> comparables = Arrays.asList(bean);
					List<?> others = strip(bean, definition.getOtherProperties());
					
					return new SimpleMatchable(keys, comparables, others, 
							metaData);
				}
			};
		}
		else {
			
			delegate = new MatchableFactory<T>() {
				@Override
				public Matchable createMatchable(T bean) {
			
					List<?> keys= strip(bean, definition.getKeyProperties());		
					List<?> comparables = strip(bean, definition.getValueProperties());
					List<?> others = strip(bean, definition.getOtherProperties());
					
					return new SimpleMatchable(keys, comparables, others, 
							metaData);
				}
			};
		}
	}
	
	@Override
	public Matchable createMatchable(T bean) {

		if (bean == null) {
			throw new NullPointerException("Bean is null.");
		}
		
		if (metaData == null) {
			metaData = metaDataFor(bean);
		}
		
		return delegate.createMatchable(bean);
	}
	
	/**
	 * Create a list of the given property values.
	 * 
	 * @param bean
	 * @param names
	 * 
	 * @return The property values. Never null.
	 */
	private List<?> strip(Object bean, Iterable<String> names) {
		
		List<Object> values = new ArrayList<Object>();
		
		if (names != null) {
			for (String name : names) {
				values.add(accessor.getProperty(bean, name));
			}
		}
		
		return values;
	}
	
	/**
	 * Create the meta data.
	 * 
	 * @param bean An object on which to base the meta data.
	 * 
	 * @return The meta data. Never null.
	 */
	private MatchableMetaData metaDataFor(Object bean) {
				
		Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		
		ArooaClass arooaClass = accessor.getClassName(bean);
		
		BeanOverview overview = arooaClass.getBeanOverview(accessor);
		
		if (definition.getValueProperties() == null && 
				definition.getKeyProperties() == null) {
			
			types.put(VALUE_NAME, bean.getClass());
			addTypes(definition.getOtherProperties(), overview, types);
			
			return new SimpleMatchableMeta(null, Arrays.asList(VALUE_NAME), 
					definition.getOtherProperties(), types);
		}
		else {

			addTypes(definition.getKeyProperties(), overview, types);
			addTypes(definition.getValueProperties(), overview, types);
			addTypes(definition.getOtherProperties(), overview, types);
			
			return new SimpleMatchableMeta(definition, types);
		}
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

