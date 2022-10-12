package org.oddjob.beancmpr.beans;

import junit.framework.TestCase;
import org.oddjob.arooa.beanutils.BeanUtilsPropertyAccessor;
import org.oddjob.beancmpr.comparers.EqualityComparison;
import org.oddjob.beancmpr.matchables.*;
import org.oddjob.beancmpr.results.SimpleResultBeanFactory;

import java.util.List;

public class SimpleResultBeanFactoryTest extends TestCase {

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

    public void testCreateResultNotEqual() {

        BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();

        SimpleResultBeanFactory test = new SimpleResultBeanFactory(
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

        MultiValueComparison<Matchable> matchableComparison =
                MatchableComparison.accumulatorFor(x, y)
                        .add(new EqualityComparison<>(2, 3))
                        .complete();

        Object bean = test.createComparisonResult(matchableComparison);

        assertEquals("NOT_EQUAL",
                accessor.getProperty(bean, "matchResultType"));

        assertEquals("Apple",
                accessor.getProperty(bean, "fruit"));

        assertEquals(2,
                accessor.getProperty(bean, "xQuantity"));

        assertEquals(3,
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
                List.of("Apple"),
                List.of(2),
                List.of("red"),
                new MyMetaData());

        SimpleMatchable y = new SimpleMatchable(
                List.of("Apple"),
                List.of(2),
                List.of("green"),
                new MyMetaData());

        MultiValueComparison<Matchable> matchableComparison =
                MatchableComparison.accumulatorFor(x, y)
                        .add(new EqualityComparison<Object>("A", "A"))
                        .complete();

        Object bean = test.createComparisonResult(matchableComparison);

        assertEquals("EQUAL",
                accessor.getProperty(bean, "matchResultType"));

        assertEquals("Apple",
                accessor.getProperty(bean, "fruit"));

        assertEquals(2,
                accessor.getProperty(bean, "xQuantity"));

        assertEquals(2,
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
                accessor, "TODAY", "YESTERDAY");

        SimpleMatchable y = new SimpleMatchable(
                List.of("Apple"),
                List.of(3),
                List.of("green"),
                new MyMetaData());

        Object bean = test.createXMissingResult(y);

        assertEquals("TODAY_MISSING",
                accessor.getProperty(bean, "matchResultType"));

        assertEquals("Apple",
                accessor.getProperty(bean, "fruit"));

        assertEquals(null,
                accessor.getProperty(bean, "TODAYQuantity"));

        assertEquals(3,
                accessor.getProperty(bean, "YESTERDAYQuantity"));

        assertEquals(null,
                accessor.getProperty(bean, "quantityComparison"));

        assertEquals(null,
                accessor.getProperty(bean, "TODAYColour"));

        assertEquals("green",
                accessor.getProperty(bean, "YESTERDAYColour"));

    }

    public void testCreateYMissing() {

        BeanUtilsPropertyAccessor accessor = new BeanUtilsPropertyAccessor();

        SimpleResultBeanFactory test = new SimpleResultBeanFactory(
                accessor, null, null);

        SimpleMatchable x = new SimpleMatchable(
                List.of("Apple"),
                List.of(2),
                List.of("red"),
                new MyMetaData());

        Object bean = test.createYMissingResult(x);

        assertEquals("y_MISSING",
                accessor.getProperty(bean, "matchResultType"));

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
