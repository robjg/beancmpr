package org.oddjob.beancmpr.beans;

import java.io.File;

import junit.framework.TestCase;

import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.tools.OddjobTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanComparerTypeExamplesTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(BeanComparerTypeExamplesTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("---------------------------  " + getName() + 
				"  ----------------------------");
	};
	
	
	public void testCompareTwoNumbersExample() throws Exception {
		
		File file = new File(getClass().getResource(
				"BeanComparerForTwoNumbers.xml").getFile());
		
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
				).getResourceAsStream("BeanComparerForTwoNumbersOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		oddjob.destroy();
	}
	
	public static class Fruit {
		
		private String type;
		private String colour;
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public String getColour() {
			return colour;
		}
		
		public void setColour(String colour) {
			this.colour = colour;
		}		
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + ": type=" + type + 
					", colour=" + colour;
		}
	}
	
	public void testCompareTwoBeansExample() throws Exception {
		
		File file = new File(getClass().getResource(
				"BeanComparerForTwoBeans.xml").getFile());
		
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
				).getResourceAsStream("BeanComparerForTwoBeansOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		oddjob.destroy();
	}
	
	public static class Snack {
		
		private int id;
		
		private Fruit fruit;		

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		public Fruit getFruit() {
			return fruit;
		}
		
		public void setFruit(Fruit fruit) {
			this.fruit = fruit;
		}
	}
	
	
	public void testPropertyThatIsBeanExample() throws ArooaPropertyException, ArooaConversionException {
		
		File file = new File(
				getClass().getResource(
						"BeanComparerForProperty.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {
			
			oddjob.run();
		}
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		assertEquals(3, (int) lookup.lookup("compare.comparedCount", 
				int.class));	
		assertEquals(2, (int) lookup.lookup("compare.matchedCount", 
				int.class));	
		
		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("BeanComparerForPropertyOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		oddjob.destroy();
	}
	
}
