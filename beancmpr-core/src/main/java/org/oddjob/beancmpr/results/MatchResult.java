package org.oddjob.beancmpr.results;

import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.*;

/**
 * A Match Result
 */
abstract public class MatchResult {

    abstract public MatchResultType getResultType();

    abstract public MatchableMetaData getMetaData();

    abstract public Object getKey(int index);

    abstract public Object getXValue(int index);

    abstract public Object getYValue(int index);

    abstract public Object getXOther(int index);

    abstract public Object getYOther(int index);

    abstract public Comparison<?> getComparison(int index);

    static class ComparisonResult extends MatchResult {

        private final MultiValueComparison<Matchable> comparison;

        ComparisonResult(MultiValueComparison<Matchable> comparison) {
            this.comparison = comparison;
        }

        @Override
        public MatchResultType getResultType() {
            return comparison.getResult() == 0 ? MatchResultType.EQUAL : MatchResultType.NOT_EQUAL;
        }

        @Override
        public MatchableMetaData getMetaData() {
            return comparison.getX().getMetaData();
        }

        @Override
        public Object getKey(int index) {
            return comparison.getX().getKeys().get(index);
        }

        @Override
        public Object getXValue(int index) {
            return comparison.getX().getValues().get(index);
        }

        @Override
        public Object getYValue(int index) {
            return comparison.getY().getValues().get(index);
        }

        @Override
        public Object getXOther(int index) {
            return comparison.getX().getOthers().get(index);
        }

        @Override
        public Object getYOther(int index) {
            return comparison.getY().getOthers().get(index);
        }

        @Override
        public Comparison<?> getComparison(int index) {
            return comparison.getValueComparisons().get(index);
        }
    }

    public static MatchResult fromComparison(MultiValueComparison<Matchable> comparison) {
        return new ComparisonResult(comparison);
    }

    static class MissingX extends MatchResult {

        private final Matchable y;

        MissingX(Matchable y) {
            this.y = y;
        }

        @Override
        public MatchResultType getResultType() {
            return MatchResultType.X_MISSING;
        }

        @Override
        public MatchableMetaData getMetaData() {
            return y.getMetaData();
        }

        @Override
        public Object getKey(int index) {
            return y.getKeys().get(index);
        }

        @Override
        public Object getXValue(int index) {
            return null;
        }

        @Override
        public Object getYValue(int index) {
            return y.getValues().get(index);
        }

        @Override
        public Object getXOther(int index) {
            return null;
        }

        @Override
        public Object getYOther(int index) {
            return y.getOthers().get(index);
        }

        @Override
        public Comparison<?> getComparison(int index) {
            return null;
        }
    }

    public static ImmutableCollection<MatchResult> fromMissingX(MatchableGroup ys) {

        return ys.getGroup().stream()
                .map(MissingX::new)
                .collect(ImmutableCollection.collector());
    }

    static class MissingY extends MatchResult {

        private final Matchable x;

        MissingY(Matchable x) {
            this.x = x;
        }

        @Override
        public MatchResultType getResultType() {
            return MatchResultType.Y_MISSING;
        }

        @Override
        public MatchableMetaData getMetaData() {
            return x.getMetaData();
        }

        @Override
        public Object getKey(int index) {
            return x.getKeys().get(index);
        }

        @Override
        public Object getXValue(int index) {
            return x.getValues().get(index);
        }

        @Override
        public Object getYValue(int index) {
            return null;
        }

        @Override
        public Object getXOther(int index) {
            return x.getOthers().get(index);
        }

        @Override
        public Object getYOther(int index) {
            return null;
        }

        @Override
        public Comparison<?> getComparison(int index) {
            return null;
        }
    }

    public static ImmutableCollection<MatchResult> fromMissingY(MatchableGroup xs) {

        return xs.getGroup().stream()
                .map(MissingY::new)
                .collect(ImmutableCollection.collector());
    }

}
