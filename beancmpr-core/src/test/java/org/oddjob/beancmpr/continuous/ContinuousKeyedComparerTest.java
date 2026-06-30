package org.oddjob.beancmpr.continuous;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparer;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class ContinuousKeyedComparerTest {

    static class ResultList<K extends Comparable<K>, V>
            implements ContinuousKeyedResults<K, V> {

        public final List<String> results = new ArrayList<>();

        @Override
        public void xMissing(K key, V value) {
            results.add("xMissing: " + key + '=' + value);
        }

        @Override
        public void yMissing(K key, V value) {
            results.add("yMissing: " + key + '=' + value);
        }

        @Override
        public void comparison(K key, Comparison<? super V> comparison) {
            if (comparison.getResult() == 0) {
                results.add("Matched: " + key);
            }
            else {
                results.add("Different: " + key + ", " + comparison.getSummaryText());
            }
        }
    }

    @Test
    void oneForOneComparison() {

        ResultList<String, Integer> results = new ResultList<>();

        List<Runnable> checkBack = new ArrayList<>();

        ContinuousKeyedComparer<String, Integer> comparer =
                new ContinuousKeyedComparer<>(
                results, new StrategyOneForOne<>(new NumericComparer()),
                HistoryBySequence::new,
                checkBack::add);

        comparer.acceptX("Apple", 5);

        assertThat(results.results, empty());

        comparer.acceptX("Apple", 6);

        assertThat(results.results, empty());

        comparer.acceptY("Apple", 5);

        assertThat(results.results, contains("Matched: Apple"));

        assertThat(checkBack.size(), is(2));

        checkBack.forEach(Runnable::run);

        assertThat(results.results, contains("Matched: Apple", "yMissing: Apple=6"));
    }
}