package org.oddjob.beancmpr.results;

import java.util.function.Consumer;

public interface PlaysWithBeanbus {

    void setOut(Consumer<? super Object> destination);
}
