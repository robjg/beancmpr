package org.oddjob.beancmpr.beans;

import java.io.File;

import org.oddjob.Oddjob;
import org.oddjob.OddjobLookup;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.reflect.ArooaPropertyException;
import org.oddjob.state.ParentState;

import junit.framework.TestCase;

public class BeanComparerBeanTest extends TestCase {

	public static class Fruit {
		
		private String type;
		private String colour;
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public String getColour() {
			return colour;
		}
		
		public void setColour(String colour) {
			this.colour = colour;
		}		
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + ": type=" + type + 
					", colour=" + colour;
		}
	}
	
	public static class Snack {
		
		private Fruit fruit;
		
		public Fruit getFruit() {
			return fruit;
		}
		
		public void setFruit(Fruit fruit) {
			this.fruit = fruit;
		}
	}
	
	
	public void testNestedComparersInOddjob() throws ArooaPropertyException, ArooaConversionException {
		
		File file = new File(
				getClass().getResource(
						"BeanComparerExample.xml").getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);
		
		oddjob.run();
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		OddjobLookup lookup = new OddjobLookup(oddjob);
		
		assertEquals(2, (int) lookup.lookup("compare.matchedCount", 
				int.class));	
		
		oddjob.destroy();
	}
	
}
