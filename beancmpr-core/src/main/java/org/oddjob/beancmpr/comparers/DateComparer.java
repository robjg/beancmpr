package org.oddjob.beancmpr.comparers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * @oddjob.description A Comparer that will allow a comparison between
 * two Java dates with a millisecond tolerance.
 * 
 * @author rob
 *
 */
public class DateComparer implements Comparer<Date> {

	/**
	 * @oddjob.property
	 * @oddjob.description The difference as a number of milliseconds that
	 * can exist between two dates for them still to be considered equal.
	 * @oddjob.required No. Default to 0.
	 */
	private long tolerance;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The format for the date and time when reporting a 
	 * date difference.
	 * @oddjob.required No.
	 */
	private String dateFormat;
	
	/**
	 * @oddjob.property type
	 * @oddjob.description The base class this is a comparer for. 
	 * Used internally.
	 * 
	 */
	@Override
	public Class<?> getType() {
		return Date.class;
	}
	
	@Override
	public Comparison<Date> compare(final Date x, final Date y) {
		
		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
		
		long delta = x.getTime() - y.getTime();

		final int result;
		
		if (Math.abs(delta) <= tolerance) {
			result = 0;
		}
		else if (delta < 0){
			result = -1;
		}
		else {
			result = 1;
		}
		
		return new Comparison<Date>() {
			
			@Override
			public int getResult() {
				return result;
			}
			
			@Override
			public Date getX() {
				return x;
			}
			
			@Override
			public Date getY() {
				return y;
			}
			
			@Override
			public String getSummaryText() {
				if (result == 0) {
					return "";
				}
				
				String dateFormat = DateComparer.this.dateFormat;
				if (dateFormat == null) {
					dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				
				return sdf.format(x) + "<>" + sdf.format(y);
			}
		};
	}


	public long getTolerance() {
		return tolerance;
	}

	public void setTolerance(long tolerance) {
		this.tolerance = tolerance;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String toString() {
		return getClass().getName() + 
			(tolerance > 0 ? ", +/-" + tolerance + "ms": "" ); 
	}

}
