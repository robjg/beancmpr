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

	private volatile double deltaTolerance;
	
	private volatile String deltaFormat;
	
	private volatile double percentageTolerance;
	
	private volatile String percentageFormat;
	
	private volatile boolean twoNaNsEqual;
	
	/*
	 * (non-Javadoc)
	 * @see org.oddjob.beancmpr.Comparer#compare(java.lang.Object, java.lang.Object)
	 */
	public NumericComparison compare(final Number x, final Number y) {

		if (x == null || y == null) {
			throw new NullPointerException("X or Y Null.");
		}
		
		double doubleX = x.doubleValue();
		double doubleY = y.doubleValue();

		double delta;
		double percentage;
		int result;
		
		if (doubleX == doubleY || (
				twoNaNsEqual && Double.isNaN(doubleX) 
				&& Double.isNaN(doubleY))) {
			delta = 0;
			percentage = 0.0;
			result = 0;
		}
		else {
			delta = doubleY - doubleX;
			
			percentage = 0.0;
			
			if (Math.abs(delta) <= deltaTolerance) {
				result = 0;
			}
			else {
			
				if (delta > 0.0) {
					result = -1;
				}
				else {
					result = 1;
				}
				
				percentage = delta /doubleX * 100; 
					
				if (percentageTolerance > 0.0 && 
						Math.abs(percentage) < percentageTolerance) {
					result = 0;
				}
			}
		}
			
		return new NumericComparisonImpl(x, y, result, delta, percentage);
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
	
	public boolean isTwoNaNsEqual() {
		return twoNaNsEqual;
	}

	public void setTwoNaNsEqual(boolean twoNaNsEqual) {
		this.twoNaNsEqual = twoNaNsEqual;
	}

	public String toString() {
		return getClass().getName() + 
			(deltaTolerance > 0 ? ", +/-" + deltaTolerance : "" ) +
			(percentageTolerance > 0 ? ", " + percentageTolerance + "%" : ""); 
	}

	/**
	 * Implementation of the {@link NumericComparison}.
	 */
	class NumericComparisonImpl implements NumericComparison {
		
		private final Number x;
		private final Number y;
		
		private final int finalResult;
		
		private final double delta;
		private final double percentage;
		
		private volatile String summaryText;
		
		public NumericComparisonImpl(Number x, Number y, int result,
				double delta, double percentage) {
			this.x = x;
			this.y = y;
			
			this.finalResult = result;
			
			this.delta = delta;
			this.percentage = percentage;
		}
		
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
			if (summaryText == null) {
				if (finalResult == 0) {
					summaryText = "";
				}
				else {
					StringBuilder builder = new StringBuilder();
					if (deltaFormat == null) {
						builder.append(String.valueOf(delta));
					}
					else {
						builder.append(new DecimalFormat(
								deltaFormat).format(delta));
					}
					
					if (!Double.isInfinite(percentage) 
							&& !Double.isNaN(percentage)) {
						builder.append(" (");
						if (percentageFormat == null) {
							percentageFormat = "0.0";
						}
						builder.append(new DecimalFormat(
								percentageFormat).format(percentage));
						builder.append("%)");
					}
					summaryText = builder.toString();
				}
			}			
			return summaryText;
		}
		
		@Override
		public String toString() {
			return "NumericComparison " + getSummaryText();
		}
	};

}
