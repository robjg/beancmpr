package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.Comparison;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of comparing to {@link Matchable}s.
 *
 * @author rob
 */
public class MatchableComparison
        implements MultiValueComparison<Matchable> {

    private final ImmutableCollection<Comparison<?>> comparisons;

    private final int result;

    private final int equal;

    private final Matchable x;

    private final Matchable y;

    private MatchableComparison(Accumulator accumulator) {
        this.x = accumulator.x;
        this.y = accumulator.y;
        this.comparisons = ImmutableCollection.of(accumulator.comparisons);
        this.result = accumulator.result;
        this.equal = accumulator.equal;
    }

    public static class Accumulator {

        private final Matchable x;

        private final Matchable y;

        private int equal;

        private int result;

        private final List<Comparison<?>> comparisons = new ArrayList<>();

        Accumulator(Matchable x, Matchable y) {
            this.x = x;
            this.y = y;
        }

        public Accumulator add(Comparison<?> comparison) {

            this.comparisons.add(comparison);

            int comparisonResult = comparison.getResult();
            if (comparisonResult == 0) {
                equal++;
            } else if (result == 0) {
                result = comparisonResult;
            }

            return this;
        }

        public MultiValueComparison<Matchable> complete() {

            return new MatchableComparison(this);
        }
    }

    public static Accumulator accumulatorFor(Matchable x, Matchable y) {

        return new Accumulator(x, y);
    }

    @Override
    public Matchable getX() {
        return x;
    }

    @Override
    public Matchable getY() {
        return y;
    }

    @Override
    public int getResult() {
        return result;
    }

    @Override
    public Iterable<Comparison<?>> getValueComparisons() {
        return comparisons;
    }

    @Override
    public String getSummaryText() {
        return "" + equal + "/" + comparisons.size() + " values equal";
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getSummaryText();
    }
}
