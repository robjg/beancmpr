package org.oddjob.beancmpr.continuous;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.NumericComparer;
import org.oddjob.beancmpr.comparers.NumericComparison;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.*;

class StrategyOneForOneTest {

    @Test
    void simpleMatch() {

        SourceHistory<Integer> theirHistory = HistoryBySequence.of(42);
        SourceHistory<Integer> ourHistory = HistoryBySequence.of();

        StrategyOneForOne<Integer> test = new StrategyOneForOne<>(new NumericComparer());

        @SuppressWarnings("unchecked")
        SourceStrategy.Results<Integer> results = mock(SourceStrategy.Results.class);

        Optional<Runnable> checkBack = test.onX(42, ourHistory, theirHistory, results);
        assertThat(checkBack.isPresent(), is(false));

        ArgumentCaptor<NumericComparison> comparisonCaptor = ArgumentCaptor.forClass(NumericComparison.class);

        verify(results).comparison(comparisonCaptor.capture());

        Comparison<? super Integer> comparison =  comparisonCaptor.getValue();
        assertThat(comparison.getResult(), is(0));
    }

    @Test
    void theirMissing() {

        SourceHistory<Integer> theirHistory = HistoryBySequence.of();
        SourceHistory<Integer> ourHistory = HistoryBySequence.of(42);

        StrategyOneForOne<Integer> test = new StrategyOneForOne<>(new NumericComparer());

        @SuppressWarnings("unchecked")
        SourceStrategy.Results<Integer> results = mock(SourceStrategy.Results.class);

        Optional<Runnable> checkBack = test.onX(42, ourHistory, theirHistory, results);

        checkBack.orElseThrow().run();

        verify(results).theirMissing(42);
    }

    @Test
    void multipleTheirs() {

        SourceHistory<Integer> theirHistory = HistoryBySequence.of(42, 43);
        SourceHistory<Integer> ourHistory = HistoryBySequence.of();

        StrategyOneForOne<Integer> test = new StrategyOneForOne<>(new NumericComparer());

        @SuppressWarnings("unchecked")
        SourceStrategy.Results<Integer> results = mock(SourceStrategy.Results.class);
        test.onX(42, ourHistory, theirHistory, results);

        ArgumentCaptor<NumericComparison> comparisonCaptor = ArgumentCaptor.forClass(NumericComparison.class);

        verify(results).comparison(comparisonCaptor.capture());

        assertThat(comparisonCaptor.getValue().getResult(), is(0));

        test.onX(42, ourHistory, theirHistory, results);

        verify(results, times(2)).comparison(comparisonCaptor.capture());

        assertThat(comparisonCaptor.getValue().getResult(), lessThan(0));
    }

}