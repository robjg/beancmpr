package org.oddjob.beancmpr;

import org.oddjob.beancmpr.matchables.ImmutableCollection;

/**
 * Simple definition of an {@link MatchDefinition}.
 *
 * @author rob
 */
public class SimpleMatchDefinition implements MatchDefinition {

    /**
     * Key property names.
     */
    private final ImmutableCollection<String> keyProperties;

    /**
     * Values for comparison property names.
     */
    private final ImmutableCollection<String> valueProperties;

    /**
     * Other property names.
     */
    private final ImmutableCollection<String> otherProperties;

    /**
     * Private Constructor.
     */
    private SimpleMatchDefinition(ImmutableCollection<String> keyProperties,
                                  ImmutableCollection<String> valueProperties,
                                  ImmutableCollection<String> otherProperties) {
        this.keyProperties = keyProperties;
        this.valueProperties = valueProperties;
        this.otherProperties = otherProperties;
    }

    /**
     * Constructor.
     *
     * @param keys   Array of key property names. May be null.
     * @param values Array of value property names. May be null.
     * @param others Array of other property names. May be null.
     */
    public static MatchDefinition of(String[] keys,
                                     String[] values,
                                     String[] others) {

        return new SimpleMatchDefinition(
                keys == null ?
                        ImmutableCollection.of() : ImmutableCollection.of(keys),
                values == null ?
                        ImmutableCollection.of() : ImmutableCollection.of(values),
                others == null ?
                        ImmutableCollection.of() : ImmutableCollection.of(others));
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
    public String toString() {
        return getClass().getSimpleName() + ": keys " +
                keyProperties + ", values " +
                valueProperties + ", others " +
                otherProperties;
    }
}
