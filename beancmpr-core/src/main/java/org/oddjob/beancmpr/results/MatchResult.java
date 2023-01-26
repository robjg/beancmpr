package org.oddjob.beancmpr.results;

import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

/**
 * A Match Result
 */
public class MatchResult {

    private MatchResultType resultType;

    private Object[] keys;

    private Comparison<?>[] comparisons;

    public MatchResultType getResultType() {
        return resultType;
    }

    public Object[] getKeys() {
        return keys;
    }

    public Object getKey(int index) {
        return keys[index];
    }

    public Comparison<?>[] getComparisons() {
        return comparisons;
    }

    public Comparison<?> getComparison(int index) {
        return comparisons[index];
    }

    public static MatchResult fromComparison(MultiValueComparison<Matchable> comparison) {
        MatchResult row = new MatchResult();

        if (comparison.getResult() == 0) {
            row.resultType = MatchResultType.EQUAL;
        }
        else {
            row.resultType = MatchResultType.NOT_EQUAL;
        }
        row.keys = Iterables.toArray(comparison.getX().getKeys());

        row.comparisons = Iterables.toArray(
                comparison.getValueComparisons(), Comparison.class);

        return row;
    }

    public static MatchResult fromMissingX(MatchableGroup ys) {
        MatchResult row = new MatchResult();

        row.resultType = MatchResultType.X_MISSING;

        row.keys = Iterables.toArray(ys.getKeys());

        row.comparisons = new Comparison[0];

        return row;
    }

    public static MatchResult fromMissingY(MatchableGroup xs) {
        MatchResult row = new MatchResult();

        row.resultType = MatchResultType.Y_MISSING;

        row.keys = Iterables.toArray(xs.getKeys());

        row.comparisons = new Comparison[0];

        return row;
    }

}
