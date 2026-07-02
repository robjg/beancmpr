package org.oddjob.beancmpr.continuous;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

import java.util.List;
import java.util.Optional;

/**
 * We expect exactly one of their items for each of ours.
 * <pre>
 * | ours   | theirs | result           |
 * | yellow |        |                  |
 * | red    |        |                  |
 * |        | yellow | yellow == yellow |
 * |        | red    | red == red       |
 * |        | red    |                  |
 * | green  |        | green <> red     |
 * | yellow |        | yellow <> red    |
 * |        | green  | yellow <> green  |
 * |        | green  |                  |
 * | green  |        | green == green   |
 * </pre>
 *
 * @param <V> The type of value being matched.
 */
public class StrategyOneForOne<V> implements SourceStrategy<V> {

    private final Comparer<? super V> comparer;

    public StrategyOneForOne(Comparer<? super V> comparer) {
        this.comparer = comparer;
    }

    @Override
    public Optional<Runnable> onX(V x,
                                  SourceHistory<V> xHistory,
                                  SourceHistory<V> yHistory,
                                  Results<V> results) {
        return onItem(x, xHistory, yHistory, results, true);
    }

    @Override
    public Optional<Runnable> onY(V y,
                                  SourceHistory<V> yHistory,
                                  SourceHistory<V> xHistory,
                                  Results<V> results) {
        return onItem(y, yHistory, xHistory, results, false);
    }

    public Optional<Runnable> onItem(V item,
                                     SourceHistory<V> itemHistory,
                                     SourceHistory<V> otherHistory,
                                     Results<V> results,
                                     boolean isX) {

        List<SourceHistory.Entry<V>> theirEntries = otherHistory.allOldestFirst();

        if (theirEntries.isEmpty()) {
            SourceHistory.Entry<V> entry = itemHistory.store(item);
            return Optional.of(() -> {
                V expired = itemHistory.remove(entry);

                if (expired != null) {
                    results.theirMissing(expired);
                }
            });
        } else {
            for (SourceHistory.Entry<V> theirEntry : theirEntries) {

                V theirItem = otherHistory.remove(theirEntry);
                Comparison<? super V> comparison = isX ?
                        comparer.compare(item, theirItem) :
                        comparer.compare(theirItem, item);
                results.comparison(comparison);
                if (comparison.getResult() == 0) {
                    break;
                }
            }
            return Optional.empty();
        }
    }
}
