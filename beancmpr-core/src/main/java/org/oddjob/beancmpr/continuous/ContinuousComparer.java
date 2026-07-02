package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparison;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Continuously compare 2 streams of items.
 *
 * @param <V> The type of item.
 */
public class ContinuousComparer<V> {

    private final ContinuousResults<V> resultsHandler;

    private final SourceStrategy<V> sourceStrategy;

    private final SourceHistory<V> xHistory;

    private final SourceHistory<V> yHistory;

    private final Consumer<? super Runnable> checkBacks;

    public ContinuousComparer(ContinuousResults<V> resultsHandler,
                              SourceStrategy<V> sourceStrategy,
                              Supplier<? extends SourceHistory<V>> historyFactory,
                              Consumer<? super Runnable> checkBacks) {
        this.resultsHandler = resultsHandler;
        this.sourceStrategy = sourceStrategy;
        this.xHistory = historyFactory.get();
        this.yHistory = historyFactory.get();
        this.checkBacks = checkBacks;
    }

    public void acceptX(V xItem) {

        sourceStrategy.onX(xItem, xHistory, yHistory,
                new SourceStrategy.Results<>() {

                    @Override
                    public void comparison(Comparison<? super V> comparison) {
                        resultsHandler.comparison(comparison);
                    }

                    @Override
                    public void theirMissing(V theirItem) {
                        resultsHandler.yMissing(theirItem);
                    }
                }).ifPresent(checkBacks);
    }

    public void acceptY(V yItem) {

        sourceStrategy.onY(yItem, yHistory, xHistory,
                new SourceStrategy.Results<>() {

                    @Override
                    public void comparison(Comparison<? super V> comparison) {
                        resultsHandler.comparison(comparison);
                    }

                    @Override
                    public void theirMissing(V theirItem) {
                        resultsHandler.xMissing(theirItem);
                    }
                }).ifPresent(checkBacks);
    }
}
