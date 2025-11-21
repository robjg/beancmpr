package org.oddjob.beancmpr;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.arooa.types.ArooaObject;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanCompareJobTest {

	private static final Logger logger = LoggerFactory.getLogger(BeanCompareJobTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

        logger.debug("========================== {} ===================", testInfo.getDisplayName());
	}
		
	SharedTestData data = new SharedTestData();
	
	static String EOL = System.lineSeparator();
	
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

	@Test
	void testMainDocumentedExample() throws ArooaPropertyException {

		File config = new File(
				Objects.requireNonNull(
						getClass().getResource("BeanCompareJobExample.xml")).getFile());

		Oddjob oddjob = new Oddjob();
		oddjob.setFile(config);

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close ignored = console.captureConsole()) {

			oddjob.run();
		}

		assertEquals(ParentState.COMPLETE,
				oddjob.lastStateEvent().getState());

        List<String> lines = console.getAsList().stream()
                .map(String::stripTrailing)
                .toList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("BeanCompareJobExampleOut.txt"))))
                .lines().toList();

        assertThat(lines, is(expected));

		oddjob.destroy();
	}

    @Test
    void compareTwoListsOfIntegers() throws ArooaConversionException {

        File file = new File(Objects.requireNonNull(getClass().getResource(
                "CompareTwoListsOfIntegers.xml")).getFile());

        Oddjob oddjob = new Oddjob();
        oddjob.setFile(file);

        ConsoleCapture console = new ConsoleCapture();
        try (ConsoleCapture.Close ignored = console.captureConsole()) {

            oddjob.run();
        }

        assertTrue(oddjob.lastStateEvent().getState().isComplete());

        console.dump(logger);

        List<String> lines = console.getAsList();

        List<String> expected = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        getClass().getResourceAsStream("CompareTwoListsOfIntegersOut.txt"))))
                        .lines().toList();

        assertThat(lines, is(expected));

        OddjobLookup lookup = new OddjobLookup(oddjob);

        assertThat(lookup.lookup("compare.breaksCount", int.class),
                Matchers.is(4));
        assertThat(lookup.lookup("compare.matchedCount", int.class),
                Matchers.is(1));

        oddjob.destroy();

    }
}
