package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.comparers.IterableComparison;

import junit.framework.TestCase;

public class MultiItemComparisonTest extends TestCase {

	private class Stats implements MultiItemComparisonCounts {
		
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
	
	public void testIsEqual() {
		
		IterableComparison<Object> test1 = 
				new IterableComparison<Object>(null, null, 
						new Stats(0, 0, 0, 3));
		
		assertEquals(0, test1.getResult());
		
		IterableComparison<Object> test2 = 
				new IterableComparison<Object>(null, null, 
						new Stats(1, 0, 0, 3));
		
		assertTrue(new Integer(test2.getResult()).toString(), 
				test2.getResult() > 0);
		
		IterableComparison<Object> test3 = 
				new IterableComparison<Object>(null, null, 
						new Stats(0, 1, 0, 3));
		
		assertTrue(test3.getResult() < 0);
		
		IterableComparison<Object> test4 = 
				new IterableComparison<Object>(null, null, 
						new Stats(0, 0, 1, 3));
		
		assertTrue(test4.getResult() != 0);		

	}
	
	public void testNumbers() {
		
		IterableComparison<Object> test = 
				new IterableComparison<Object>(null, null, 
						new Stats(1, 2, 3, 4));
		
		assertEquals(1, test.getXMissingCount());
		assertEquals(2, test.getYMissingCount());
		assertEquals(3, test.getDifferentCount());
		assertEquals(4, test.getMatchedCount());	
		assertEquals(6, test.getBreaksCount());	
		assertEquals(10, test.getComparedCount());	
	}
}
