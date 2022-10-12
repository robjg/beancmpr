package org.oddjob.beancmpr.matchables;

import java.util.function.Consumer;

public interface CompareResultsHandlerFactory {

    CompareResultsHandler createResultsHandlerTo(Consumer<Object> resultsConsumer);
}
