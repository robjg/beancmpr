package org.oddjob.beancmpr.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.BeanCompareJob;

public class ResultFactoryDifferentTypesTest extends TestCase {

	private static final Logger logger = Logger.getLogger(
			ResultFactoryDifferentTypesTest.class);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		logger.info("---------------------  " + getName() + "  -----------------");
	}
	
	public static class Bean1 {
		
		private final int id;
		
		private final Double quantity;

		public Bean1(int id, Double quantity) {
			this.id = id;
			this.quantity = quantity;
		}
		
		public int getId() {
			return id;
		}

		public Double getQuantity() {
			return quantity;
		}
	}
	
	public static class Bean2 {
		
		private final int id;
		
		private final BigDecimal quantity;

		public Bean2(int id, BigDecimal quantity) {
			this.id = id;
			this.quantity = quantity;
		}
		
		public int getId() {
			return id;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}
	}
	
	public void testComparisonFirst() {
		
		List<Bean1> bean1s = Arrays.asList(
				new Bean1(1, 2.5),
				new Bean1(2, 3.5));
		
		List<Bean2> bean2s = Arrays.asList(
				new Bean2(1, new BigDecimal(2.6)),
				new Bean2(3, new BigDecimal(4.5)));
		
		List<Object> results = new ArrayList<Object>();
				
		ArooaSession session = new StandardArooaSession();
		
		BeanCompareJob comparer = new BeanCompareJob();
		comparer.setArooaSession(session);
		comparer.acceptDestination(results);
		comparer.setInX(bean1s);
		comparer.setInY(bean2s);
		comparer.setKeyProperties(new String[] {"id"});
		comparer.setValueProperties(new String[] {"quantity"});
		
		comparer.run();
		
		assertEquals(3, results.size());
	}
	
	public void testXMissingFirst() {
		
		List<Bean1> bean1s = Arrays.asList(
				new Bean1(2, 2.5),
				new Bean1(3, 3.5));
		
		List<Bean2> bean2s = Arrays.asList(
				new Bean2(1, new BigDecimal(2.6)),
				new Bean2(3, new BigDecimal(4.5)));
		
		List<Object> results = new ArrayList<Object>();
				
		ArooaSession session = new StandardArooaSession();
		
		BeanCompareJob comparer = new BeanCompareJob();
		comparer.setArooaSession(session);
		comparer.acceptDestination(results);
		comparer.setInX(bean1s);
		comparer.setInY(bean2s);
		comparer.setKeyProperties(new String[] {"id"});
		comparer.setValueProperties(new String[] {"quantity"});
		
		comparer.run();
		
		assertEquals(3, results.size());
	}
	
	public void testYMissingFirst() {
		
		List<Bean1> bean1s = Arrays.asList(
				new Bean1(1, 2.5),
				new Bean1(3, 3.5));
		
		List<Bean2> bean2s = Arrays.asList(
				new Bean2(2, new BigDecimal(2.6)),
				new Bean2(3, new BigDecimal(4.5)));
		
		List<Object> results = new ArrayList<Object>();
				
		ArooaSession session = new StandardArooaSession();
		
		BeanCompareJob comparer = new BeanCompareJob();
		comparer.setArooaSession(session);
		comparer.acceptDestination(results);
		comparer.setInX(bean1s);
		comparer.setInY(bean2s);
		comparer.setKeyProperties(new String[] {"id"});
		comparer.setValueProperties(new String[] {"quantity"});
		
		comparer.run();
		
		assertEquals(3, results.size());
	}
}
