package org.oddjob.beancmpr.comparers;

import java.text.DecimalFormat;

import org.oddjob.beancmpr.Comparer;

/**
 * A {@link Comparer} for numbers that supports tolerances and provides
 * the comparison as a difference between the numbers and as a percentage 
 * change between numbers.
 * <p>
 * The change is considered as going from x to y. If x is 200 and y is 190, the
 * delta is -10, and the percentage chnage is 5%.
 * <p>
 * Two numbers are only considered different if their delta is more
 * the given tolerance or more. The tolerance may be specified as either a 
 * delta tolerance number that two numbers must differ more than, 
 * or a minimum percentage change that the two number must exceed.
 * <p>
 * The comparison is only unequal if the difference between the two numbers
 * is greater than both tolerances. Both tolerances default to zero.
 * <p>
 * If either or both input number is null, the result of the compare is null.
 * 
 * @author Rob
 *
 */
public class NumericComparer implements Comparer<Number> {

	private double deltaTolerance;
	
	private String deltaFormat;
	
	private double percentageTolerance;
	
	private String percentageFormat;
	
	public NumericComparison compare(final Number x, final Number y) {

		if (x == null || y == null) {
			return null;
		}
		
		double doubleX = x.doubleValue();
		double doubleY = y.doubleValue();

		final double delta = doubleY - doubleX;
		final double percentage;
		if (doubleX == 0) {
			percentage = 0;
		}
		else {
			percentage = delta /doubleX * 100; 
		}
		
		int result;
		if (delta == 0) {
			result = 0;
		}
		else if (delta > 0) {
			result = -1;
		}
		else {
			result = 1;
		}
		
		if (deltaTolerance > 0 && 
				Math.abs(delta) < deltaTolerance) {
			result = 0;
		}
		else if (percentageTolerance > 0 && 
					Math.abs(percentage) < percentageTolerance) {
			result = 0;
		}

		final int finalResult = result;
		
		StringBuilder builder = new StringBuilder();
		if (deltaFormat == null) {
			builder.append(String.valueOf(delta));
		}
		else {
			builder.append(new DecimalFormat(
					deltaFormat).format(delta));
		}
		builder.append(" (");
		if (percentageFormat == null) {
			percentageFormat = "0.0";
		}
		builder.append(new DecimalFormat(
				percentageFormat).format(percentage));
		builder.append("%)");

		final String summaryText = builder.toString();

		return new NumericComparison() {
			
			@Override
			public Number getX() {
				return x;
			}
			
			@Override
			public Number getY() {
				return y;
			}
			
			@Override
			public double getDelta() {
				return delta;
			}
			
			@Override
			public double getPercentage() {
				return percentage;
			}
			
			@Override
			public int getResult() {
				return finalResult;
			}
			
			@Override
			public String getSummaryText() {
				if (finalResult == 0) {
					return "";
				}
				else {
					return summaryText;
				}
			}
			
			@Override
			public String toString() {
				return "NumericComparison " + summaryText;
			}
		};
	}
	
	@Override
	public Class<Number> getType() {
		return Number.class;
	}
	
	public double getDeltaTolerance() {
		return deltaTolerance;
	}

	public void setDeltaTolerance(double deltaTolerance) {
		this.deltaTolerance = deltaTolerance;
	}

	public double getPercentageTolerance() {
		return percentageTolerance;
	}

	public void setPercentageTolerance(double percentageTolerance) {
		this.percentageTolerance = percentageTolerance;
	}
	
	public String getDeltaFormat() {
		return deltaFormat;
	}

	public void setDeltaFormat(String deltaFormat) {
		this.deltaFormat = deltaFormat;
	}

	public String getPercentageFormat() {
		return percentageFormat;
	}

	public void setPercentageFormat(String percentageFormat) {
		this.percentageFormat = percentageFormat;
	}
	
	public String toString() {
		return getClass().getName() + 
			(deltaTolerance > 0 ? ", +/-" + deltaTolerance : "" ) +
			(percentageTolerance > 0 ? ", " + percentageTolerance + "%" : ""); 
	}

}