package org.oddjob.beancmpr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.state.ParentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Objects;

class OddjobExamplesTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(OddjobExamplesTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
	}
	
	@Test
	void testBeanBusExample()
	throws ArooaPropertyException, ArooaConversionException {

		URL config = Objects.requireNonNull(
				getClass().getResource("OddjobBeanBusExample.xml"));
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(new File(config.getFile()));
		
		oddjob.run();

		assertEquals(ParentState.COMPLETE,
				oddjob.lastStateEvent().getState());

		OddjobLookup lookup = new OddjobLookup(
				oddjob);
		
		assertEquals(3,
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

	@Test void testDatabaseExample() throws ArooaPropertyException, ArooaConversionException {
		
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/beancmpr/OddjobDatabaseExample.xml",
				getClass().getClassLoader()));
		oddjob.run();

		assertEquals(ParentState.COMPLETE,
				oddjob.lastStateEvent().getState());

		String byIdResults = new OddjobLookup(
				oddjob).lookup("by-id-results", String.class);
		
		assertEquals(expectedKeysDifferent,
				byIdResults);
		
		String byTypeResults = new OddjobLookup(
				oddjob).lookup("by-type-results", String.class);
				
		assertEquals(expectedKeysSame,
				byTypeResults);

		oddjob.destroy();
	}
}
