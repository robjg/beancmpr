package org.oddjob.beancmpr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.types.ArooaObject;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.state.ParentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BeanCompareJobTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(BeanCompareJobTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
	}
		
	SharedTestData data = new SharedTestData();
	
	static String EOL = System.getProperty("line.separator");
	
	public static String expectedKeysDifferent =
		"matchResultType  id  xType   yType   typeComparison  xQuantity  yQuantity  quantityComparison  xColour  yColour  colourComparison" + EOL +
		"---------------  --  ------  ------  --------------  ---------  ---------  ------------------  -------  -------  ----------------" + EOL +
		"NOT_EQUAL        1   Apple   Apple                   4          4                              green    red      green<>red" + EOL +
		"NOT_EQUAL        2   Banana  Banana                  3          4          3<>4                yellow   yellow   " + EOL +
		"x_MISSING        3           Orange                             2                                       orange   " + EOL +
		"y_MISSING        5   Orange                          2                                         orange            " + EOL;

	public static String expectedKeysSame =
		"matchResultType  type    xQuantity  yQuantity  quantityComparison  xColour  yColour" + EOL +
		"---------------  ------  ---------  ---------  ------------------  -------  -------" + EOL +
		"EQUAL            Apple   4          4                              green    red" + EOL +
		"NOT_EQUAL        Banana  3          4          3<>4                yellow   yellow" + EOL +
		"EQUAL            Orange  2          2                              orange   orange" + EOL;

	
	@Test
	void testUnsortedKeysDifferent() throws Exception {
							
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/beancmpr/BeanCompareJobExample1.xml", 
				getClass().getClassLoader()));
		oddjob.setExport("listX", new ArooaObject(data.getListFruitX()));
		oddjob.setExport("listY", new ArooaObject(data.getListFruitY()));
		oddjob.setExport("sorted", new ArooaObject(false));
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		String results = new OddjobLookup(
				oddjob).lookup("results", String.class);
		
		assertEquals(expectedKeysDifferent, results);
	}
	
	@Test
	void testUnsortedKeysSame() throws Exception {
						
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/beancmpr/BeanCompareJobExample2.xml", 
				getClass().getClassLoader()));
		oddjob.setExport("listX", new ArooaObject(data.getListFruitX()));
		oddjob.setExport("listY", new ArooaObject(data.getListFruitY()));
		oddjob.setExport("sorted", new ArooaObject(false));
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		String results = new OddjobLookup(
				oddjob).lookup("results", String.class);
				
		assertEquals(expectedKeysSame, results);
	}
	
	@Test
	void testSortedKeysDifferent() throws Exception {
		
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/beancmpr/BeanCompareJobExample1.xml", 
				getClass().getClassLoader()));
		oddjob.setExport("listX", new ArooaObject(data.getListFruitX()));
		oddjob.setExport("listY", new ArooaObject(data.getListFruitY()));
		oddjob.setExport("sorted", new ArooaObject(true));
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		String results = new OddjobLookup(
				oddjob).lookup("results", String.class);
		
		assertEquals(expectedKeysDifferent, results);
	}
	
	@Test
	void testSortedKeysSame() throws Exception {
						
		Oddjob oddjob = new Oddjob();
		oddjob.setConfiguration(new XMLConfiguration(
				"org/oddjob/beancmpr/BeanCompareJobExample2.xml", 
				getClass().getClassLoader()));
		oddjob.setExport("listX", new ArooaObject(data.getListFruitX()));
		oddjob.setExport("listY", new ArooaObject(data.getListFruitY()));
		oddjob.setExport("sorted", new ArooaObject(true));
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		String results = new OddjobLookup(
				oddjob).lookup("results", String.class);
				
		assertEquals(expectedKeysSame, results);
	}
}
