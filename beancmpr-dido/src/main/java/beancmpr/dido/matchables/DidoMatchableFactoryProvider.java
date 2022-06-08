package beancmpr.dido.matchables;

import dido.data.GenericData;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactoryProvider;

/**
 * Provide a {@link MatchableFactory} for a Java Bean.
 * 
 * @see DidoMatchableFactory
 * 
 * @author rob
 *
 */
public class DidoMatchableFactoryProvider
implements MatchableFactoryProvider<GenericData<String>> {
	
	/** The definition for the match. */ 
	private final MatchDefinition matchDefinition;


	/**
	 * Create a new instance.
	 * 
	 * @param matchDefinition
	 */
	public DidoMatchableFactoryProvider(
			MatchDefinition matchDefinition) {
		
		this.matchDefinition = matchDefinition;
	}
	
	@Override
	public MatchableFactory<GenericData<String>> provideFactory() {
		return new DidoMatchableFactory(
				matchDefinition);
	}
}
