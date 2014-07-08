package org.oddjob.beancmpr.matchables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
	
	public static final String SELF_TOKEN = "@SELF";
	
	private final MatchDefinition definition;
	
	private final PropertyAccessor accessor;
	
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
	}
	
	@Override
	public Matchable createMatchable(T bean) {

		if (bean == null) {
			throw new NullPointerException("Bean is null.");
		}
		
		if (metaData == null) {
			metaData = metaDataFor(bean);
		}
		
		List<?> keys= strip(bean, definition.getKeyProperties());		
		List<?> comparables = strip(bean, definition.getValueProperties());
		List<?> others = strip(bean, definition.getOtherProperties());
		
		return new SimpleMatchable(keys, comparables, others, 
				metaData);
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
				if (SELF_TOKEN.equals(name)) {
					values.add(bean);
				}
				else {
					values.add(accessor.getProperty(bean, name));
				}
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
		AtomicReference<BeanOverview> overview = new AtomicReference<>();
		
		List<String> keys = new ArrayList<>();
		List<String> values = new ArrayList<>();
		List<String> others = new ArrayList<>();
		
		addTypes(bean, definition.getKeyProperties(), 
				overview, keys, types);
		addTypes(bean, definition.getValueProperties(), 
				overview, values, types);
		addTypes(bean, definition.getOtherProperties(), 
				overview, others, types);
		
		return new SimpleMatchableMeta(keys, values, others, types);
	}	
	
	/**
	 * Used by the above.
	 * 
	 * @param propertyNames
	 * @param overview
	 * @param types
	 */
	private void addTypes(Object bean, Iterable<String> propertyNames, 
			AtomicReference<BeanOverview> overview, 
			List<String> propertyNamesOut,
			Map<String, Class<?>> types) {
		
		if (propertyNames != null) {
			for (String name : propertyNames) {
				if (SELF_TOKEN.equals(name)) {
					types.put(VALUE_NAME, bean.getClass());
					propertyNamesOut.add(VALUE_NAME);
				}
				else {
					if (overview.get() == null) {
						ArooaClass arooaClass = accessor.getClassName(bean);
						overview.set(arooaClass.getBeanOverview(accessor));
					}
					types.put(name, overview.get().getPropertyType(name));
					propertyNamesOut.add(name);
				}
			}
		}		
	}
		
}

