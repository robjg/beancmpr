package org.oddjob.beancmpr.matchables;

import java.util.function.Consumer;

/**
 * Creates a {@link CompareResultsHandler} that may be linked to a Consumer of the results. This
 * is to allow {@link org.oddjob.beancmpr.BeanCompareJob} to play nicely with Oddjob's Bean Bus.
 */
public interface CompareResultsHandlerFactory {

    /**
     * Create the Result Handler.
     * @param resultsConsumer The onward destination if there is one.
     *
     * @return The Result Handler. Never null.
     */
    CompareResultsHandler createResultsHandlerTo(Consumer<Object> resultsConsumer);
}
