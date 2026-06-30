package org.oddjob.beancmpr.continuous;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class HistoryBySequenceTest {

    @Test
    void simpleSequence() {

        HistoryBySequence<Integer> history = HistoryBySequence.of(1, 2, 3);

        assertThat(history.allOldestFirst()
                .stream()
                .map(SourceHistory.Entry::getItem)
                .toList(), contains(1, 2, 3));

        assertThat(history.allRecentFirst().stream()
                .map(SourceHistory.Entry::getItem)
                .toList(), contains(3, 2, 1));
    }
}