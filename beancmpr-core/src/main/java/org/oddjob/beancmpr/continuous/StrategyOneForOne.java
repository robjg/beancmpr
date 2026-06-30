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
    public Optional<Runnable> onOurItem(V ourItem,
                                        SourceHistory<V> ourHistory,
                                        SourceHistory<V> theirHistory,
                                        Results<V> results) {

        List<SourceHistory.Entry<V>> theirEntries = theirHistory.allOldestFirst();

        if (theirEntries.isEmpty()) {
            SourceHistory.Entry<V> entry = ourHistory.store(ourItem);
            return Optional.of(() -> {
                V expired = ourHistory.remove(entry);

                if (expired != null) {
                    results.theirMissing(expired);
                }
            });
        } else {
            for (SourceHistory.Entry<V> theirEntry : theirEntries) {

                V theirItem = theirHistory.remove(theirEntry);
                Comparison<? super V> comparison = comparer.compare(ourItem, theirItem);
                results.comparison(comparison);
                if (comparison.getResult() == 0) {
                    break;
                }
            }
            return Optional.empty();
        }
    }
}
