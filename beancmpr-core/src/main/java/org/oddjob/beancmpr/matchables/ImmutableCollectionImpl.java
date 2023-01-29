package org.oddjob.beancmpr.matchables;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

class ImmutableCollectionImpl<E> implements ImmutableCollection<E> {

    private final List<E> elements;

    private ImmutableCollectionImpl(List<E> elements) {
        this.elements = elements;
    }

    public static <E> ImmutableCollection<E> of(List<? extends E> list) {
        return new ImmutableCollectionImpl<>(new ArrayList(list));
    }

    public static <E> ImmutableCollection<E> of(E... elements) {
        return new ImmutableCollectionImpl<>(Arrays.asList(elements));
    }

    static <T> Collector<T, List<T>, ImmutableCollection<T>> collector() {


        return new Collector<>() {

            @Override
            public Supplier<List<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<List<T>, T> accumulator() {
                return (l, r) -> l.add(r);
            }

            @Override
            public BinaryOperator<List<T>> combiner() {
                return (l, r) -> { l.addAll(r); return l; };
            }

            @Override
            public Function<List<T>, ImmutableCollection<T>> finisher() {
                return l ->  new ImmutableCollectionImpl<>(l);
            }

            @Override
            public Set<Characteristics> characteristics() {
                return Collections.emptySet();
            }
        };
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableCollectionImpl<?> that = (ImmutableCollectionImpl<?>) o;
        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
