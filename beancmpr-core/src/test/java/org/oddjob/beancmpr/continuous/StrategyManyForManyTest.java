package org.oddjob.beancmpr.continuous;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class StrategyManyForManyTest {

    static class OurResults implements SourceStrategy.Results<Double> {

        List<String> results = new ArrayList<>();

        @Override
        public void comparison(Comparison<? super Double> comparison) {
            String text = comparison.getX() +
                    (comparison.getResult() == 0 ?
                            "==" : "<>")
                    + comparison.getY();
            results.add(text);
        }

        @Override
        public void theirMissing(Double theirItem) {
            results.add("Missing: " + theirItem.toString());
        }
    }

    final SourceHistory<Double> xHistory = HistoryBySequence.of();
    final SourceHistory<Double> yHistory = HistoryBySequence.of();

    final NumericComparer comparer = new NumericComparer();

    final StrategyManyForMany<Double> test = new StrategyManyForMany<>(comparer);

    final OurResults results = new OurResults();

    @BeforeEach
    void setUp() {

        comparer.setDeltaTolerance(0.5);
        comparer.setDeltaFormat("0.0");
        comparer.setPercentageFormat(NumericComparer.NONE);
    }

    @Test
    void simpleMatch() {

        Optional<Runnable> checkBack1 = test.onX(42.0, xHistory, yHistory, results);
        assertThat(checkBack1.isPresent(), is(true));

        assertThat(results.results, Matchers.empty());

        Optional<Runnable> checkBack2 = test.onY(42.1, yHistory, xHistory, results);
        assertThat(checkBack2.isPresent(), is(false));

        Optional<Runnable> checkBack3 = test.onY(42.2, yHistory, xHistory, results);
        assertThat(checkBack3.isPresent(), is(false));

        checkBack1.get().run();

        assertThat(results.results, Matchers.contains("42.0==42.1", "42.0==42.2"));
    }

    @Test
    void matchMany() {

        Optional<Runnable> checkBack1 = test.onX(42.0, xHistory, yHistory, results);
        assertThat(checkBack1.isPresent(), is(true));

        Optional<Runnable> checkBack2 = test.onX(42.1, xHistory, yHistory, results);
        assertThat(checkBack2.isPresent(), is(true));

        Optional<Runnable> checkBack3 = test.onX(17.1, xHistory, yHistory, results);
        assertThat(checkBack3.isPresent(), is(true));

        assertThat(results.results, Matchers.empty());

        Optional<Runnable> checkBack4 = test.onY(42.2, yHistory, xHistory, results);
        assertThat(checkBack4.isPresent(), is(false));

        checkBack1.get().run();
        checkBack2.get().run();

        assertThat(results.results, Matchers.contains("42.0==42.2", "42.1==42.2"));
        results.results.clear();

        checkBack3.get().run();

        assertThat(results.results, Matchers.contains("Missing: 17.1"));
    }

    @Test
    void matchNone() {

        Optional<Runnable> checkBack1 = test.onX(42.0, xHistory, yHistory, results);
        assertThat(checkBack1.isPresent(), is(true));

        Optional<Runnable> checkBack2 = test.onX(42.1, xHistory, yHistory, results);
        assertThat(checkBack2.isPresent(), is(true));

        assertThat(results.results, Matchers.empty());

        Optional<Runnable> checkBack3 = test.onY(17.2, yHistory, xHistory, results);
        assertThat(checkBack3.isPresent(), is(false));

        checkBack1.get().run();
        checkBack2.get().run();

        assertThat(results.results, Matchers.contains("42.0<>17.2", "42.1<>17.2"));
    }
}