package org.oddjob.beancmpr.beans;

import java.io.File;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.oddjob.Oddjob;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.tools.OddjobTestHelper;

public class BeanArrayComparerTypeTest extends TestCase {

	private static final Logger logger = Logger.getLogger(
			BeanArrayComparerTypeTest.class);
	
	
	public void testArraysOfInts() throws Exception {
		
		File file = new File(getClass().getResource(
				"BeanArrayExamples.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture console = new ConsoleCapture();
		console.capture(Oddjob.CONSOLE);
		
		oddjob.run();
		
		console.close();
		
		assertEquals(true, oddjob.lastStateEvent().getState().isComplete());

		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("BeanArrayExamplesOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		oddjob.destroy();
	}
}

