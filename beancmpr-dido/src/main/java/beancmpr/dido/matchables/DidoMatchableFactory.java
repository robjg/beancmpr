package beancmpr.dido.matchables;

import dido.data.DataSchema;
import dido.data.GenericData;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.matchables.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link MatchableFactory} that creates {@link Matchable}s from {@link GenericData}.
 * 
 * @author rob
 *
 */
public class DidoMatchableFactory implements MatchableFactory<GenericData<String>> {

	/** The name of the matchable value when the value is the entire value 
	 * of the entry. */
	public static final String VALUE_NAME = "value";
	
	public static final String SELF_TOKEN = "@SELF";
	
	private final MatchDefinition definition;
	
	private volatile MatchableMetaData metaData;
	
	/**
	 * Create a new instance.
	 * 
	 * @param definition
	 */
	public DidoMatchableFactory(final MatchDefinition definition) {
		this.definition = definition;
	}
	
	@Override
	public Matchable createMatchable(GenericData<String> data) {

		if (data == null) {
			throw new NullPointerException("Bean is null.");
		}
		
		if (metaData == null) {
			metaData = metaDataFor(data);
		}
		
		List<?> keys= strip(data, definition.getKeyProperties());
		List<?> comparables = strip(data, definition.getValueProperties());
		List<?> others = strip(data, definition.getOtherProperties());
		
		return new SimpleMatchable(keys, comparables, others,
				metaData);
	}
	
	/**
	 * Create a list of the given property values.
	 * 
	 * @param genericData
	 * @param names
	 * 
	 * @return The property values. Never null.
	 */
	private List<?> strip(GenericData<String> genericData, Iterable<String> names) {
		
		List<Object> values = new ArrayList<>();
		
		if (names != null) {
			for (String name : names) {
				if (SELF_TOKEN.equals(name)) {
					values.add(genericData);
				}
				else {
					values.add(genericData.get(name));
				}
			}
		}
		
		return values;
	}
	
	/**
	 * Create the meta data.
	 * 
	 * @param data An object on which to base the meta data.
	 * 
	 * @return The meta data. Never null.
	 */
	private MatchableMetaData metaDataFor(GenericData<String> data) {
				
		TypeAdder types = new TypeAdder(data.getSchema());
		
		List<String> keys = new ArrayList<>();
		List<String> values = new ArrayList<>();
		List<String> others = new ArrayList<>();
		
		types.addTypes(definition.getKeyProperties(), keys);
		types.addTypes(definition.getValueProperties(), values);
		types.addTypes(definition.getOtherProperties(), others);
		
		return new SimpleMatchableMeta(keys, values, others,
				types.getTypes());
	}	
	
	/**
	 * Used by the above to accumulate property types for the key,
	 * value, and other properties.
	 */
	private static class TypeAdder {

		private final Map<String, Class<?>> types = new HashMap<>();
		
		private final DataSchema<String> overview;
		
		TypeAdder(DataSchema<String> schema) {
			this.overview = schema;
		}
		
		/**
		 * @param propertyNames
		 * @param propertyNamesOut
		 */
			
		void addTypes(
				Iterable<String> propertyNames, 
				List<String> propertyNamesOut) {
			
			if (propertyNames != null) {
				for (String name : propertyNames) {
					if (SELF_TOKEN.equals(name)) {
						types.put(VALUE_NAME, GenericData.class);
						propertyNamesOut.add(VALUE_NAME);
					}
					else {
						types.put(name, overview.getType(name));
						propertyNamesOut.add(name);
					}
				}
			}
		}
		
		Map<String, Class<?>> getTypes() {
			return types;
		}
		
	}


}

