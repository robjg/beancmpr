package org.oddjob.beancmpr.results;

import org.oddjob.beancmpr.matchables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Create {@link MatchResult} objects from Comparison Results.
 */
public class MatchResultHandlerFactory implements CompareResultsHandlerFactory {

    private final List<MatchResult> results = new ArrayList<>();

    private boolean ignoreMatches;

    public static CompareResultsHandler handlerTo(Consumer<? super MatchResult> resultsConsumer) {
        return new Handler(resultsConsumer, false);
    }

    public CompareResultsHandler createMatchResultsHandlerTo(Consumer<? super MatchResult> resultsConsumer) {
        if (resultsConsumer == null) {
            return new Handler(results::add, ignoreMatches);
        }
        else {
            return new Handler(resultsConsumer, ignoreMatches);
        }
    }

    @Override
    public CompareResultsHandler createResultsHandlerTo(Consumer<Object> resultsConsumer) {
        return createMatchResultsHandlerTo(resultsConsumer);
    }

    static class Handler implements CompareResultsHandler {

        private final Consumer<? super MatchResult> to;

        private final boolean ignoreMatches;

        Handler(Consumer<? super MatchResult> to, boolean ignoreMatches) {
            this.to = Objects.requireNonNull(to, "No Destination");
            this.ignoreMatches = ignoreMatches;
        }

        @Override
        public void compared(MultiValueComparison<Matchable> comparison) {

            if (ignoreMatches && comparison.getResult() == 0) {
                return;
            }
            to.accept(MatchResult.fromComparison(comparison));
        }

        @Override
        public void xMissing(MatchableGroup ys) {

            MatchResult.fromMissingX(ys).forEach(to);
        }

        @Override
        public void yMissing(MatchableGroup xs) {
            MatchResult.fromMissingY(xs).forEach(to);
        }

        @Override
        public String toString() {
            return "MatchResultHandler{" +
                    "to=" + to +
                    ", ignoreMatches=" + ignoreMatches +
                    '}';
        }
    }

    public boolean isIgnoreMatches() {
        return ignoreMatches;
    }

    public void setIgnoreMatches(boolean ignoreMatches) {
        this.ignoreMatches = ignoreMatches;
    }

    public List<MatchResult> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "MatchResultHandlerFactory{" +
                ", ignoreMatches=" + ignoreMatches +
                '}';
    }
}
