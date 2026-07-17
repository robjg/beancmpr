package org.oddjob.beancmpr.continuous;

/**
 * Something that accepts an X and a Y being pushed at it.
 *
 * @param <T> The type of thing being pushed.
 */
public interface DuelConsumer<T> {

    void acceptX(T x);

    void acceptY(T y);
}
