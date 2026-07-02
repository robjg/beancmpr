package org.oddjob.beancmpr.continuous;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;
import org.oddjob.beancmpr.matchables.FluentMatchable;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparerFactory;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.beancmpr.results.MatchResultHandlerFactory;
import org.oddjob.beancmpr.results.MatchResultType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MatchableContinuousComparerTest {

    @Test
    void thinking() {

        Matchable x1 = FluentMatchable
                .withKeys("A").andValues(2).make();

        Matchable y1 = FluentMatchable
                .withKeys("A").andValues(3).make();

        List<MatchResult> results = new ArrayList<>();

        MatchableComparerFactory comparerFactory = new MatchableComparerFactory(
                new ComparersByNameOrType());

        SourceStrategy<Matchable> sourceStrategy =
                new StrategyOneForOne<>(comparerFactory
                        .createComparerFor(x1.getMetaData(), y1.getMetaData()));

        List<Runnable> checkBacks = new ArrayList<>();

        MatchableContinuousComparer comparer =
                MatchableContinuousComparer.with()
                        .resultsHandler(MatchResultHandlerFactory.handlerTo(results::add))
                        .createFor(sourceStrategy,
                                HistoryBySequence::new,
                                x1.getMetaData(),
                                checkBacks::add);

        comparer.acceptX(x1);
        comparer.acceptY(y1);

        assertThat(results.size(), is(1));

        MatchResult result1 = results.getFirst();
        assertThat(result1.getResultType(), is(MatchResultType.NOT_EQUAL));
        // The comparison isn't correct
        assertThat(result1.getComparison(0).getResult(), greaterThan(0));

        assertThat(checkBacks.size(), is(1));
        checkBacks.getFirst().run();

        assertThat(results.size(), is(1));
    }
}