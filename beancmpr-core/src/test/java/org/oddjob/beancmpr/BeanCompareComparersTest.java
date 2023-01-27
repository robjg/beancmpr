package org.oddjob.beancmpr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
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

class BeanCompareComparersTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(BeanCompareComparersTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
	}

	@Test
	void testBeanComparersExample() throws ArooaPropertyException, ArooaConversionException {

		URL config = Objects.requireNonNull(
				getClass().getResource("BeanCompareComparers.xml"));
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(new File(config.getFile()));
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE,
				oddjob.lastStateEvent().getState());

		OddjobLookup lookup = new OddjobLookup(
				oddjob);

		assertEquals(0, (int) lookup.lookup("check1.differentCount",
				int.class));
		
		oddjob.destroy();
	}	
}
