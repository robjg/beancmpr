package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparison;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Continuously Compare items based on a key.
 *
 * @param <K> The key type.
 * @param <V> The item type.
 */
public class ContinuousKeyedComparer<K extends Comparable<K>, V> {

    private final Function<K, ContinuousComparer<V>> comparerFactory;

    private final Map<K, ContinuousComparer<V>> comparer = new HashMap<>();

    public ContinuousKeyedComparer(ContinuousKeyedResults<K, V> resultsHandler,
                                   SourceStrategy<V> sourceStrategy,
                                   Supplier<? extends SourceHistory<V>> historyFactory,
                                   Consumer<? super Runnable> checkBacks) {
        this.comparerFactory = key ->
                new ContinuousComparer<>(new ContinuousResults<>() {
                    @Override
                    public void xMissing(V value) {
                        resultsHandler.xMissing(key, value);
                    }

                    @Override
                    public void yMissing(V value) {
                        resultsHandler.yMissing(key, value);
                    }

                    @Override
                    public void comparison(Comparison<? super V> comparison) {
                        resultsHandler.comparison(key, comparison);
                    }
                },
                        sourceStrategy,
                        historyFactory,
                        checkBacks);
    }


    public void acceptX(K key, V xItem) {

        ContinuousComparer<V> comparer = this.comparer.computeIfAbsent(key,
                comparerFactory);

        comparer.acceptX(xItem);
    }

    public void acceptY(K key, V yItem) {

        ContinuousComparer<V> comparer = this.comparer.computeIfAbsent(key,
                comparerFactory);

        comparer.acceptY(yItem);
    }

}
