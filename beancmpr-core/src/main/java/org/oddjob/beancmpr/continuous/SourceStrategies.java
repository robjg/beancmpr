package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.matchables.Matchable;import java.util.Objects;

/**
 * Encapsulate {@link SourceStrategyFactory}s as an enum, primarily for Oddjob configuration.
 */
public class SourceStrategies {

    private Strategy name;

    public enum Strategy implements SourceStrategyFactory<Matchable> {

        ONE_FOR_ONE(StrategyOneForOne::new) {
        },

        MANY_FOR_MANY(StrategyManyForMany::new) {
        };

        private final SourceStrategyFactory<Matchable> factory;

        Strategy(SourceStrategyFactory<Matchable> factory) {
            this.factory = factory;
        }

        @Override
        public SourceStrategy<Matchable> createStrategy(Comparer<? super Matchable> comparer) {
            return factory.createStrategy(comparer);
        }
    }

    public SourceStrategyFactory<Matchable> toSourceStrategy() {
        return Objects.requireNonNull(name, "No Strategy Specified.");
    }

    public Strategy getName() {
        return name;
    }

    public void setName(Strategy name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + name + ']';
    }
}
