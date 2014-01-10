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
public class MapMatchableFactory 
implements MatchableFactory<Map.Entry<?, ?>> {

	/** The name of the matchable property when the key is the entire key 
	 * of the entry. */
	private static final String KEY_NAME = "key";
	
	/** The name of the matchable value when the value is the entire value 
	 * of the entry. */
	private static final String VALUE_NAME = "value";
	
	private final MatchDefinition definition;
	
	private final PropertyAccessor accessor;
	
	private MatchableMetaData metaData;
	
	/**
	 * Create a new instance.
	 * 
	 * @param definition
	 * @param accessor
	 */
	public MapMatchableFactory(MatchDefinition definition,
			PropertyAccessor accessor) {
		this.definition = definition;
		this.accessor = accessor;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Matchable createMatchable(Map.Entry<?, ?> entry) {

		if (entry == null) {
			throw new NullPointerException("No Map Entry.");
		}
		
		if (entry.getKey() == null) {
			throw new NullPointerException("No Key in Map Entry.");
		}
		
		if (entry.getValue() == null) {
			throw new NullPointerException("No Value in Map Entry.");
		}
		
		if (metaData == null) {
			metaData = metaDataFor(entry);
		}
		
		List<?> keys; 
		if (definition.getKeyProperties() == null) {
			keys = Arrays.asList(entry.getKey());
		}
		else {
			keys = strip(entry.getKey(), definition.getKeyProperties());		
		}
		
		List<?> comparables;
		if (definition.getKeyProperties() == null) {
			comparables = Arrays.asList(entry.getValue());
		}
		else {
			comparables = strip(entry.getValue(), definition.getValueProperties());
		}
		
		List<?> others;
		if (definition.getOtherProperties() == null) {
			others = new ArrayList<Object>();
		}
		else {
			others = strip(entry.getValue(), definition.getOtherProperties());
		}
		
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
		for (String name : names) {
			values.add((T) accessor.getProperty(bean, name));
		}
		return values;
	}
	
	/**
	 * 
	 * @param bean
	 * 
	 * @return
	 */
	private MatchableMetaData metaDataFor(Map.Entry<?, ?> entry) {
		
		Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		
		Iterable<String> keyProperties = definition.getKeyProperties();
		if (keyProperties == null) {
			types.put(KEY_NAME, entry.getKey().getClass());
			keyProperties = Arrays.asList(KEY_NAME);
		}
		else {
			ArooaClass arooaClass = accessor.getClassName(entry.getKey());
		
			BeanOverview overview = arooaClass.getBeanOverview(accessor);
			
			for (String name : keyProperties) {
				types.put(name, overview.getPropertyType(name));
			}
		}
		
		Iterable<String> valueProperties = definition.getValueProperties();
		if (valueProperties == null) {
			types.put(VALUE_NAME, entry.getValue().getClass());
			valueProperties = Arrays.asList(VALUE_NAME);
		}
		else {
			ArooaClass arooaClass = accessor.getClassName(entry.getValue());
			
			BeanOverview overview = arooaClass.getBeanOverview(accessor);			
			
			for (String name : valueProperties) {
				types.put(name, overview.getPropertyType(name));
			}
			
			if (definition.getOtherProperties() != null) {
				for (String name : definition.getOtherProperties()) {
					types.put(name, overview.getPropertyType(name));
				}
			}
		}
		
		return new SimpleMatchableMeta(keyProperties, valueProperties, 
				definition.getOtherProperties(), types);
	}	
	
}

