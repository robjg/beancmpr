package org.oddjob.beancmpr.matchables;

import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.MatchDefinition;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

/**
 * A simple implementation of {@link MatchableMetaData}.
 * 
 * @author rob
 *
 */
public class SimpleMatchableMeta implements MatchableMetaData {

	private final ImmutableCollection<String> keyProperties;
	
	private final ImmutableCollection<String> valueProperties;
	
	private final ImmutableCollection<String> otherProperties;
	
	private final Function<? super String, ? extends Type> types;

	private SimpleMatchableMeta(ImmutableCollection<String> keyProperties,
							   ImmutableCollection<String> valueProperties,
							   ImmutableCollection<String> otherProperties,
								Function<? super String, ? extends Type> typeLookup) {

		this.keyProperties = Objects.requireNonNull(keyProperties);
		this.valueProperties = Objects.requireNonNull(valueProperties);
		this.otherProperties = Objects.requireNonNull(otherProperties);
		
		this.types = typeLookup;
	}

	public static MatchableMetaData of(ImmutableCollection<String> keyProperties,
								ImmutableCollection<String> valueProperties,
								ImmutableCollection<String> otherProperties,
								Map<String, Class<?>> types) {

		final Map<String, Class<?>> typeCopy = new HashMap<>();
		for (Map.Entry<String, Class<?>> entry: types.entrySet()) {
			Class<?> type = entry.getValue();
			String property = entry.getKey();
			if (type == null) {
				throw new IllegalArgumentException("No property " + property);
			}
			if (type.isPrimitive()) {
				type = ClassUtils.wrapperClassForPrimitive(type);
			}
			typeCopy.put(property, type);
		}

		return new SimpleMatchableMeta(keyProperties, valueProperties, otherProperties,
                typeCopy::get);
	}

	public static MatchableMetaData of(MatchDefinition definition,
									   Function<? super String, ? extends Type> typeLookup) {
		return new SimpleMatchableMeta(definition.getKeyProperties(),
				definition.getValueProperties(),
				definition.getOtherProperties(),
				typeLookup);
	}

	public static class Builder {

		private final Map<String, Class<?>> types = new HashMap<>();

		private final List<String> keys = new ArrayList<>();

		private final List<String> values = new ArrayList<>();

		private final List<String> others = new ArrayList<>();

		public Builder addKey(String name, Class<?> type) {
			keys.add(Objects.requireNonNull(name));
			types.put(name, Objects.requireNonNull(type));
			return this;
		}

		public Builder addValue(String name, Class<?> type) {
			values.add(Objects.requireNonNull(name));
			types.put(name, Objects.requireNonNull(type));
			return this;
		}

		public Builder addOther(String name, Class<?> type) {
			others.add(Objects.requireNonNull(name));
			types.put(name, Objects.requireNonNull(type));
			return this;
		}

		public MatchableMetaData build() {

			return of(ImmutableCollection.of(keys),
					ImmutableCollection.of(values),
					ImmutableCollection.of(others),
					types);
		}
	}

	public static Builder builder() {

		return new Builder();
	}



	@Override
	public ImmutableCollection<String> getKeyProperties() {
		return keyProperties;
	}
	
	@Override
	public ImmutableCollection<String> getValueProperties() {
		return valueProperties;
	}
	
	@Override
	public ImmutableCollection<String> getOtherProperties() {
		return otherProperties;
	}
	
	@Override
	public Type getPropertyType(String name) {
		return types.apply(name);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": types " + types;
	}
}
