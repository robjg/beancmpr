package org.oddjob.beancmpr.continuous;

import java.util.*;
import java.util.function.Supplier;

/**
 * A base class for {@link SourceHistory} keyed by something comparable.
 *
 * @param <C> The comparable for the key.
 * @param <V> The item history type.
 */
public class HistoryByComparable<C extends Comparable<C>, V> implements SourceHistory<V> {

    private final Supplier<C> keySupplier;

    private final NavigableMap<C, V> history = new TreeMap<>();

    public HistoryByComparable(Supplier<C> keySupplier) {
        this.keySupplier = keySupplier;
    }

    @Override
    public Entry<V> store(V item) {
        C key = keySupplier.get();
        history.put(key, item);
        return new KeyValue<>(key, item);
    }

    @Override
    public List<Entry<V>> allRecentFirst() {
        return history.reversed().entrySet().stream()
                .<Entry<V>>map(e -> new KeyValue<>(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    public List<Entry<V>> allOldestFirst() {
        return history.entrySet().stream()
                .<Entry<V>>map(e -> new KeyValue<>(e.getKey(), e.getValue()))
                .toList();
    }

    @Override
    public List<V> removeExpired() {
        List<V> expired = new ArrayList<>(history.values());
        history.clear();
        return expired;
    }

    public V remove(Entry<V> entry) {
        return history.remove(((KeyValue<C, V>) entry).key);
    }

    @Override
    public void removeIfOld(Entry<V> entry) {
        remove(entry);
    }

    static class KeyValue<C extends Comparable<C>, V> implements Entry<V> {

        private final C key;

        private final V value;

        KeyValue(C key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public V getItem() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            KeyValue<?, ?> keyValue = (KeyValue<?, ?>) o;
            return Objects.equals(key, keyValue.key) && Objects.equals(value, keyValue.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "Entry{key=" + key + ", value=" + value + '}';
        }
    }
}
