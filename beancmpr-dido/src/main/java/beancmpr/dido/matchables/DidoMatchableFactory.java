package beancmpr.dido.matchables;

import dido.data.DataSchema;
import dido.data.DidoData;
import dido.data.generic.GenericData;
import dido.data.util.TypeUtil;
import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.matchables.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link MatchableFactory} that creates {@link Matchable}s from {@link GenericData}.
 *
 * @author rob
 */
public class DidoMatchableFactory implements MatchableFactory<DidoData> {

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
    public Matchable createMatchable(DidoData data) {

        if (data == null) {
            throw new NullPointerException("Bean is null.");
        }

        if (metaData == null) {
            DataSchema schema = data.getSchema();

            Map<String, Type> types = Stream.of(
                            definition.getKeyProperties().stream(),
                            definition.getValueProperties().stream(),
                            definition.getOtherProperties().stream())
                    .flatMap(Function.identity())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            name -> {
                                Type type = Objects.requireNonNull(schema.getTypeNamed(name),
                                        "No Property " + name + " in Schema " + schema);
                                if (TypeUtil.isPrimitive(type)) {
                                    type = ClassUtils.wrapperClassForPrimitive(TypeUtil.classOf(type));
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
     * @param didoData The data.
     * @param names The field names.
     * @return The property values. Never null.
     */
    private ImmutableCollection<Object> copy(DidoData didoData, ImmutableCollection<String> names) {

        return names.stream()
                .map(didoData::getNamed)
                .collect(ImmutableCollection.collector());
    }

}

