package beancmpr.dido.matchables;

import dido.data.DataSchema;
import dido.data.GenericData;
import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.matchables.*;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link MatchableFactory} that creates {@link Matchable}s from {@link GenericData}.
 *
 * @author rob
 */
public class DidoMatchableFactory implements MatchableFactory<GenericData<String>> {

    private final MatchDefinition definition;

    private volatile MatchableMetaData metaData;

    /**
     * Create a new instance.
     *
     * @param definition The match definition.
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
            DataSchema<String> schema = data.getSchema();

            Map<String, Class<?>> types = Stream.of(
                            definition.getKeyProperties().stream(),
                            definition.getValueProperties().stream(),
                            definition.getOtherProperties().stream())
                    .flatMap(Function.identity())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            name -> {
                                Class<?> type = schema.getType(name);
                                if (type.isPrimitive()) {
                                    type = ClassUtils.wrapperClassForPrimitive(type);
                                }
                                return type;
                            }));

            metaData = SimpleMatchableMeta.of(definition,
                    types::get);
        }

        ImmutableCollection<Object> keys = copy(data, definition.getKeyProperties());
        ImmutableCollection<Object> comparables = copy(data, definition.getValueProperties());
        ImmutableCollection<Object> others = copy(data, definition.getOtherProperties());

        return SimpleMatchable.of(keys, comparables, others,
                metaData);
    }

    /**
     * Create a list of the given property values.
     *
     * @param genericData
     * @param names
     * @return The property values. Never null.
     */
    private ImmutableCollection<Object> copy(GenericData<String> genericData, ImmutableCollection<String> names) {

        return names.stream()
                .map(genericData::get)
                .collect(ImmutableCollection.collector());
    }

}

