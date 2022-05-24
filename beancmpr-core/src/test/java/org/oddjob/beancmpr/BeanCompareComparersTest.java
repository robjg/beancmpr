package org.oddjob.beancmpr;

import junit.framework.TestCase;
import org.junit.Assert;
import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.state.ParentState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class BeanCompareComparersTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(BeanCompareComparersTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("---------------------------  " + getName() + 
				"  ----------------------------");
	}

	public void testBeanComparersExample() throws ArooaPropertyException, ArooaConversionException {

		URL config = Objects.requireNonNull(
				getClass().getResource("BeanCompareComparers.xml"));
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(new File(config.getFile()));
		
		oddjob.run();
		
		Assert.assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());

		OddjobLookup lookup = new OddjobLookup(
				oddjob);

		assertEquals(0, (int) lookup.lookup("check1.differentCount",
				int.class));
		
		oddjob.destroy();
	}	
}
