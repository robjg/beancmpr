package org.oddjob.beancmpr.continuous;

/**
 * Something that accepts an X and a Y being pushed at it, and can
 * be closed.
 *
 * @param <T> The type of thing being pushed.
 */
public interface CloseableDuelConsumer<T> extends DuelConsumer<T>, AutoCloseable {

    @Override
    void close();
}
