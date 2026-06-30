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

    List<V> removeExpired();

    V remove(Entry<V> entry);

    void removeIfOld(Entry<V> entry);

    interface Entry<V>
    {
        V getItem();
    }
}
