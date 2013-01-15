package org.oddjob.beancmpr;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.state.ParentState;

public class OddjobDatabaseExampleTest {

	static String EOL = System.getProperty("line.separator");
	
	public static String expectedKeysDifferent =
			"matchResultType  ID  xTYPE   yTYPE   TYPEComparison  xQUANTITY  yQUANTITY  QUANTITYComparison  xCOLOUR  yCOLOUR  COLOURComparison" + EOL +
			"---------------  --  ------  ------  --------------  ---------  ---------  ------------------  -------  -------  ----------------" + EOL +
			"NOT_EQUAL        1   Apple   Apple                   4          4                              green    red      green<>red" + EOL +
			"NOT_EQUAL        2   Banana  Banana                  3          4          1.0 (33.3%)         yellow   yellow   " + EOL +
			"X_MISSING        3           Orange                             2                                       orange   " + EOL +
			"Y_MISSING        5   Orange                          2                                         orange            " + EOL;

		public static String expectedKeysSame =
			"matchResultType  TYPE    xQUANTITY  yQUANTITY  QUANTITYComparison  xCOLOUR  yCOLOUR" + EOL +
			"---------------  ------  ---------  ---------  ------------------  -------  -------" + EOL +
			"EQUAL            Apple   4          4                              green    red" + EOL +
			"NOT_EQUAL        Banana  3          4          1.0 (33.3%)         yellow   yellow" + EOL +
			"EQUAL            Orange  2          2                              orange   orange" + EOL;

	@Test
	public void testExample() 
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
