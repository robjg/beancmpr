package org.oddjob.beancmpr;

import org.oddjob.beancmpr.matchables.ImmutableCollection;

/**
 * Define the properties for matching two beans.
 * <p>
 * 
 * @author Rob
 *
 */
public interface MatchDefinition {

	/**
	 * The key property names. These properties decide if two things 
	 * can be compared.
	 * 
	 * @return The names of the key properties. May not be null.
	 */
	ImmutableCollection<String> getKeyProperties();
	
	/**
	 * The names of the properties of the values that will be compared.
	 * 
	 * @return The names of the properties for comparison. May not be null.
	 */
	ImmutableCollection<String> getValueProperties();
		
		
	/**
	 * Other properties that won't be used in the match, but which
	 * might be required for information on results. Inserted time,
	 * or Operator Id for instance.
	 * 
	 * @return The names of other properties. May not be null.
	 */
	ImmutableCollection<String> getOtherProperties();
	
}
