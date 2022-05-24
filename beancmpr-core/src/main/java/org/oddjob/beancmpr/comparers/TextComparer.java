package org.oddjob.beancmpr.comparers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.Comparison;

/**
 * @oddjob.description A comparer for text. This comparer allows a 
 * comparison ignoring whitespace and or case.
 * 
 * @author rob
 */
public class TextComparer implements Comparer<String> {
	
	private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
	
	/**
	 * @oddjob.property
	 * @oddjob.description Ignore the case of the text during a comparison
	 * when true.
	 * @oddjob.required No. Default to false.
	 */
	private boolean ignoreCase;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Ignore leading or trailing whitespace during a 
	 * comparison when true.
	 * @oddjob.required No. Default to false.
	 */
	private boolean ignoreWhitespace;
	
	@Override
	public Comparison<String> compare(final String x, final String y) {

		if (x == null || y == null) {
			throw new NullPointerException("X or Y is null.");
		}
		
		String xCleaned = x;
		String yCleaned = y;
		
		if (ignoreWhitespace) {
			xCleaned = xCleaned.trim();
			Matcher xMatch = WHITESPACE_PATTERN.matcher(xCleaned);
			xCleaned = xMatch.replaceAll(" ");

			yCleaned = yCleaned.trim();
			Matcher yMatch = WHITESPACE_PATTERN.matcher(yCleaned);
			yCleaned = yMatch.replaceAll(" ");
		}
		
		if (ignoreCase) {
			xCleaned = xCleaned.toUpperCase();
			yCleaned = yCleaned.toUpperCase();
		}
		
		final int result = xCleaned.compareTo(yCleaned);
		final String summary = result == 0 ? "" : x + "<>" + y;
		
		return new Comparison<String>() {

			@Override
			public int getResult() {
				return result;
			}
			
			@Override
			public String getSummaryText() {
				return summary;
			}
			
			@Override
			public String getX() {
				return x;
			}
			
			@Override
			public String getY() {
				return y;
			}
		};
	}

	/**
	 * @oddjob.property type
	 * @oddjob.description The base class this is a comparer for. 
	 * Used internally.
	 * 
	 */
	@Override
	public Class<?> getType() {
		return String.class;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean isIgnoreWhitespace() {
		return ignoreWhitespace;
	}

	public void setIgnoreWhitespace(boolean ignoreWhiteSpace) {
		this.ignoreWhitespace = ignoreWhiteSpace;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ", ignoreCase=" + ignoreCase +
				", ignoreWhitespace=" + ignoreWhitespace;
	}
}
