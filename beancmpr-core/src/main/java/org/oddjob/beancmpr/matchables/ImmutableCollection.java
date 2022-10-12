package org.oddjob.beancmpr.matchables;

import java.util.List;
import java.util.stream.Stream;

public interface ImmutableCollection<T> extends Iterable<T> {

    Stream<T> stream();

    int size();

    boolean isEmpty();

    static <E> ImmutableCollection<E> of(List<E> list) {
        return ImmutableCollectionImpl.of(list);
    }

    static <E> ImmutableCollection<E> of(E... elements) {
        return ImmutableCollectionImpl.of(elements);
    }

}
