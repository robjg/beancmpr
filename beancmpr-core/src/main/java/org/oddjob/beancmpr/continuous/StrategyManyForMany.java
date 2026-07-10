package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

import java.util.List;
import java.util.Optional;

/**
 * We match many of our their items for many of ours.
 *
 * <pre>
 * | x      | y      | result           |
 * | yellow |        |                  |
 * | red    |        |                  |
 * |        | yellow | yellow == yellow |
 * |        | red    | red == red       |
 * |        | red    |                  |
 * | green  |        |                  |
 * | yellow |        |                  |
 * |        | green  | green == green   |
 * |        | green  | green == green   |
 * | green  |        | green == green   |
 * </pre>
 *
 * @param <V> The type of value being matched.
 */
public class StrategyManyForMany<V> implements SourceStrategy<V> {

    private final Comparer<? super V> comparer;

    private V lastMatchedX;
    private V lastMatchedY;

    public StrategyManyForMany(Comparer<? super V> comparer) {
        this.comparer = comparer;
    }

    @Override
    public Optional<Runnable> onX(V x,
                                  SourceHistory<V> xHistory,
                                  SourceHistory<V> yHistory,
                                  Results<V> results) {

        if (lastMatchedY != null) {

            Comparison<? super V> comparison = comparer.compare(x, lastMatchedY);
            if (comparison.getResult() == 0) {
                results.comparison(comparison);
                lastMatchedX = x;
                return Optional.empty();
            }
        }

        return onItem(x, xHistory, yHistory, results, true);
    }

    @Override
    public Optional<Runnable> onY(V y,
                                  SourceHistory<V> yHistory,
                                  SourceHistory<V> xHistory,
                                  Results<V> results) {

        if (lastMatchedX != null) {

            Comparison<? super V> comparison = comparer.compare(lastMatchedX, y);
            if (comparison.getResult() == 0) {
                results.comparison(comparison);
                lastMatchedY = y;
                return Optional.empty();
            }
        }

        return onItem(y, yHistory, xHistory, results, false);
    }

    public Optional<Runnable> onItem(V item,
                                     SourceHistory<V> itemHistory,
                                     SourceHistory<V> otherHistory,
                                     Results<V> results,
                                     boolean isX) {

        List<SourceHistory.Entry<V>> theirEntries = otherHistory.allOldestFirst();

        boolean matched = false;
        boolean something = false;
        for (SourceHistory.Entry<V> otherEntry : theirEntries) {

            V otherItem = otherEntry.getItem();
            Comparison<? super V> comparison = isX ?
                    comparer.compare(item, otherItem) :
                    comparer.compare(otherItem, item);
            if (comparison.getResult() == 0) {
                matched = true;
                if (isX) {
                    lastMatchedX = item;
                    lastMatchedY = otherItem;
                } else {
                    lastMatchedY = item;
                    lastMatchedX = otherItem;
                }
            } else {
                // if the other item isn't eligible for being different yet
                // then break out
                if (otherHistory.isWithinTolerance(otherEntry)) {
                    break;
                }
                // If we've had a match we'll let the other items
                // check back report this as missing unless something else turns up later.
                if (matched) {
                    break;
                }

                lastMatchedX = null;
                lastMatchedY = null;
            }
            otherHistory.remove(otherEntry);
            results.comparison(comparison);
            something = true;
        }

        if (something) {
            return Optional.empty();
        } else {
            SourceHistory.Entry<V> entry = itemHistory.store(item);
            return Optional.of(() -> {
                V expired = itemHistory.remove(entry);

                if (expired != null) {
                    results.theirMissing(expired);
                }
            });
        }
    }
}
