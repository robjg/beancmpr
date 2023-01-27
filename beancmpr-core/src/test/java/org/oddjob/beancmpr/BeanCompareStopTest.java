package org.oddjob.beancmpr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.*;
import org.oddjob.images.IconHelper;
import org.oddjob.state.JobState;
import org.oddjob.state.ParentState;
import org.oddjob.state.StateEvent;
import org.oddjob.tools.IconSteps;
import org.oddjob.tools.StateSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;

class BeanCompareStopTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(BeanCompareStopTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
	}
	
	public static class Fruit {
		
		public String getType() {
			return "apple";
		}
		
		public String getColour() {
			return "red";
		}
	}
	
	public static class SlowIterable implements Iterable<Fruit> {
		
		@Override
		public Iterator<Fruit> iterator() {
			
			return new Iterator<>() {

				@Override
				public boolean hasNext() {
					return true;
				}

				@Override
				public Fruit next() {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					return new Fruit();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}
		
	}

	@Test
	void testStopBeanCompareJobInOddjob() throws InterruptedException, FailedToStopException {
		
		File config = new File(Objects.requireNonNull(getClass().getResource(
				"BeanCompareStopTest.xml")).getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(config);
		
		oddjob.load();
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		Object test = lookup.lookup("compare-job");
		
		StateSteps states = new StateSteps((Stateful) test);
		states.startCheck(JobState.READY, JobState.EXECUTING);
		
		Thread oddjobThread = new Thread(oddjob);
		oddjobThread.start();
		
		states.checkWait();
		
		IconSteps icons = new IconSteps((Iconic) test);
		
		icons.startCheck(IconHelper.EXECUTING, IconHelper.STOPPING,
				IconHelper.EXCEPTION);
		
		((Stoppable) test).stop();
		
		icons.checkWait();		
		
		oddjobThread.join(50000);
		if (oddjobThread.isAlive()) {
			throw new IllegalStateException("Oddjob failed to stop");
		}
		
		StateEvent event = ((Stateful) test).lastStateEvent();
		
		assertEquals(JobState.EXCEPTION, event.getState());
		assertEquals(ComparisonStoppedException.class, 
				event.getException().getClass());
		
		assertEquals(ParentState.EXCEPTION, 
				oddjob.lastStateEvent().getState());
		
		oddjob.destroy();
	}
}
