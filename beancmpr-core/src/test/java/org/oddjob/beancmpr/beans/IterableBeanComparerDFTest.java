/*
 * (c) Rob Gordon 2005.
 */
package org.oddjob.beancmpr.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.OddjobDescriptorFactory;
import org.oddjob.arooa.ArooaDescriptor;
import org.oddjob.arooa.ArooaParseException;
import org.oddjob.arooa.ArooaType;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignParser;
import org.oddjob.arooa.design.view.ViewMainHelper;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.tools.OddjobTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 *
 */
public class IterableBeanComparerDFTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(IterableBeanComparerDFTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
	}

	DesignInstance design;
	
	@Test
	void testCreate() throws ArooaParseException {
		
		String xml =  
				"<beancmpr:collection-comparer xmlns:beancmpr='oddjob:beancmpr'" +
				"      keys='id, colour'" +
				"      values='quantity, price' " +
				"      others='country' sorted='true'>" +
				" <comparersByType>" +
				"  <beancmpr:comparers-by-type/>" +
				" </comparersByType>" +
				" <comparersByName>" +
				"  <beancmpr:comparers-by-name/>" +
				" </comparersByName>" +
				"</beancmpr:collection-comparer>";
	
    	ArooaDescriptor descriptor = 
    		new OddjobDescriptorFactory().createDescriptor(null);
		
		DesignParser parser = new DesignParser(
				new StandardArooaSession(descriptor));
		parser.setArooaType(ArooaType.VALUE);
		
		parser.parse(new XMLConfiguration("TEST", xml));
		
		design = parser.getDesign();
		
		assertEquals(IterableBeansComparerDesign.class, design.getClass());
		
		IterableBeansComparerType<?> test = (IterableBeansComparerType<?>) 
				OddjobTestHelper.createValueFromConfiguration(
				design.getArooaContext().getConfigurationNode());
		
		assertEquals("[id, colour]", Arrays.toString(test.getKeys()));
		assertEquals("[quantity, price]", Arrays.toString(test.getValues()));
		assertEquals("[country]", Arrays.toString(test.getOthers()));
		assertEquals(true, test.isSorted());
		assertNotNull(test.getComparersByType());
		assertNotNull(test.getComparersByName());
		
	}

	public static void main(String[] args) throws ArooaParseException {

		IterableBeanComparerDFTest test = new IterableBeanComparerDFTest();
		test.testCreate();
		
		ViewMainHelper view = new ViewMainHelper(test.design);
		view.run();
		
	}

}
