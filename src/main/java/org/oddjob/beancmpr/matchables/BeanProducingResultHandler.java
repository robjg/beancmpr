package org.oddjob.beancmpr.matchables;

import java.util.Collection;

public interface BeanProducingResultHandler extends BeanCmprResultsHandler {

	public void setOut(Collection<? super Object> destination);
}
