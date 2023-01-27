package org.oddjob.beancmpr.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.Oddjob;
import org.oddjob.arooa.types.ValueFactory;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.state.ParentState;
import org.oddjob.tools.ConsoleCapture;
import org.oddjob.tools.OddjobTestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

class MapComparerTypeTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(
			MapComparerTypeTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
	}
	
	public static class FruitMap1 implements ValueFactory<Map<String, Integer>> {
	
		private final Map<String, Integer> map =
				new HashMap<>();
		
		@Override
		public Map<String, Integer> toValue() {
			return map;
		}
		
		public Map<String, Integer> getTheMap() {
			return map;
		}
				
		public void setFruit(String key, Integer value) {
			if (value == null) {
				map.remove(key);
			}
			else {
				map.put(key, value);
			}
		}
	}
	
	@Test
	void testSimpleMapExample() {
		
		File file = new File(Objects.requireNonNull(getClass().getResource(
				"MapCompareSimple.xml")).getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {
			
			oddjob.run();
		}
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("MapCompareSimpleOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		assertEquals(6, lines.length);
		
		oddjob.destroy();
	}	
	
	public static class MapBean1 {
		
		private Map<String, Integer> fruitMap;

		public Map<String, Integer> getFruitMap() {
			return fruitMap;
		}

		public void setFruitMap(Map<String, Integer> fruitMap) {
			this.fruitMap = fruitMap;
		}
	}
	
	
	@Test
	void testMapCompareForBeanPropertyExample() {
		
		File file = new File(Objects.requireNonNull(getClass().getResource(
				"MapCompareOfBeanProperty.xml")).getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {
			
			oddjob.run();
		}
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("MapCompareOfBeanPropertyOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
		
		assertEquals(3, lines.length);
		
		oddjob.destroy();
	}	
	
	public static class KeyBean {
		
		private String fruit;
		
		private String colour;

		public String getFruit() {
			return fruit;
		}

		public void setFruit(String fruit) {
			this.fruit = fruit;
		}

		public String getColour() {
			return colour;
		}

		public void setColour(String colour) {
			this.colour = colour;
		}		
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + 
					": fruit=" + fruit +
					", colour=" + colour;
		}
	}
	
	public static class ValueBean {
		
		private int quantity;
		
		private double price;

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + 
					": quantity=" + quantity +
					", price=" + price;
		}
	}

	public static class FruitMapEntry {
		
		private KeyBean key;

		private ValueBean value;
		
		public KeyBean getKey() {
			return key;
		}

		public void setKey(KeyBean key) {
			this.key = key;
		}

		public ValueBean getValue() {
			return value;
		}

		public void setValue(ValueBean entry) {
			this.value = entry;
		}
	}
	
	public static class FruitMap2 implements ValueFactory<Map<KeyBean, ValueBean>> {
		
		private final List<FruitMapEntry> entries =
				new ArrayList<>();
		
		@Override
		public Map<KeyBean, ValueBean> toValue() {
			Map<KeyBean, ValueBean> map = new HashMap<>();
			
			for (FruitMapEntry entry : entries) {
				map.put(entry.getKey(), entry.getValue());
			}
			
			return map;
		}
		
		public void setEntries(int index, FruitMapEntry entry) {
			if (entry == null) {
				entries.remove(index);
			}
			else {
				entries.add(entry);
			}
		}
	}
	
	public static class MapBean2 implements ValueFactory<Map<KeyBean, ValueBean>> {
		
		private Map<KeyBean, ValueBean> fruitMap;

		@Override
		public Map<KeyBean, ValueBean> toValue() {
			return fruitMap;
		}

		public void setFruitMap(Map<KeyBean, ValueBean> fruitMap) {
			this.fruitMap = fruitMap;
		}
	}
	
	@Test
	void testComplexMapExample() {
		
		File file = new File(Objects.requireNonNull(getClass().getResource(
				"MapCompareComplex.xml")).getFile());
		
		Oddjob oddjob = new Oddjob();
		oddjob.setFile(file);

		ConsoleCapture console = new ConsoleCapture();
		try (ConsoleCapture.Close close = console.captureConsole()) {
			
			oddjob.run();
		}
		
		assertEquals(ParentState.COMPLETE, 
				oddjob.lastStateEvent().getState());
		
		console.dump(logger);
		
		String[] lines = console.getLines();
		
		String[] expected = OddjobTestHelper.streamToLines(getClass(
				).getResourceAsStream("MapCompareComplexOut.txt"));
		
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i].trim(), lines[i].trim());
		}
				
		assertEquals(7, lines.length);
		
		oddjob.destroy();		
	}	
}
