package org.oddjob.beancmpr.beans;

import java.util.Arrays;

import junit.framework.TestCase;

import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.EqualityComparison;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableComparision;
import org.oddjob.beancmpr.matchables.MockMatchableMetaData;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.matchables.SimpleMatchable;
import org.oddjob.beancmpr.results.AlternativeResultBeanFactory;

public class AlternativeResultBeanFactoryTest extends TestCase {

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
		
		AlternativeResultBeanFactory test = 
				new AlternativeResultBeanFactory(
						accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("red"),
				new MyMetaData());
		
		SimpleMatchable y = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(3)),
				Arrays.asList("green"),
				new MyMetaData());

		Comparison<Integer> comparison = new EqualityComparison<Integer>(
				new Integer(2), new Integer(3));
		
		Iterable<? extends Comparison<?>> comparisons =
				Arrays.asList(comparison);
		
		MultiValueComparison<Matchable> matchableComparison = 
			new MatchableComparision(x, y, comparisons);
		
		Object bean = test.createComparisonResult(matchableComparison);
		
		assertEquals("NOT_EQUAL", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "xQuantity"));
		
		assertEquals(new Integer(3), 
				accessor.getProperty(bean, "yQuantity"));
		
		assertSame(comparison, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "xColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "yColour"));
		
	}
	
	public void testCreateResultEqual() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = 
				new AlternativeResultBeanFactory(
						accessor, "left", "right");
		
		SimpleMatchable x = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("red"),
				new MyMetaData());
		
		SimpleMatchable y = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("green"),
				new MyMetaData());
		
		Comparison<Object> comparison = 
				new EqualityComparison<Object>("A", "A");
		
		Iterable<? extends Comparison<?>> comparisons =
				Arrays.asList(comparison);
		
		MultiValueComparison<Matchable> matchableComparison = 
			new MatchableComparision(x, y, comparisons);
		
		Object bean = test.createComparisonResult(matchableComparison);
		
		assertEquals("EQUAL", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "leftQuantity"));
		
		assertEquals(new Integer(2), 
				accessor.getProperty(bean, "rightQuantity"));
		
		assertSame(comparison, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "leftColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "rightColour"));
		
	}
	
	public void testCreateResultXMissing() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = new AlternativeResultBeanFactory(
				accessor, "a", "b");
		
		SimpleMatchable y = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(3)),
				Arrays.asList("green"),
				new MyMetaData());
				
		Object bean = test.createXMissingResult(y);
		
		assertEquals("a_MISSING", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "aQuantity"));
		
		assertEquals(new Integer(3), 
				accessor.getProperty(bean, "bQuantity"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals(null, 
				accessor.getProperty(bean, "aColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "bColour"));
		
	}
	
	public void testCreateYMissing() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = new AlternativeResultBeanFactory(
				accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				Arrays.asList("Apple"), 
				Arrays.asList(new Integer(2)),
				Arrays.asList("red"),
				new MyMetaData());
				
		Object bean = test.createYMissingResult(x);
		
		assertEquals("y_MISSING", 
				accessor.getProperty(bean, "matchResultType.text"));

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
