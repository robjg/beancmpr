package org.oddjob.beancmpr.continuous;

import java.util.List;

/**
 * Provides a history of items to a {@link SourceStrategy}
 *
 * @param <V> The type of item.
 */
public interface SourceHistory<V> {

    Entry<V> store(V item);

    List<Entry<V>> allOldestFirst();

    /**
     * Provide all history items. Most recent first.
     *
     * @return A list. Maybe be empty, never null.
     */
    List<Entry<V>> allRecentFirst();

    V remove(Entry<V> entry);

    /**
     * Is the entry within some tolerance. Allows a time limit to be
     * applied to differences.
     *
     * @param entry An entry that must have come from this history.
     * @return true if the entry can be considered not yet eligible to be different.
     */
    boolean isWithinTolerance(Entry<V> entry);

    interface Entry<V>
    {
        V getItem();
    }
}
