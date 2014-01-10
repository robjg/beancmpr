package org.oddjob.beancmpr;

import java.util.Arrays;

import org.oddjob.arooa.utils.Iterables;

/**
 * Simple definition of an {@link MatchDefinition}.
 * 
 * @author rob
 *
 */
public class SimpleMatchDefinition implements MatchDefinition {

	/** Key property names. */
	private final Iterable<String> keyProperties;

	/** Values for comparison property names. */
	private final Iterable<String> valueProperties;
	
	/** Other property names. */
	private final Iterable<String> otherProperties;
	
	/**
	 * Constructor.
	 * 
	 * @param keys Array of key property names. May be null. 
	 * @param values Array of value property names. May be null.
	 * @param others Array of other property names. May be null.
	 */
	public SimpleMatchDefinition(String[] keys,
			String[] values, String[] others) {
		
		if (keys == null) {
			this.keyProperties = null;
		}
		else {
			this.keyProperties = Arrays.asList(keys);

		}
		
		if (values == null) {
			this.valueProperties = null;
		}
		else {
			this.valueProperties = Arrays.asList(values);
		}
	
		if (others == null) {
			this.otherProperties = null;
		}
		else {
			this.otherProperties = Arrays.asList(others);
		}
	}	
	
	@Override
    public Iterable<String> getKeyProperties() {
		return keyProperties;
    }
	
	@Override
	public Iterable<String> getValueProperties() {
		return valueProperties;
	}
	
	@Override
	public Iterable<String> getOtherProperties() {
		return otherProperties;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + ": keys " +
				Iterables.toString(keyProperties) + ", values " +
				Iterables.toString(valueProperties) + ", others " + 
				Iterables.toString(otherProperties);
	}
}
