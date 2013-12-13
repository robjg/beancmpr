package org.oddjob.beancmpr;

/**
 * Used to detect that a stop has been requested via an interrupt.
 * 
 * @author rob
 *
 */
public class ComparisonStoppedException extends RuntimeException {
	private static final long serialVersionUID = 2013120900L;
	
	public ComparisonStoppedException() {
	}
	
	public ComparisonStoppedException(String message) {
		super(message);
	}

}
