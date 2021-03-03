package org.oddjob.beancmpr;

import java.io.File;
import java.net.URL;
import java.text.ParseException;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.state.ParentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OddjobExamplesTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(OddjobExamplesTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("---------------------------  " + getName() + 
				"  ----------------------------");
	};
	
	@Test
	public void testBeanBusExample() 
	throws ArooaPropertyException, ArooaConversionException, ParseException {
		
		URL config = getClass().getResource("OddjobBeanBusExample.xml");
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(new File(config.getFile()));
		
		oddjob.run();

		Assert.assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());

		OddjobLookup lookup = new OddjobLookup(
				oddjob);
		
		Assert.assertEquals(3, 
				(int) lookup.lookup("bean-capture.count", int.class));
		
		oddjob.destroy();
	}
	
	static String EOL = System.getProperty("line.separator");
	
	public static String expectedKeysDifferent =
			"matchResultType  ID  xTYPE   yTYPE   TYPEComparison  xQUANTITY  yQUANTITY  QUANTITYComparison  xCOLOUR  yCOLOUR  COLOURComparison" + EOL +
			"---------------  --  ------  ------  --------------  ---------  ---------  ------------------  -------  -------  ----------------" + EOL +
			"NOT_EQUAL        1   Apple   Apple                   4          4                              green    red      green<>red" + EOL +
			"NOT_EQUAL        2   Banana  Banana                  3          4          3<>4                yellow   yellow   " + EOL +
			"x_MISSING        3           Orange                             2                                       orange   " + EOL +
			"y_MISSING        5   Orange                          2                                         orange            " + EOL;

		public static String expectedKeysSame =
			"matchResultType  TYPE    xQUANTITY  yQUANTITY  QUANTITYComparison  xCOLOUR  yCOLOUR" + EOL +
			"---------------  ------  ---------  ---------  ------------------  -------  -------" + EOL +
			"EQUAL            Apple   4          4                              green    red" + EOL +
			"NOT_EQUAL        Banana  3          4          3<>4                yellow   yellow" + EOL +
			"EQUAL            Orange  2          2                              orange   orange" + EOL;

	public void testDatabaseExample() 
	throws ArooaPropertyException, ArooaConversionException, ParseException {
		
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/beancmpr/OddjobDatabaseExample.xml",
				getClass().getClassLoader()));
		oddjob.run();

		Assert.assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());

		String byIdResults = new OddjobLookup(
				oddjob).lookup("by-id-results", String.class);
		
		Assert.assertEquals(expectedKeysDifferent, 
				byIdResults);
		
		String byTypeResults = new OddjobLookup(
				oddjob).lookup("by-type-results", String.class);
				
		Assert.assertEquals(expectedKeysSame, 
				byTypeResults);

		oddjob.destroy();
	}
}
