package org.oddjob.beancmpr;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.ArooaParseException;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.state.ParentState;

public class BeanCompareComparersTest extends TestCase {

	private static final Logger logger = Logger.getLogger(BeanCompareComparersTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("---------------------------  " + getName() + 
				"  ----------------------------");
	};
	
	SharedTestData data = new SharedTestData();
	
	public void testBeanComparersExample() throws ArooaParseException, ArooaPropertyException, ArooaConversionException {
		
		URL config = getClass().getResource("BeanCompareComparers.xml");
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(new File(config.getFile()));
		
		oddjob.run();
		
		Assert.assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());

		OddjobLookup lookup = new OddjobLookup(
				oddjob);

		assertEquals((int) 0, (int) lookup.lookup("check1.differentCount", 
				int.class));
		
		oddjob.destroy();
	}	
}
