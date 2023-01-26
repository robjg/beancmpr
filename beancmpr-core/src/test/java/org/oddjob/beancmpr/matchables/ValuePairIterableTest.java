package org.oddjob.beancmpr.matchables;

import junit.framework.TestCase;
import org.oddjob.arooa.utils.Iterables;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ValuePairIterableTest extends TestCase {

	public void testIterableOfIntegers() {
		
		ValuePairIterable<String, Integer> test =
				new ValuePairIterable<>(
						Arrays.asList("a", "b", "c"),
						Arrays.asList(1, 2, 3),
						Arrays.asList(7, 8, 9));
	
		ValuePairIterable.ValuePair<String, Integer>[] sets = 
			Iterables.toArray(test, ValuePairIterable.ValuePair.class);

		assertEquals(3, sets.length);
		
		assertEquals("a", sets[0].getCommon());
		assertThat(sets[0].getValueX(), is(1));
		assertThat(sets[0].getValueY(), is(7));
		
		assertEquals("b", sets[1].getCommon());
		assertThat(sets[1].getValueX(), is(2));
		assertThat(sets[1].getValueY(), is(8));
		
		assertEquals("c", sets[2].getCommon());
		assertThat(sets[2].getValueX(), is(3));
		assertThat(sets[2].getValueY(), is(9));
	}
	
	public void testWhenXisNull() {
		
		ValuePairIterable<String, Integer> test =
				new ValuePairIterable<>(
						Arrays.asList("a", "b", "c"),
						null,
						Arrays.asList(7, 8, 9));
	
		ValuePairIterable.ValuePair<String, Integer>[] sets = 
			Iterables.toArray(test, ValuePairIterable.ValuePair.class);

		assertEquals(3, sets.length);
		
		assertEquals("a", sets[0].getCommon());
		assertThat(sets[0].getValueX(), nullValue());
		assertThat(sets[0].getValueY(), is(7));
		
		assertEquals("b", sets[1].getCommon());
		assertThat(sets[1].getValueX(), nullValue());
		assertThat(sets[1].getValueY(), is(8));
		
		assertEquals("c", sets[2].getCommon());
		assertThat(sets[2].getValueX(), nullValue());
		assertThat(sets[2].getValueY(), is(9));
	}
	
	public void testWhenYisNull() {
		
		ValuePairIterable<String, Integer> test =
				new ValuePairIterable<>(
						Arrays.asList("a", "b", "c"),
						Arrays.asList(1, 2, 3),
						null);
	
		ValuePairIterable.ValuePair<String, Integer>[] sets = 
			Iterables.toArray(test, ValuePairIterable.ValuePair.class);

		assertEquals(3, sets.length);
		
		assertEquals("a", sets[0].getCommon());
		assertThat(sets[0].getValueX(), is(1));
		assertThat(sets[0].getValueY(), nullValue());
		
		assertEquals("b", sets[1].getCommon());
		assertThat(sets[1].getValueX(), is(2));
		assertThat(sets[1].getValueY(), nullValue());
		
		assertEquals("c", sets[2].getCommon());
		assertThat(sets[2].getValueX(), is(3));
		assertThat(sets[2].getValueY(), nullValue());
	}
}
