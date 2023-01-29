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
 */
public class BeanMatchableFactory<T> implements MatchableFactory<T> {

    /**
     * The name of the matchable value when the value is the entire value
     * of the entry.
     */
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

        return SimpleMatchable.of(
                strip(bean, definition.getKeyProperties()),
                strip(bean, definition.getValueProperties()),
                strip(bean, definition.getOtherProperties()),
                metaData);
    }

    /**
     * Create a list of the given property values.
     *
     * @param bean
     * @param names
     * @return The property values. Never null.
     */
    private ImmutableCollection<Object> strip(Object bean, ImmutableCollection<String> names) {

        if (names == null) {
            return ImmutableCollection.of();
        }

        return names.stream()
                .map(name -> {
                    if (SELF_TOKEN.equals(name)) {
                        return bean;
                    } else {
                        return accessor.getProperty(bean, name);
                    }
                })
                .collect(ImmutableCollection.collector());

    }

    /**
     * Create the meta data.
     *
     * @param bean An object on which to base the meta data.
     * @return The meta data. Never null.
     */
    private MatchableMetaData metaDataFor(Object bean) {

        TypeAdder types = new TypeAdder(bean);

        return SimpleMatchableMeta.of(types.addTypes(definition.getKeyProperties()),
                types.addTypes(definition.getValueProperties()),
                types.addTypes(definition.getOtherProperties()),
                types.getTypes());
    }

    /**
     * Used by the above to accumulate property types for the key,
     * value, and other properties.
     */
    private class TypeAdder {

        private final Object bean;

        private final Map<String, Class<?>> types = new HashMap<>();

        private BeanOverview overview;

        TypeAdder(Object bean) {
            this.bean = bean;
        }

        /**
         * @param propertyNames The names of the properties
         */

        ImmutableCollection<String> addTypes(
                ImmutableCollection<String> propertyNames) {

            if (propertyNames == null) {
                return ImmutableCollection.of();
            }

            return propertyNames.stream()
                    .map(name -> {
                        if (SELF_TOKEN.equals(name)) {
                            types.put(VALUE_NAME, bean.getClass());
                            return VALUE_NAME;
                        } else {
                            types.put(name, getBeanOverview().getPropertyType(name));
                            return name;
                        }
                    })
                    .collect(ImmutableCollection.collector());
        }

        BeanOverview getBeanOverview() {
            if (overview == null) {
                if (accessor == null) {
                    throw new NullPointerException(
                            "No Property Accessor. Set Arooa Session.");
                }
                ArooaClass arooaClass = accessor.getClassName(bean);
                overview = arooaClass.getBeanOverview(accessor);
            }
            return overview;
        }

        Map<String, Class<?>> getTypes() {
            return types;
        }

    }
}

