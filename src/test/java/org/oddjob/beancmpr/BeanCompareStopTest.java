package org.oddjob.beancmpr;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import org.oddjob.FailedToStopException;
import org.oddjob.Iconic;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.Stateful;
import org.oddjob.Stoppable;
import org.oddjob.images.IconHelper;
import org.oddjob.state.JobState;
import org.oddjob.state.ParentState;
import org.oddjob.state.StateEvent;
import org.oddjob.tools.IconSteps;
import org.oddjob.tools.StateSteps;

public class BeanCompareStopTest extends TestCase {

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
			
			return new Iterator<Fruit>() {
				
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
	
	public void testStopBeanCompareJobInOddjob() throws InterruptedException, FailedToStopException {
		
		File config = new File(getClass().getResource(
				"BeanCompareStopTest.xml").getFile());
		
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
