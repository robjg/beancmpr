package beancmpr.dido.matchables;

import dido.data.DataSchema;
import dido.data.DidoData;
import dido.data.FieldGetter;
import dido.data.ReadSchema;
import org.jetbrains.annotations.NotNull;
import org.oddjob.beancmpr.matchables.ImmutableCollection;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public class DidoImmutableCollection implements ImmutableCollection<Object> {

    private final FieldGetter[] getters;

    private final DidoData data;

    public DidoImmutableCollection(FieldGetter[] getters, DidoData data) {
        this.getters = getters;
        this.data = data;
    }

    public static Function<DidoData, ImmutableCollection<Object>> factoryFrom(
            DataSchema schema, String... fieldNames) {

        return factoryFrom(schema, ImmutableCollection.of(fieldNames));
    }

    public static Function<DidoData, ImmutableCollection<Object>> factoryFrom(
            DataSchema schema, ImmutableCollection<String> names) {

        ReadSchema readSchema = ReadSchema.from(schema);

        FieldGetter[] getters = new FieldGetter[names.size()];
        for (int i = 0; i < names.size(); i++) {
            getters[i] = readSchema.getFieldGetterNamed(names.get(i));
        }

        return data ->
            new DidoImmutableCollection(getters, data);
    }


    @Override
    public Stream<Object> stream() {

        return Arrays.stream(getters).map(getter -> getter.get(data));
    }

    @Override
    public int size() {
        return getters.length;
    }

    @Override
    public boolean isEmpty() {
        return getters.length == 0;
    }

    @Override
    public Object get(int index) {
        return getters[index].get(data);
    }

    @Override
    public @NotNull Iterator<Object> iterator() {
        return stream().iterator();
    }
}
