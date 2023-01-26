package org.oddjob.beancmpr.results;

import org.oddjob.beancmpr.matchables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class MatchResultHandlerFactory implements CompareResultsHandlerFactory {

    private final List<MatchResult> results = new ArrayList<>();

    public static CompareResultsHandler handlerTo(Consumer<? super MatchResult> resultsConsumer) {
        return new Handler(resultsConsumer);
    }

    public CompareResultsHandler createMatchResultsHandlerTo(Consumer<? super MatchResult> resultsConsumer) {
        if (resultsConsumer == null) {
            return new Handler(results::add);
        }
        else {
            return new Handler(resultsConsumer);
        }
    }

    @Override
    public CompareResultsHandler createResultsHandlerTo(Consumer<Object> resultsConsumer) {
        return createMatchResultsHandlerTo(resultsConsumer);
    }

    static class Handler implements CompareResultsHandler {

        private final Consumer<? super MatchResult> to;

        Handler(Consumer<? super MatchResult> to) {
            this.to = Objects.requireNonNull(to, "No Destination");
        }

        @Override
        public void compared(MultiValueComparison<Matchable> comparison) {

            to.accept(MatchResult.fromComparison(comparison));
        }

        @Override
        public void xMissing(MatchableGroup ys) {

            to.accept(MatchResult.fromMissingX(ys));
        }

        @Override
        public void yMissing(MatchableGroup xs) {
            to.accept(MatchResult.fromMissingY(xs));
        }

    }

    public List<MatchResult> getResults() {
        return results;
    }
}
