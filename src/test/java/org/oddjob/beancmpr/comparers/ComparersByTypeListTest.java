package org.oddjob.beancmpr.comparers;

import junit.framework.TestCase;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.comparers.ComparersByTypeList;

public class ComparersByTypeListTest extends TestCase {

	private class MyType {
		
	}
	
	private class MyComparer implements Comparer<MyType> {
		
		@Override
		public Comparison compare(MyType x, MyType y) {
			throw new RuntimeException("Unexepected");
		}
		
		@Override
		public Class<?> getType() {
			return MyType.class;
		}
	}
	
	public void testFindByType() {
		
		ComparersByTypeList test = new ComparersByTypeList();
		
		MyComparer comparer = new MyComparer();
		
		test.setComparer(0, comparer);
		
		assertSame(null, test.comparerFor(Object.class));
		assertSame(comparer, test.comparerFor(MyType.class));
		assertSame(null, test.comparerFor(String.class));
	}
}
