package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;
import org.oddjob.beancmpr.matchables.*;

import java.beans.ExceptionListener;
import java.time.Duration;
import java.time.Instant;
import java.time.InstantSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class TimedMatchableComparer implements CloseableDuelConsumer<Matchable> {

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

        private SourceStrategyFactory<Matchable> sourceStrategy;

        private Duration tolerance;

        private ScheduledExecutorService scheduler;

        private ExceptionListener exceptionListener;

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

        public Settings sourceStrategy(SourceStrategyFactory<Matchable> sourceStrategy) {
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

        public Settings exceptionListener(ExceptionListener exceptionListener) {
            this.exceptionListener = exceptionListener;
            return this;
        }

        public TimedMatchableComparer createFor(MatchableMetaData metaData) {

            InstantSource clock = Objects.requireNonNullElseGet(this.clock,
                    () -> Instant::now);

            BeanPropertyComparerProvider comparerProvider = Objects.requireNonNullElseGet(
                    this.comparerProvider, ComparersByNameOrType::new);

            MatchableComparer matchableComparer = new MatchableComparerFactory(
                    comparerProvider).createComparerFor(metaData, metaData);

            SourceStrategy<Matchable> strategy = Objects.requireNonNullElse(this.sourceStrategy,
                            SourceStrategies.Strategy.ONE_FOR_ONE).createStrategy(matchableComparer);

            Supplier<SourceHistory<Matchable>> historyFactory =
                    () -> new HistoryByInstant<>(clock);

            List<Runnable> shutdownTasks = new ArrayList<>();

            ScheduledExecutorService scheduler = Objects.requireNonNullElseGet(
                    this.scheduler, () -> {
                        ScheduledExecutorService scheduler_ = Executors.newScheduledThreadPool(1,
                                new DefaultThreadFactory(exceptionListener));
                        shutdownTasks.add(scheduler_::shutdown);
                        return scheduler_;
                    });

            long toleranceMillis = tolerance == null ? 1000L : tolerance.toMillis();

            MatchableContinuousComparer continuousComparer =
                    MatchableContinuousComparer.with()
                            .resultsHandler(resultsHandler)
                            .comparerProvider(comparerProvider)
                            .createFor(strategy,
                                    historyFactory,
                                    metaData,
                                    checkBack -> scheduler.schedule(
                                            checkBack, toleranceMillis,
                                            TimeUnit.MILLISECONDS));

            return new TimedMatchableComparer(continuousComparer,
                    scheduler, shutdownTasks);
        }
    }

    public static Settings with() {
        return new Settings();
    }

    @Override
    public void acceptX(Matchable x) {

        scheduler.execute(() -> {
            try {
                continuousComparer.acceptX(x);
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
            }
        });
    }

    @Override
    public void acceptY(Matchable y) {

        scheduler.execute(() -> {
            try {
                continuousComparer.acceptY(y);
            } catch (Exception e) {
                Thread.currentThread().getUncaughtExceptionHandler()
                        .uncaughtException(Thread.currentThread(), e);
            }
        });
    }

    @Override
    public void close() {
        shutdownTasks.forEach(Runnable::run);
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final ExceptionListener exceptionListener;

        DefaultThreadFactory(ExceptionListener exceptionListener) {
            this.exceptionListener = exceptionListener;
            this.namePrefix = TimedMatchableComparer.class.getSimpleName() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(null, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            if (exceptionListener != null) {
                t.setUncaughtExceptionHandler(
                        (th, e) -> {
                            if (e instanceof Exception ex) {
                                exceptionListener.exceptionThrown(ex);
                            }
                            else {
                                exceptionListener.exceptionThrown(new Exception(e));
                            }
                        });
            }
            return t;
        }
    }

}
