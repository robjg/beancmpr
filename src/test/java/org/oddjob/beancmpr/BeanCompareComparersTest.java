package org.oddjob.beancmpr;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.junit.Assert;
import org.oddjob.Oddjob;
import org.oddjob.arooa.ArooaParseException;
import org.oddjob.state.ParentState;

public class BeanCompareComparersTest extends TestCase {

	SharedTestData data = new SharedTestData();
	
	public void testBeanComparersExample() throws ArooaParseException {
		
		URL config = getClass().getResource("BeanCompareComparers.xml");
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(new File(config.getFile()));
		
		oddjob.run();
		
		Assert.assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());

//		OddjobLookup lookup = new OddjobLookup(
//				oddjob);
//		
//		
//		String definition =
//				"<arooa:magic-beans xmlns:arooa='http://rgordon.co.uk/oddjob/arooa'>" +
//				" <definitions>" +
//				"  <is element='result'>" +
//				"   <properties>" +
//				"    <is name='matchResultType' type='java.lang.String'/>" +
//				"    <is name='quantity' type='java.lang.Integer'/>" +
//				"    <is name='price' type='java.lang.Double'/>" +
//				"   </properties>" +
//				"  </is>" +
//				" </definitions>" +
//				"</arooa:magic-beans>";
//			
//		MagicBeanDescriptorFactory descriptorFactory = 
//				new MagicBeanDescriptorFactory();
//		
//		StandardArooaParser parser = new StandardArooaParser(
//				descriptorFactory,
//				new ArooaDescriptorDescriptorFactory().createDescriptor(
//						getClass().getClassLoader()));
//		
//		String expected = 
//				"<list>" +
//				" <";
//
//		parser.parse(new XMLConfiguration("XML", definition));
			
	}	
}
