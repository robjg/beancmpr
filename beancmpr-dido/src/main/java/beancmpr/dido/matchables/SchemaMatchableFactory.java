package beancmpr.dido.matchables;

import dido.data.DataSchema;
import dido.data.DidoData;
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

public class SchemaMatchableFactory implements MatchableFactory<DidoData> {

    private final MatchableMetaData metaData;

    private final Function<DidoData, ImmutableCollection<Object>> keys;

    private final Function<DidoData, ImmutableCollection<Object>> values;

    private final Function<DidoData, ImmutableCollection<Object>> others;

    public SchemaMatchableFactory(MatchableMetaData metaData,
                                  Function<DidoData, ImmutableCollection<Object>> keys,
                                  Function<DidoData, ImmutableCollection<Object>> values,
                                  Function<DidoData, ImmutableCollection<Object>> others) {
        this.metaData = metaData;
        this.keys = keys;
        this.values = values;
        this.others = others;
    }

    public static SchemaMatchableFactory from(DataSchema schema, MatchDefinition definition) {

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

        return new SchemaMatchableFactory(
                SimpleMatchableMeta.of(definition,
                types::get),
                DidoImmutableCollection.factoryFrom(schema, definition.getKeyProperties()),
                DidoImmutableCollection.factoryFrom(schema, definition.getValueProperties()),
                DidoImmutableCollection.factoryFrom(schema, definition.getOtherProperties()));
    }

    @Override
    public Matchable createMatchable(DidoData data) {
        return SimpleMatchable.of(keys.apply(data),
                values.apply(data),
                others.apply(data),
                metaData);
    }

    public MatchableMetaData getMetaData() {
        return metaData;
    }
}
