package org.oddjob.beancmpr;


import org.apache.commons.beanutils.DynaBean;
import org.junit.jupiter.api.Test;
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

public class BeanCompareDFTest extends TestCase {

	DesignInstance design;
	
	@Test
	void testCreate() throws ArooaParseException {
		
		String xml =  
			"<beancmpr:compare xmlns:beancmpr='oddjob:beancmpr'" +
			"     name='A Test'" +
			"	  id='this'" +
			"   >" +
			"   <inX>" +
			"    <list/>" +
			"   </inX>" +
			"   <inY>" +
			"    <list/>" +
			"   </inY>" +
			"   <results>" +
			"     <beancmpr:bean-results/>" +
			"   </results>" +
			"</beancmpr:compare>";
		
    	ArooaDescriptor descriptor = 
    		new OddjobDescriptorFactory().createDescriptor(null);
    	
		DesignParser parser = new DesignParser(
				new StandardArooaSession(descriptor));
		parser.setArooaType(ArooaType.COMPONENT);
		
		parser.parse(new XMLConfiguration("TEST", xml));
		
		design = parser.getDesign();
		
		assertEquals(BeanCompareDesign2.class, design.getClass());
		
    	StandardFragmentParser fragmentParser = new StandardFragmentParser(
    			new OddjobSessionFactory().createSession());    	
    	fragmentParser.setArooaType(ArooaType.COMPONENT);
    	
    	fragmentParser.parse(design.getArooaContext().getConfigurationNode());

		DynaBean test = (DynaBean) fragmentParser.getRoot();
		
		assertEquals("A Test", test.get("name"));
		
		assertNotNull(test.get("inX"));
		assertNotNull(test.get("inY"));
		assertNotNull(test.get("results"));
				
	}

	public static void main(String[] args) throws ArooaParseException {

		BeanCompareDFTest test = new BeanCompareDFTest();
		test.testCreate();
		
		ViewMainHelper view = new ViewMainHelper(test.design);
		view.run();
	}
}
