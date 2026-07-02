package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparerFactory;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TimedMatchableComparer implements AutoCloseable {

    private final MatchableContinuousComparer continuousComparer;

    private final ScheduledExecutorService scheduler;

    private final List<Runnable> shutdownTasks;

    public TimedMatchableComparer(MatchableContinuousComparer continuousComparer,
                                  ScheduledExecutorService scheduler,
                                  List<Runnable> shutdownTasks) {
        this.continuousComparer = continuousComparer;
        this.scheduler = scheduler;
        this.shutdownTasks = shutdownTasks;
    }

    public static class Settings {

        private CompareResultsHandler resultsHandler;

        private InstantSource clock;

        private BeanPropertyComparerProvider  comparerProvider;

        private SourceStrategy<Matchable> sourceStrategy;

        private Duration tolerance;

        private ScheduledExecutorService scheduler;

        public Settings resultsHandler(CompareResultsHandler resultsHandler) {

            this.resultsHandler = resultsHandler;
            return this;
        }

        public Settings clock(InstantSource clock) {
            this.clock = clock;
            return this;
        }

        public Settings comparerProvider(BeanPropertyComparerProvider  comparerProvider) {
            this.comparerProvider = comparerProvider;
            return this;
        }

        public Settings sourceStrategy(SourceStrategy<Matchable> sourceStrategy) {
            this.sourceStrategy = sourceStrategy;
            return this;
        }

        public Settings tolerance(Duration tolerance) {
            this.tolerance = tolerance;
            return this;
        }

        public Settings scheduler(ScheduledExecutorService scheduler) {
            this.scheduler = scheduler;
            return this;
        }


        public TimedMatchableComparer createFor(MatchableMetaData metaData) {

            InstantSource clock = Objects.requireNonNullElseGet(this.clock,
                    () -> Instant::now);

            BeanPropertyComparerProvider comparerProvider = Objects.requireNonNullElseGet(
                    this.comparerProvider, ComparersByNameOrType::new);

            SourceStrategy<Matchable> strategy = Objects.requireNonNullElseGet(this.sourceStrategy,
                    () -> new StrategyOneForOne<>(new MatchableComparerFactory(
                            comparerProvider).createComparerFor(metaData, metaData)));

            Supplier<SourceHistory<Matchable>> historyFactory =
                    () -> new HistoryByInstant<>(clock);

            List<Runnable> shutdownTasks = new ArrayList<>();

            ScheduledExecutorService scheduler = Objects.requireNonNullElseGet(
                    this.scheduler, () -> {
                        ScheduledExecutorService scheduler_ = Executors.newScheduledThreadPool(1);
                        shutdownTasks.add(scheduler_::shutdown);
                        return scheduler_;
                    });

            MatchableContinuousComparer continuousComparer =
                    MatchableContinuousComparer.with()
                            .resultsHandler(resultsHandler)
                            .comparerProvider(comparerProvider)
                            .createFor(strategy,
                                    historyFactory,
                                    metaData,
                                    checkBack -> scheduler.schedule(
                                            checkBack, tolerance.toMillis(),
                                            TimeUnit.MILLISECONDS));

            return new TimedMatchableComparer(continuousComparer,
                    scheduler, shutdownTasks);
        }
    }

    public static Settings with() {
        return new Settings();
    }


    public void acceptX(Matchable x) {

        scheduler.execute(() -> continuousComparer.acceptX(x));
    }

    public void acceptY(Matchable y) {

        scheduler.execute(() -> continuousComparer.acceptY(y));
    }

    @Override
    public void close() {
        shutdownTasks.forEach(Runnable::run);
    }
}
