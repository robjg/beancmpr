package org.oddjob.beancmpr.continuous;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.matchables.FluentMatchable;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.beancmpr.results.MatchResultHandlerFactory;
import org.oddjob.beancmpr.results.MatchResultType;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TimedMatchableComparerTest {

    @Test
    void thinking() throws InterruptedException {

        Matchable x1 = FluentMatchable
                .withKeys("A").andValues(2).make();

        Matchable y1 = FluentMatchable
                .withKeys("A").andValues(3).make();

        BlockingQueue<MatchResult> results = new LinkedBlockingQueue<>();

        AtomicReference<Instant> instance = new  AtomicReference<>(
                Instant.parse("2026-06-21T06:00:00Z"));

        try (TimedMatchableComparer comparer =
                TimedMatchableComparer.with()
                        .resultsHandler(MatchResultHandlerFactory.handlerTo(results::add))
                        .clock(instance::get)
                        .createFor(x1.getMetaData())) {

            comparer.acceptX(x1);
            comparer.acceptY(y1);

            MatchResult result1 = results.poll(5, TimeUnit.SECONDS);

            assertThat(result1, Matchers.notNullValue());
            assertThat(result1.getResultType(), is(MatchResultType.NOT_EQUAL));
            assertThat(result1.getComparison(0).getResult(), lessThan(0));
        }
    }
}