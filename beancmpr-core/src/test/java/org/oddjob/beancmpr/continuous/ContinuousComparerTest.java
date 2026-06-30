package org.oddjob.beancmpr.continuous;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ContinuousComparerTest {

    static class ResultList<V> implements ContinuousResults<V> {

        public final List<String> results = new ArrayList<>();

        @Override
        public void xMissing(V value) {
            results.add("xMissing: " + value);
        }

        @Override
        public void yMissing(V value) {
            results.add("yMissing: " + value);
        }

        @Override
        public void comparison(Comparison<? super V> comparison) {
            if (comparison.getResult() == 0) {
                results.add("Matched");
            }
            else {
                results.add("Different: " + comparison.getSummaryText());
            }
        }
    }

    @Test
    void oneForOneComparison() {

        ResultList<Integer> results = new ResultList<>();

        List<Runnable> checkBack = new ArrayList<>();

        ContinuousComparer<Integer> comparer = new ContinuousComparer<>(
                results, new StrategyOneForOne<>(new NumericComparer()),
                HistoryBySequence::new,
                checkBack::add);

        comparer.acceptX(5);

        assertThat(results.results, empty());

        comparer.acceptX(6);

        assertThat(results.results, empty());

        comparer.acceptY(5);

        assertThat(results.results, contains("Matched"));

        assertThat(checkBack.size(), is(2));

        checkBack.forEach(Runnable::run);

        assertThat(results.results, contains("Matched", "yMissing: 6"));
    }
}