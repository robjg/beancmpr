package org.oddjob.beancmpr.comparers;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.composite.ComparerFactory;

import java.text.DecimalFormat;

/**
 * @oddjob.description A Comparer for numbers that supports tolerances and provides
 * the comparison as a difference between the numbers and as a percentage 
 * change between numbers.
 * <p>
 * The change is considered as going from x to y. If x is 200 and y is 190, the
 * delta is -10, and the percentage change is 5%.
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

	/**
	 * @oddjob.property
	 * @oddjob.description The absolute difference to allow for two numbers
	 * to still be considered equal.
	 * @oddjob.required No. Defaults to 0.
	 */
	private volatile double deltaTolerance;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The decimal format to use for reporting a 
	 * difference.
	 * @oddjob.required No.
	 */
	private volatile String deltaFormat;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The difference specified as percentage to allow
	 * for two numbers to still be considered equal.
	 * @oddjob.required No. Defaults to 0.
	 */
	private volatile double percentageTolerance;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The decimal format to use for reporting a 
	 * percentage difference.
	 * @oddjob.required No.
	 */
	private volatile String percentageFormat;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Treat two Not a Numbers as being equal when true.
	 * @oddjob.required No. Defaults to false.
	 */
	private volatile boolean twoNaNsEqual;

	public NumericComparer() {}

	private NumericComparer(Options options) {
		this.deltaTolerance = options.deltaTolerance;
		this.deltaFormat = options.deltaFormat;
		this.percentageFormat = options.percentageFormat;
		this.percentageTolerance = options.percentageTolerance;
		this.twoNaNsEqual = options.twoNaNsEqual;
	}

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
	
	/**
	 * @oddjob.property type
	 * @oddjob.description The base class this is a comparer for. 
	 * Used internally.
	 * 
	 */
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

	public static Options with() {
		return new Options();
	}
	public static class Options {

		private double deltaTolerance;

		private String deltaFormat;

		private double percentageTolerance;

		private String percentageFormat;

		private boolean twoNaNsEqual;

		public Options deltaTolerance(double deltaTolerance) {
			this.deltaTolerance = deltaTolerance;
			return this;
		}

		public Options deltaFormat(String deltaFormat) {
			this.deltaFormat = deltaFormat;
			return this;
		}

		public Options percentageTolerance(double percentageTolerance) {
			this.percentageTolerance = percentageTolerance;
			return this;
		}

		public Options percentageFormat(String percentageFormat) {
			this.percentageFormat = percentageFormat;
			return this;
		}

		public Options twoNaNsEqual(boolean twoNaNsEqual) {
			this.twoNaNsEqual = twoNaNsEqual;
			return this;
		}

		public ComparerFactory<Number> toFactory() {

			return ignored -> toComparer();
		}

		public NumericComparer toComparer() {

			return new NumericComparer(this);
		}
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
						builder.append(delta);
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
	}

}
