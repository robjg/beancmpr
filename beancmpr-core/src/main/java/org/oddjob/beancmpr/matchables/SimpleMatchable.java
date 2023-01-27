package org.oddjob.beancmpr.matchables;

import java.util.List;
import java.util.Objects;

/**
 * A simple implementation of a {@link Matchable}.
 *
 * @author rob
 */
public class SimpleMatchable implements Matchable {

    private final ImmutableCollection<?> keys;

    private final ImmutableCollection<?> values;

    private final ImmutableCollection<?> others;

    private final MatchableMetaData metaData;

    private SimpleMatchable(ImmutableCollection<?> keys, ImmutableCollection<?> values,
                            ImmutableCollection<?> others, MatchableMetaData metaData) {

        this.keys = keys;
        this.values = values;
        this.others = others;
        this.metaData = metaData;
    }

    /**
     * @param keys   The key values.
     * @param values The values to compare.
     * @param others Other values.
     */
    public SimpleMatchable(List<?> keys, List<?> values,
                           List<?> others, MatchableMetaData metaData) {

        this(keys == null ? ImmutableCollection.of() : ImmutableCollection.of(keys),
                values == null ? ImmutableCollection.of() : ImmutableCollection.of(values),
                others == null ? ImmutableCollection.of() : ImmutableCollection.of(others),
                metaData);
    }

    public static Matchable of(ImmutableCollection<?> keys, ImmutableCollection<?> values,
                     ImmutableCollection<?> others, MatchableMetaData metaData) {
        return new SimpleMatchable(
                Objects.requireNonNull(keys, "No Keys"),
                Objects.requireNonNull(values, "No Values"),
                Objects.requireNonNull(others, "No Others"),
                Objects.requireNonNull(metaData, "No Metadata"));
    }

    @Override
    public Iterable<?> getKeys() {
        return keys;
    }

    @Override
    public Iterable<?> getValues() {
        return values;
    }

    @Override
    public Iterable<?> getOthers() {
        return others;
    }

    public MatchableMetaData getMetaData() {
        return metaData;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ", key=" + keys +
                ", values = " + values + ", other=" + others;
    }
}