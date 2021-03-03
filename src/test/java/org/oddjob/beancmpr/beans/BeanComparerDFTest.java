/*
 * (c) Rob Gordon 2005.
 */
package org.oddjob.beancmpr.beans;

import java.text.ParseException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.oddjob.OddjobDescriptorFactory;
import org.oddjob.arooa.ArooaDescriptor;
import org.oddjob.arooa.ArooaParseException;
import org.oddjob.arooa.ArooaType;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignParser;
import org.oddjob.arooa.design.view.ViewMainHelper;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.tools.OddjobTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class BeanComparerDFTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(BeanComparerDFTest.class);
	
	public void setUp() {
		logger.debug("========================== " + getName() + "===================" );
	}

	DesignInstance design;
	
	public void testCreate() throws ArooaParseException, ParseException {
		
		String xml =  
				"<beancmpr:bean-comparer xmlns:beancmpr='oddjob:beancmpr'" +
				"      values='quantity, price' " +
				"      others='country'>" +
				" <comparersByType>" +
				"  <beancmpr:comparers-by-type/>" +
				" </comparersByType>" +
				" <comparersByName>" +
				"  <beancmpr:comparers-by-name/>" +
				" </comparersByName>" +
				"</beancmpr:bean-comparer>";
	
    	ArooaDescriptor descriptor = 
    		new OddjobDescriptorFactory().createDescriptor(null);
		
		DesignParser parser = new DesignParser(
				new StandardArooaSession(descriptor));
		parser.setArooaType(ArooaType.VALUE);
		
		parser.parse(new XMLConfiguration("TEST", xml));
		
		design = parser.getDesign();
		
		assertEquals(BeanComparerDesign.class, design.getClass());
		
		BeanComparerType<?> test = (BeanComparerType<?>) 
				OddjobTestHelper.createValueFromConfiguration(
				design.getArooaContext().getConfigurationNode());
		
		assertEquals("[quantity, price]", Arrays.toString(test.getValues()));
		assertEquals("[country]", Arrays.toString(test.getOthers()));
		assertNotNull(test.getComparersByType());
		assertNotNull(test.getComparersByName());
		
	}

	public static void main(String args[]) throws ArooaParseException, ParseException {

		BeanComparerDFTest test = new BeanComparerDFTest();
		test.testCreate();
		
		ViewMainHelper view = new ViewMainHelper(test.design);
		view.run();
		
	}

}
