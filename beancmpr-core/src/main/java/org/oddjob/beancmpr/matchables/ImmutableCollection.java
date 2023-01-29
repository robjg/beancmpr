package org.oddjob.beancmpr.matchables;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Provides a Simple way creating more than an Iterable.
 *
 * @param <E> The element type.
 */
public interface ImmutableCollection<E> extends Iterable<E> {

    Stream<E> stream();

    int size();

    boolean isEmpty();

    static <E> ImmutableCollection<E> of(List<? extends E> list) {
        return ImmutableCollectionImpl.of(list);
    }

    static <E> ImmutableCollection<E> of(E... elements) {
        return ImmutableCollectionImpl.of(elements);
    }

    static <T> Collector<T, List<T>, ImmutableCollection<T>> collector() {

        return ImmutableCollectionImpl.collector();
    }
}
