package org.oddjob.beancmpr.beans;

import java.io.File;

import junit.framework.TestCase;

import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.tools.OddjobTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanArrayComparerTypeTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(
			BeanArrayComparerTypeTest.class);
	
	
	public void testArrayOfIntsExample() throws Exception {
		
		File file = new File(getClass().getResource(
				"ArrayOfIntsCompare.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {
			oddjob.run();
		}
		
		assertEquals(true, oddjob.lastStateEvent().getState().isComplete());

		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("ArrayOfIntsCompareOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		oddjob.destroy();
	}

	public void testArrayOfBeansCompareByPropertiesExample() throws Exception {
		
		File file = new File(getClass().getResource(
				"ArrayOfBeansCompareByProperties.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {

			oddjob.run();
		}
		
		assertEquals(true, oddjob.lastStateEvent().getState().isComplete());

		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("ArrayOfBeansCompareByPropertiesOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		oddjob.destroy();
	}

	
	public void testBeanWithArrayPropertyCompare() throws Exception {
		
		File file = new File(getClass().getResource(
				"BeanWithArrayPropertyCompare.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {
			
			oddjob.run();
		}
		
		assertEquals(true, oddjob.lastStateEvent().getState().isComplete());

		console.dump(logger);
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		int result = lookup.lookup("compare.breaksCount", int.class);
		
		assertEquals(0, result);
		
		oddjob.destroy();
	}
	
	
	public static class ArrayPropertyTestData {
		
		final BeanWithArrayProperty x = new BeanWithArrayProperty(new double[] {
			50.1, 70.2, 30.5 }, "X");
		
		final BeanWithArrayProperty y = new BeanWithArrayProperty(new double[] {
			50.5, 70.4, 30.1 }, "Y");
		
		public BeanWithArrayProperty getX() {
			return x;
		}
		
		public BeanWithArrayProperty getY() {
			return y;
		}
	}
	
	public static class BeanWithArrayProperty {
		
		final String name;
		
		final double[] numbers;
		
		public BeanWithArrayProperty(double[] numbers, String name) {
			this.numbers = numbers;
			this.name = name;
		}
		
		public double[] getNumbers() {
			return numbers;
		}
		
		@Override
		public String toString() {
			return "Bean" + name;
		}
	}
}

