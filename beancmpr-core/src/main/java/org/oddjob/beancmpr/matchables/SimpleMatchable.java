package org.oddjob.beancmpr.matchables;

import java.util.List;
import java.util.Objects;

/**
 * A simple implementation of a {@link Matchable}.
 *
 * @author rob
 */
public class SimpleMatchable implements Matchable {

    private final ImmutableCollection<Object> keys;

    private final ImmutableCollection<Object> values;

    private final ImmutableCollection<Object> others;

    private final MatchableMetaData metaData;

    private SimpleMatchable(ImmutableCollection<Object> keys,
                            ImmutableCollection<Object> values,
                            ImmutableCollection<Object> others, MatchableMetaData metaData) {

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
    public SimpleMatchable(List<?> keys,
                           List<?> values,
                           List<?> others,
                           MatchableMetaData metaData) {

        this(keys == null ? ImmutableCollection.of() : ImmutableCollection.of(keys),
                values == null ? ImmutableCollection.of() : ImmutableCollection.of(values),
                others == null ? ImmutableCollection.of() : ImmutableCollection.of(others),
                metaData);
    }

    public static Matchable of(ImmutableCollection<Object> keys,
                               ImmutableCollection<Object> values,
                               ImmutableCollection<Object> others,
                               MatchableMetaData metaData) {

        return new SimpleMatchable(
                Objects.requireNonNull(keys, "No Keys"),
                Objects.requireNonNull(values, "No Values"),
                Objects.requireNonNull(others, "No Others"),
                Objects.requireNonNull(metaData, "No Metadata"));
    }

    @Override
    public ImmutableCollection<Object> getKeys() {
        return keys;
    }

    @Override
    public ImmutableCollection<Object> getValues() {
        return values;
    }

    @Override
    public ImmutableCollection<Object> getOthers() {
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