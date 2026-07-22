package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.matchables.Matchable;

import java.util.Objects;

/**
 * @oddjob.description Provides a way to configure a Strategy for comparing
 * sources for Oddjob. Current strategies are:
 * <ul>
 *     <li>ONE_FOR_ONE: Trys to match each X to each Y</li>
 *     <li>MANY_FOR_MANY: Any number of Xs will Match any number of Ys. For instance if
 *     you get 2 Xs that match a single Y, then 2 matches are reported, as opposed
 *     to the ONE_FOR_ONE strategy where the second X would result in a missing Y result.
 *     One use case is comparing a ticking price stream with a throttled price
 *     stream to ensure the throttled stream had approximately the same data within
 *     a tolerance period.</li>
 * </ul>
 */
public class SourceStrategies {

    /**
     * @oddjob.description The name of the strategy.
     * @oddjob.required Yes.
     */
    private Strategy strategy;

    /**
     * Encapsulate {@link SourceStrategyFactory}s as an enum.
     */
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
        return Objects.requireNonNull(strategy, "No Strategy Specified.");
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[name=" + strategy + ']';
    }
}
