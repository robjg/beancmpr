package org.oddjob.beancmpr.beans;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.comparers.EqualityComparison;
import org.oddjob.beancmpr.matchables.*;
import org.oddjob.beancmpr.results.AlternativeResultBeanFactory;

import java.util.List;

class AlternativeResultBeanFactoryTest extends TestCase {

	private static class MyMetaData extends MockMatchableMetaData {
		
		@Override
		public Iterable<String> getKeyProperties() {
			return List.of("fruit");
		}
		
		@Override
		public Iterable<String> getValueProperties() {
			return List.of("quantity");
		}
		
		@Override
		public Iterable<String> getOtherProperties() {
			return List.of("colour");
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
		
	@Test
	void testCreateResultNotEqual() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = 
				new AlternativeResultBeanFactory(
						accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				List.of("Apple"),
				List.of(2),
				List.of("red"),
				new MyMetaData());
		
		SimpleMatchable y = new SimpleMatchable(
				List.of("Apple"),
				List.of(3),
				List.of("green"),
				new MyMetaData());

		Comparison<Integer> comparison = new EqualityComparison<>(
				2, 3);
		
		MultiValueComparison<Matchable> matchableComparison =
			MatchableComparison.accumulatorFor(x, y)
					.add(comparison)
					.complete();

		Object bean = test.createComparisonResult(matchableComparison);
		
		assertEquals("NOT_EQUAL", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(2,
				accessor.getProperty(bean, "xQuantity"));
		
		assertEquals(3,
				accessor.getProperty(bean, "yQuantity"));
		
		assertSame(comparison, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "xColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "yColour"));
		
	}
	
	@Test
	void testCreateResultEqual() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = 
				new AlternativeResultBeanFactory(
						accessor, "left", "right");
		
		SimpleMatchable x = new SimpleMatchable(
				List.of("Apple"),
				List.of(2),
				List.of("red"),
				new MyMetaData());
		
		SimpleMatchable y = new SimpleMatchable(
				List.of("Apple"),
				List.of(2),
				List.of("green"),
				new MyMetaData());
		
		Comparison<Object> comparison =
				new EqualityComparison<>("A", "A");
		
		MultiValueComparison<Matchable> matchableComparison =
			MatchableComparison.accumulatorFor(x, y)
					.add(comparison)
					.complete();
		
		Object bean = test.createComparisonResult(matchableComparison);
		
		assertEquals("EQUAL", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(2,
				accessor.getProperty(bean, "leftQuantity"));
		
		assertEquals(2,
				accessor.getProperty(bean, "rightQuantity"));
		
		assertSame(comparison, 
				accessor.getProperty(bean, "quantityComparison"));
		
		assertEquals("red", 
				accessor.getProperty(bean, "leftColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "rightColour"));
		
	}
	
	@Test
	void testCreateResultXMissing() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = new AlternativeResultBeanFactory(
				accessor, "a", "b");
		
		SimpleMatchable y = new SimpleMatchable(
				List.of("Apple"),
				List.of(3),
				List.of("green"),
				new MyMetaData());
				
		Object bean = test.createXMissingResult(y);
		
		assertEquals("a_MISSING", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		MatcherAssert.assertThat(accessor.getProperty(bean, "aQuantity"),
				Matchers.nullValue());
		
		assertEquals(3,
				accessor.getProperty(bean, "bQuantity"));
		
		MatcherAssert.assertThat(accessor.getProperty(bean, "quantityComparison"),
				Matchers.nullValue());

		assertEquals(null,
				accessor.getProperty(bean, "aColour"));
		
		assertEquals("green", 
				accessor.getProperty(bean, "bColour"));
		
	}
	
	@Test
	void testCreateYMissing() {
		
		BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();
		
		AlternativeResultBeanFactory test = new AlternativeResultBeanFactory(
				accessor, null, null);
		
		SimpleMatchable x = new SimpleMatchable(
				List.of("Apple"),
				List.of(2),
				List.of("red"),
				new MyMetaData());
				
		Object bean = test.createYMissingResult(x);
		
		assertEquals("y_MISSING", 
				accessor.getProperty(bean, "matchResultType.text"));

		assertEquals("Apple", 
				accessor.getProperty(bean, "fruit"));
		
		assertEquals(2,
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
