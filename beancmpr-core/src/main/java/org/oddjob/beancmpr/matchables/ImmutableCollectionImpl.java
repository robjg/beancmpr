package org.oddjob.beancmpr.matchables;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

class ImmutableCollectionImpl<E> implements ImmutableCollection<E> {

    private final List<E> elements;

    private ImmutableCollectionImpl(List<E> elements) {
        this.elements = elements;
    }

    public static <E> ImmutableCollection<E> of(List<E> list) {
        return new ImmutableCollectionImpl<>(List.copyOf(list));
    }

    public static <E> ImmutableCollection<E> of(E... elements) {
        return new ImmutableCollectionImpl<>(Arrays.asList(elements));
    }

    @Override
    public Stream<E> stream() {
        return elements.stream();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return elements.listIterator();
    }
}
