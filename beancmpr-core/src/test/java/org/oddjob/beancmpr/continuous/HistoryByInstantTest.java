package org.oddjob.beancmpr.continuous;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.continuous.SourceHistory.Entry;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

class HistoryByInstantTest {

    @Test
    void addListRemove() {

        AtomicReference<Instant> clock =  new AtomicReference<>(
                Instant.parse("2026-06-30T19:30:00Z"));

        HistoryByInstant<String> test = new HistoryByInstant<>(clock::get);

        Entry<String> e1 = test.store("Apple");

        clock.set(Instant.parse("2026-06-30T19:30:30Z"));

        Entry<String> e2 = test.store("Orange");

        List<Entry<String>> entries = test.allOldestFirst();

        assertThat(entries, contains(e1, e2));

        assertThat(test.remove(e2), is("Orange"));
    }
}