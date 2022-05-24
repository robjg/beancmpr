package org.oddjob.beancmpr.results;

import java.text.ParseException;

import junit.framework.TestCase;

import org.oddjob.OddjobDescriptorFactory;
import org.oddjob.OddjobSessionFactory;
import org.oddjob.arooa.ArooaDescriptor;
import org.oddjob.arooa.ArooaParseException;
import org.oddjob.arooa.ArooaType;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignParser;
import org.oddjob.arooa.design.view.ViewMainHelper;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.arooa.standard.StandardFragmentParser;
import org.oddjob.arooa.xml.XMLConfiguration;
import org.oddjob.beancmpr.results.BeanCreatingResultDesign;
import org.oddjob.beancmpr.results.BeanCreatingResultHandler;
import org.oddjob.beancmpr.results.SimpleResultBeanFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class BeanCreatingResultHandlerDFTest extends TestCase {
	private static final Logger logger = LoggerFactory.getLogger(BeanCreatingResultHandlerDFTest.class);
	
	public void setUp() {
		logger.debug("========================== " + getName() + "===================" );
	}

	DesignInstance design;
	
	public void testCreate() throws ArooaParseException, ParseException {
		
		String xml =  
				"<beancmpr:bean-results xmlns:beancmpr='oddjob:beancmpr'" +
				"     xPropertyPrefix='desk'" +
				"     yPropertyPrefix='broker'" +
				"     ignoreMatches='true'" +
				"   >" +
				"   <out>" +
				"    <list/>" +
				"   </out>" +
				"   <factoryBuilder>" +
				"    <bean class='org.oddjob.beancmpr.results.SimpleResultBeanFactoryBuilder'/>" +
				"   </factoryBuilder>" +
				"</beancmpr:bean-results>";
	
    	ArooaDescriptor descriptor = 
    		new OddjobDescriptorFactory().createDescriptor(null);
		
		DesignParser parser = new DesignParser(
				new StandardArooaSession(descriptor));
		parser.setArooaType(ArooaType.VALUE);
		
		parser.parse(new XMLConfiguration("TEST", xml));
		
		design = parser.getDesign();
		
		assertEquals(BeanCreatingResultDesign.class, design.getClass());
		
    	StandardFragmentParser fragmentParser = new StandardFragmentParser(
    			new OddjobSessionFactory().createSession());    	
    	
    	fragmentParser.parse(design.getArooaContext().getConfigurationNode());
    	
		BeanCreatingResultHandler test = (BeanCreatingResultHandler)
				fragmentParser.getRoot();
		
		assertEquals("desk", test.getxPropertyPrefix());
		assertEquals("broker", test.getyPropertyPrefix());
		assertEquals(true, test.isIgnoreMatches());
		assertNotNull(test.getOut());
		assertEquals(SimpleResultBeanFactoryBuilder.class,
				test.getFactoryBuilder().getClass());
		
	}

	public static void main(String args[]) throws ArooaParseException, ParseException {

		BeanCreatingResultHandlerDFTest test = new BeanCreatingResultHandlerDFTest();
		test.testCreate();
		
		ViewMainHelper view = new ViewMainHelper(test.design);
		view.run();
		
	}

}
