package org.oddjob.beancmpr.matchables;

import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;
import org.oddjob.beancmpr.composite.ComparersByNameOrType;

import java.math.BigInteger;
import java.util.Comparator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KeyComparatorFactoryTest extends TestCase {

	@Test
	void testCompareEqual() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());
				
		MatchableMetaData metaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("colour", String.class)
				.build();

		Matchable x = mock(Matchable.class);
		when(x.getKeys()).thenReturn(ImmutableCollection.of("apple", "red"));
		when(x.getMetaData()).thenReturn(metaData);

		Matchable y = mock(Matchable.class);
		when(y.getKeys()).thenReturn(ImmutableCollection.of("apple", "red"));
		when(y.getMetaData()).thenReturn(metaData);
		
		Comparator<Iterable<?>> comparer = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparer.compare(x.getKeys(), y.getKeys());
		
		assertEquals(0, result);
	}
	
	@Test
	void testCompareNotEqual() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());

		MatchableMetaData metaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("colour", String.class)
				.build();

		Matchable x = mock(Matchable.class);
		when(x.getKeys()).thenReturn(ImmutableCollection.of("apple", "red"));
		when(x.getMetaData()).thenReturn(metaData);

		Matchable y = mock(Matchable.class);
		when(y.getKeys()).thenReturn(ImmutableCollection.of("apple", "green"));
		when(y.getMetaData()).thenReturn(metaData);


		Comparator<Iterable<?>> comparator = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertTrue(result > 0);
	}
	
	@Test
	void testEqualDifferentTypes() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());

		MatchableMetaData xMetaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("quantity", BigInteger.class)
				.build();
		
		MatchableMetaData yMetaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("quantity", Integer.class)
				.build();

		Matchable x = mock(Matchable.class);
		when(x.getKeys()).thenReturn(ImmutableCollection.of("apple", new BigInteger("42")));
		when(x.getMetaData()).thenReturn(xMetaData);

		Matchable y = mock(Matchable.class);
		when(y.getKeys()).thenReturn(ImmutableCollection.of("apple", 42));
		when(y.getMetaData()).thenReturn(yMetaData);

		Comparator<Iterable<?>> comparator = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertEquals(0, result);
		
	}
	
	@Test
	void testCompareOneSideNullComponent() {
		
		KeyComparatorFactory test = new KeyComparatorFactory(
				new ComparersByNameOrType());

		MatchableMetaData xMetaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("quantity", BigInteger.class)
				.build();

		MatchableMetaData yMetaData = SimpleMatchableMeta.builder()
				.addKey("fruit", String.class)
				.addKey("quantity", BigInteger.class)
				.build();

		Matchable x = mock(Matchable.class);
		when(x.getKeys()).thenReturn(ImmutableCollection.of("apple", new BigInteger("42")));
		when(x.getMetaData()).thenReturn(xMetaData);

		Matchable y = mock(Matchable.class);
		when(y.getKeys()).thenReturn(ImmutableCollection.of("apple", null));
		when(y.getMetaData()).thenReturn(yMetaData);

		Comparator<Iterable<?>> comparator = test.createComparatorFor(
				x.getMetaData(), y.getMetaData());
		
		int result = comparator.compare(x.getKeys(), y.getKeys());
		
		assertThat(result, greaterThan(0));
		
		result = comparator.compare(y.getKeys(), x.getKeys());

		assertThat(result, lessThan(0));
	}
}
