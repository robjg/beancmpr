package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;
import org.oddjob.arooa.utils.Iterables;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.comparers.NumericComparison;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

import java.math.BigInteger;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchableComparerFactoryTest extends TestCase {


    static List<Class<?>> classList(Class<?>... classes) {
        List<Class<?>> classList = new ArrayList<>();
        Collections.addAll(classList, classes);
        return classList;
    }

    @Test
    void testCompareEqual() {

        MatchableComparerFactory test = new MatchableComparerFactory(
                new ComparersByNameOrType());

        MatchableMetaData metaData = SimpleMatchableMeta.builder()
                .addValue("fruit", String.class)
                .addValue("colour", String.class)
                .build();

        Matchable x = mock(Matchable.class);
        when(x.getValues()).thenReturn(ImmutableCollection.of("apple", "red"));
        when(x.getMetaData()).thenReturn(metaData);

        Matchable y = mock(Matchable.class);
        when(y.getValues()).thenReturn(ImmutableCollection.of("apple", "red"));
        when(y.getMetaData()).thenReturn(metaData);


        MatchableComparer comparer = test.createComparerFor(
                metaData, metaData);

        MultiValueComparison<Matchable> result = comparer.compare(x, y);

        assertEquals(0, result.getResult());

        Comparison<?>[] comparisons = Iterables.toArray(
                result.getValueComparisons(), Comparison.class);

        assertEquals(2, comparisons.length);

        assertEquals(0, comparisons[0].getResult());
        assertEquals(0, comparisons[1].getResult());
    }

    @Test
    void testCompareNotEqual() {

        MatchableComparerFactory test = new MatchableComparerFactory(
                new ComparersByNameOrType());

        MatchableMetaData metaData = SimpleMatchableMeta.builder()
                .addValue("fruit", String.class)
                .addValue("colour", String.class)
                .build();

        Matchable x = mock(Matchable.class);
        when(x.getValues()).thenReturn(ImmutableCollection.of("apple", "red"));
        when(x.getMetaData()).thenReturn(metaData);

        Matchable y = mock(Matchable.class);
        when(y.getValues()).thenReturn(ImmutableCollection.of("apple", "green"));
        when(y.getMetaData()).thenReturn(metaData);

        MatchableComparer comparer = test.createComparerFor(
                metaData, metaData);

        MultiValueComparison<Matchable> result = comparer.compare(x, y);

        assertTrue(result.getResult() > 0);

        Comparison<?>[] comparisons = Iterables.toArray(result.getValueComparisons(),
                Comparison.class);

        assertEquals(2, comparisons.length);

        assertEquals(0, comparisons[0].getResult());
        assertTrue(comparisons[1].getResult() > 0);
    }

    @Test
    void testEqualDifferentTypes() {

        MatchableComparerFactory test = new MatchableComparerFactory(
                new ComparersByNameOrType());

        MatchableMetaData xMetaData = SimpleMatchableMeta.builder()
                .addValue("fruit", String.class)
                .addValue("quantity", BigInteger.class)
                .build();

        MatchableMetaData yMetaData = SimpleMatchableMeta.builder()
                .addValue("fruit", String.class)
                .addValue("quantity", BigInteger.class)
                .build();

        Matchable x = mock(Matchable.class);
        when(x.getValues()).thenReturn(ImmutableCollection.of("apple", new BigInteger("42")));
        when(x.getMetaData()).thenReturn(xMetaData);

        Matchable y = mock(Matchable.class);
        when(y.getValues()).thenReturn(ImmutableCollection.of("apple", 42));
        when(y.getMetaData()).thenReturn(yMetaData);

        MatchableComparer comparer = test.createComparerFor(
                xMetaData, yMetaData);

        MultiValueComparison<Matchable> result = comparer.compare(x, y);

        assertEquals(0, result.getResult());

        Comparison<?>[] comparisons = Iterables.toArray(
                result.getValueComparisons(), Comparison.class);

        assertEquals(2, comparisons.length);

        assertEquals(0, comparisons[0].getResult());
        assertEquals(0, comparisons[1].getResult());

        assertThat(comparisons[1], instanceOf(NumericComparison.class));
    }
}
