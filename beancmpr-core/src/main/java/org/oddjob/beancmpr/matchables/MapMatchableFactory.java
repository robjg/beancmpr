package org.oddjob.beancmpr.matchables;

import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.BeanOverview;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.MatchDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link MatchableFactory} that creates {@link Matchable}s from beans.
 * 
 * @author rob
 *
 */
public class MapMatchableFactory<K, V> 
implements MatchableFactory<Map.Entry<K, V>> {

	/** The name of the matchable property when the key is the entire key 
	 * of the entry. */
	public static final String KEY_NAME = "key";
	
	/** The name of the matchable value when the value is the entire value 
	 * of the entry. */
	public static final String VALUE_NAME = "value";
	
	private final MatchDefinition definition;
	
	private final PropertyAccessor accessor;
	
	private MatchableMetaData metaData;
	
	/**
	 * Create a new instance.
	 * 
	 * @param definition The match definition.
	 * @param accessor The property accessor.
	 */
	public MapMatchableFactory(MatchDefinition definition,
			PropertyAccessor accessor) {
		this.definition = definition;
		this.accessor = accessor;
	}
	
	@Override
	public Matchable createMatchable(Map.Entry<K, V> entry) {

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
		
		ImmutableCollection<Object> keys;
		if (definition.getKeyProperties().isEmpty()) {
			keys = ImmutableCollectionImpl.of(entry.getKey());
		}
		else {
			keys = extract(entry.getKey(), definition.getKeyProperties());
		}
		
		ImmutableCollection<Object> comparables;
		if (definition.getKeyProperties().isEmpty()) {
			comparables = ImmutableCollectionImpl.of(entry.getValue());
		}
		else {
			comparables = extract(entry.getValue(), definition.getValueProperties());
		}
		
		ImmutableCollection<Object> others =
				extract(entry.getValue(), definition.getOtherProperties());

		return SimpleMatchable.of(keys, comparables, others, metaData);
	}
	
	/**
	 * Create a collection of the given property values.
	 * 
	 * @param bean the bean
	 * @param names the property names
	 * 
	 * @return The property values. Never null.
	 */
	private ImmutableCollection<Object> extract(Object bean, ImmutableCollection<String> names) {

		return names.stream()
				.map(name -> accessor.getProperty(bean, name))
				.collect(ImmutableCollection.collector());
	}
	
	/**
	 * Create the meta data.
	 * 
	 * @param entry A map entry on which to base the metadata.
	 * 
	 * @return The meta data. Never null.
	 */
	private MatchableMetaData metaDataFor(Map.Entry<?, ?> entry) {
		
		Map<String, Class<?>> types = new HashMap<>();
		
		ImmutableCollection<String> keyProperties = definition.getKeyProperties();
		if (keyProperties.isEmpty()) {
			types.put(KEY_NAME, entry.getKey().getClass());
			keyProperties = ImmutableCollection.of(KEY_NAME);
		}
		else {
			ArooaClass arooaClass = accessor.getClassName(entry.getKey());
		
			BeanOverview overview = arooaClass.getBeanOverview(accessor);
			
			for (String name : keyProperties) {
				types.put(name, overview.getPropertyType(name));
			}
		}
		
		ImmutableCollection<String> valueProperties = definition.getValueProperties();
		if (valueProperties.isEmpty()) {
			types.put(VALUE_NAME, entry.getValue().getClass());
			valueProperties = ImmutableCollection.of(VALUE_NAME);
		}
		else {
			ArooaClass arooaClass = accessor.getClassName(entry.getValue());
			
			BeanOverview overview = arooaClass.getBeanOverview(accessor);			
			
			for (String name : valueProperties) {
				types.put(name, overview.getPropertyType(name));
			}
			
			for (String name : definition.getOtherProperties()) {
				types.put(name, overview.getPropertyType(name));
			}
		}
		
		return SimpleMatchableMeta.of(keyProperties, valueProperties,
				definition.getOtherProperties(), types);
	}	
	
}

