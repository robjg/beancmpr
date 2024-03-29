package org.oddjob.beancmpr.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.standard.StandardArooaSession;
import org.oddjob.beancmpr.BeanCompareJob;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.jobs.BeanReportJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ResultFactoryDifferentTypesTest extends TestCase {

	private static final Logger logger = LoggerFactory.getLogger(
			ResultFactoryDifferentTypesTest.class);

	@BeforeEach
	void init(TestInfo testInfo) {

		logger.debug("========================== " + testInfo.getDisplayName() + "===================" );
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
	
	@Test void testComparisonFirst() {
		
		List<Object> bean1s = Arrays.asList(new Object[] {
				new Bean1(1, 2.5),
				new Bean1(2, 3.5) });
		
		List<Object> bean2s = Arrays.asList(new Object[] {
				new Bean2(1, BigDecimal.valueOf(2.6)),
				new Bean2(3, BigDecimal.valueOf(4.5)) });
		
		List<Object> results = new ArrayList<>();
				
		ArooaSession session = new StandardArooaSession();
		
		IterableBeansComparerType<Object> iterableComparer = 
				new IterableBeansComparerType<>();
				
		iterableComparer.setArooaSession(session);
		iterableComparer.setKeys(new String[] {"id"});
		iterableComparer.setValues(new String[] {"quantity"});
		
		BeanCompareJob<Iterable<Object>> comparer = new BeanCompareJob<>();
		comparer.setArooaSession(session);
		comparer.acceptDestination(results::add);
		comparer.setInX(bean1s);
		comparer.setInY(bean2s);
		comparer.setComparer(iterableComparer);
		
		comparer.run();
		
		assertEquals(3, results.size());
	}
	
	@Test
	void testXMissingFirst() {
		
		List<Object> bean1s = Arrays.asList(new Object[] {
				new Bean1(2, 2.5),
				new Bean1(3, 3.5) });
		
		List<Object> bean2s = Arrays.asList(new Object[] {
				new Bean2(1, BigDecimal.valueOf(2.6)),
				new Bean2(3, BigDecimal.valueOf(4.5)) });
		
		List<Object> results = new ArrayList<>();
				
		ArooaSession session = new StandardArooaSession();
		
		IterableBeansComparerType<Object> iterableComparer = 
				new IterableBeansComparerType<>();
				
		iterableComparer.setArooaSession(session);
		iterableComparer.setKeys(new String[] {"id"});
		iterableComparer.setValues(new String[] {"quantity"});
				
		BeanCompareJob<Iterable<Object>> comparer = new BeanCompareJob<>();
		comparer.setArooaSession(session);
		comparer.acceptDestination(results::add);
		comparer.setInX(bean1s);
		comparer.setInY(bean2s);
		comparer.setComparer(iterableComparer);
		
		comparer.run();
		
		assertEquals(3, results.size());
	}
	
	@Test
	void testYMissingFirst() {
		
		List<Object> bean1s = Arrays.asList(new Object[] {
				new Bean1(1, 2.5),
				new Bean1(3, 3.5) });
		
		List<Object> bean2s = Arrays.asList(new Object[] {
				new Bean2(2, BigDecimal.valueOf(2.6)),
				new Bean2(3, BigDecimal.valueOf(4.5)) });
		
		List<Object> results = new ArrayList<>();
				
		ArooaSession session = new StandardArooaSession();
		
		IterableBeansComparerType<Object> iterableComparer = 
				new IterableBeansComparerType<>();

		iterableComparer.setArooaSession(session);
		iterableComparer.setKeys(new String[] {"id"});
		iterableComparer.setValues(new String[] {"quantity"});
				
		BeanCompareJob<Iterable<Object>> comparer = new BeanCompareJob<>();
		comparer.setArooaSession(session);
		comparer.acceptDestination(results::add);
		comparer.setInX(bean1s);
		comparer.setInY(bean2s);
		comparer.setComparer(iterableComparer);
		
		comparer.run();
		
		BeanReportJob report = new BeanReportJob();
		report.setArooaSession(session);
		report.setBeans(results);
		report.run();
		
		assertEquals(3, results.size());
	}
}
