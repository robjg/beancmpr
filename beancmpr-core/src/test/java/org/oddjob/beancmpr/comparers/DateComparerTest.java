package org.oddjob.beancmpr.comparers;

import org.junit.jupiter.api.Test;
import org.oddjob.arooa.utils.DateHelper;
import org.oddjob.beancmpr.Comparison;
import org.oddjob.beancmpr.TestCase;

import java.text.ParseException;
import java.util.Date;

class DateComparerTest extends TestCase {

	@Test
	void testSame() throws ParseException {
		
		java.sql.Date x = new java.sql.Date(
				DateHelper.parseDateTime("2012-04-22 18:53").getTime());
		
		Date y = DateHelper.parseDateTime("2012-04-22 18:53");
		
		DateComparer test = new DateComparer();
		
		Comparison<Date> result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
		assertSame(x, result.getX());		
		assertSame(y, result.getY());		
		assertEquals("", result.getSummaryText());		
		
	}
	
	@Test
	void testSameWithinTolerance() throws ParseException {
		
		java.sql.Date x = new java.sql.Date(
				DateHelper.parseDateTime("2012-04-22 18:53").getTime());
		
		Date y = DateHelper.parseDateTime("2012-04-22 18:52");
		
		DateComparer test = new DateComparer();
		test.setTolerance(60000);
		
		Comparison<Date> result = test.compare(x, y);
		
		assertEquals(0, result.getResult());
		assertSame(x, result.getX());		
		assertSame(y, result.getY());		
		assertEquals("", result.getSummaryText());		
		
	}
	
	@Test
	void testDifferent() throws ParseException {
		
		java.sql.Date x = new java.sql.Date(
				DateHelper.parseDateTime("2012-04-22 18:53").getTime());
		
		Date y = DateHelper.parseDateTime("2012-04-22 18:51");
		
		DateComparer test = new DateComparer();
		test.setTolerance(60000);
		
		Comparison<Date> result = test.compare(x, y);
		
		assertEquals(1, result.getResult());
		assertSame(x, result.getX());		
		assertSame(y, result.getY());		
		assertEquals("2012-04-22 18:53:00.000<>2012-04-22 18:51:00.000", 
				result.getSummaryText());		
		
	}
}
