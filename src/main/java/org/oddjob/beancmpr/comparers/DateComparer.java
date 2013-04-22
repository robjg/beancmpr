package org.oddjob.beancmpr.comparers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

public class DateComparer implements Comparer<Date>{

	private long tolerance;
	
	private String dateFormat;
	
	@Override
	public Class<?> getType() {
		return Date.class;
	}
	
	@Override
	public Comparison<Date> compare(final Date x, final Date y) {
		
		if (x == null || y == null) {
			return null;
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
