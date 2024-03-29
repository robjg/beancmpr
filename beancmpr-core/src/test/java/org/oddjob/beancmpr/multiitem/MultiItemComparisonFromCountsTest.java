package org.oddjob.beancmpr.multiitem;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.oddjob.beancmpr.TestCase;

import static org.hamcrest.MatcherAssert.assertThat;

class MultiItemComparisonFromCountsTest extends TestCase {

	private static class Stats implements MultiItemComparisonCounts {
		
		private final int xsMissing;
		
		private final int ysMissing;

		private final int different;
		
		private final int same;
			
		public Stats(
				int xsMissing,
				int ysMissing,
				int different,
				int same) {
			
			this.xsMissing = xsMissing;
			this.ysMissing = ysMissing;
			this.different = different;
			this.same = same;
		}
		
		@Override
		public int getXMissingCount() {
			return xsMissing;
		}
		
		@Override
		public int getYMissingCount() {
			return ysMissing;
		}
		
		@Override
		public int getDifferentCount() {
			return different;
		}
		
		@Override
		public int getMatchedCount() {
			return same;
		}
		
		@Override
		public int getBreaksCount() {
			return xsMissing + ysMissing + different;
		}
		
		@Override
		public int getComparedCount() {
			return xsMissing + ysMissing + different + same;
		}
		
		
	}
	
	@Test
	void testIsEqual() {
		
		MultiItemComparison<Object> test1 =
				new MultiItemComparisonFromCounts<>(null, null,
						new Stats(0, 0, 0, 3));
		
		assertEquals(0, test1.getResult());
		
		MultiItemComparison<Object> test2 =
				new MultiItemComparisonFromCounts<>(null, null,
						new Stats(1, 0, 0, 3));

		assertThat(test2.getResult(), Matchers.greaterThan(0));
		
		MultiItemComparison<Object> test3 =
				new MultiItemComparisonFromCounts<>(null, null,
						new Stats(0, 1, 0, 3));
		
		assertTrue("Result is "+ test3.getResult(), test3.getResult() != 0);
		
		MultiItemComparison<Object> test4 =
				new MultiItemComparisonFromCounts<>(null, null,
						new Stats(0, 0, 1, 3));
		
		assertTrue(test4.getResult() != 0);		

	}
	
	@Test
	void testNumbers() {
		
		MultiItemComparison<Object> test =
				new MultiItemComparisonFromCounts<>(null, null,
						new Stats(1, 2, 3, 4));
		
		assertEquals(1, test.getXMissingCount());
		assertEquals(2, test.getYMissingCount());
		assertEquals(3, test.getDifferentCount());
		assertEquals(4, test.getMatchedCount());	
		assertEquals(6, test.getBreaksCount());	
		assertEquals(10, test.getComparedCount());	
	}
}
