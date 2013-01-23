package org.oddjob.beancmpr.beans;

import java.util.Arrays;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.SimpleComparison;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparision;
import org.oddjob.beancmpr.matchables.MockMatchableMetaData;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.matchables.SimpleMatchable;

public class SimpleResultBeanFactoryTest extends TestCase {

	private class MyMetaData extends MockMatchableMetaData {
		
		@Override
		public Iterable<String> getKeyProperties() {
			return Arrays.asList("fruit");
		}
		
		@Override
		public Iterable<String> getValueProperties() {
			return Arrays.asList("quantity");
		}
		
		@Override
		public Iterable<String> getOtherProperties() {
			return Arrays.asList("colour");
		}
		
		@Override
		public Class<?> getPropertyType(String name) {
			if ("fruit".equals(name)) {
				return String.class;
			}
			if ("quantity".equals(name)) {
				return Integer.class;
			}
			if ("colour".equals(name)) {
				return String.class;
			}
			throw new IllegalArgumentException(name);
		}
	}
		
	public void testCreateResultNotEqual() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		SimpleResultBeanFactory test = new SimpleResultBeanFactory(
				accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("red"));
		x.setMetaData(new MyMetaData());
		
		SimpleMatchable y = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(3)),
				Arrays.asList("green"));

		@SuppressWarnings("unchecked")
		Iterable<? extends Comparison<?>> comparisons =
				Arrays.asList(new SimpleComparison<Integer>(
						new Integer(2), new Integer(3)));
		
		MultiValueComparison<Matchable> matchableComparison = 
			new MatchableComparision(x, y, comparisons);
		
		Object bean = test.createComparisonResult(matchableComparison);
		
		assertEquals(MatchResultType.NOT_EQUAL, 
				accessor.getProperty(bean, "matchResultType"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "xQuantity"));
		
		assertEquals(new Integer(3), 
				accessor.getProperty(bean, "yQuantity"));
		
		assertEquals("2<>3", 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "xColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "yColour"));
		
	}
	
	public void testCreateResultEqual() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		SimpleResultBeanFactory test = new SimpleResultBeanFactory(
				accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("red"));
		x.setMetaData(new MyMetaData());
		
		SimpleMatchable y = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("green"));
		
		@SuppressWarnings("unchecked")
		Iterable<? extends Comparison<?>> comparisons =
				Arrays.asList(new SimpleComparison<Object>("A", "A"));
		
		MultiValueComparison<Matchable> matchableComparison = 
			new MatchableComparision(x, y, comparisons);
		
		Object bean = test.createComparisonResult(matchableComparison);
		
		assertEquals(MatchResultType.EQUAL, 
				accessor.getProperty(bean, "matchResultType"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "xQuantity"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "yQuantity"));
		
		assertEquals("", 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "xColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "yColour"));
		
	}
	
	public void testCreateResultXMissing() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		SimpleResultBeanFactory test = new SimpleResultBeanFactory(
				accessor, null, null);
		
		SimpleMatchable y = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(3)),
				Arrays.asList("green"));
		y.setMetaData(new MyMetaData());
				
		Object bean = test.createXMissingResult(y);
		
		assertEquals(MatchResultType.X_MISSING, 
				accessor.getProperty(bean, "matchResultType"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "xQuantity"));
		
		assertEquals(new Integer(3), 
				accessor.getProperty(bean, "yQuantity"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "xColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "yColour"));
		
	}
	
	public void testCreateYMissing() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		SimpleResultBeanFactory test = new SimpleResultBeanFactory(
				accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("red"));
		x.setMetaData(new MyMetaData());
				
		Object bean = test.createYMissingResult(x);
		
		assertEquals(MatchResultType.Y_MISSING, 
				accessor.getProperty(bean, "matchResultType"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "xQuantity"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "yQuantity"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "xColour"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "yColour"));
		
	}
}
