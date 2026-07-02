package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;
import org.oddjob.beancmpr.matchables.*;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MatchableContinuousComparer {

    private final ContinuousKeyedComparer<SimpleMatchKey, Matchable> continuousComparer;

    private final Function<Iterable<?>, SimpleMatchKey> keyFunc;

    public MatchableContinuousComparer(CompareResultsHandler resultsHandler,
                                       Function<Iterable<?>, SimpleMatchKey> keyFunc,
                                       SourceStrategy<Matchable> sourceStrategy,
                                       Supplier<? extends SourceHistory<Matchable>> historyFactory,
                                       Consumer<? super Runnable> checkBacks) {
        this.continuousComparer = new ContinuousKeyedComparer<>(
                new ContinuousKeyedResults<SimpleMatchKey, Matchable>() {
                    @Override
                    public void xMissing(SimpleMatchKey key, Matchable value) {
                        resultsHandler.xMissing(MatchableGroup.of(value));
                    }

                    @Override
                    public void yMissing(SimpleMatchKey key, Matchable value) {
                        resultsHandler.yMissing(MatchableGroup.of(value));
                    }

                    @Override
                    public void comparison(SimpleMatchKey key, Comparison<? super Matchable> comparison) {
                        resultsHandler.compared((MultiValueComparison<Matchable>) comparison);
                    }
                },
                sourceStrategy,
                historyFactory,
                checkBacks);
        this.keyFunc = keyFunc;
    }

    public static class Settings {

        private CompareResultsHandler resultsHandler;

        private BeanPropertyComparerProvider  comparerProvider;

        public Settings resultsHandler(CompareResultsHandler resultsHandler) {

            this.resultsHandler = resultsHandler;
            return this;
        }

        public Settings comparerProvider(BeanPropertyComparerProvider comparerProvider) {
            this.comparerProvider = comparerProvider;
            return this;
        }

        public MatchableContinuousComparer createFor(
                SourceStrategy<Matchable> sourceStrategy,
                Supplier<? extends SourceHistory<Matchable>> historyFactory,
                MatchableMetaData metaData,
                Consumer<? super Runnable> checkBacks) {

            Function<Iterable<?>, SimpleMatchKey> keyFunc =
                    SimpleMatchKey.creatorFrom(
                            Objects.requireNonNullElseGet(comparerProvider, ComparersByNameOrType::new),
                            metaData);

            return new MatchableContinuousComparer(resultsHandler,
                    keyFunc,
                    sourceStrategy,
                    historyFactory,
                    checkBacks);
        }
    }

    public static Settings with() {
        return new Settings();
    }


    public void acceptX(Matchable x) {

        SimpleMatchKey key = keyFunc.apply(x.getKeys());
        continuousComparer.acceptX(key, x);
    }

    public void acceptY(Matchable y) {

        SimpleMatchKey key = keyFunc.apply(y.getKeys());
        continuousComparer.acceptY(key, y);
    }

}
