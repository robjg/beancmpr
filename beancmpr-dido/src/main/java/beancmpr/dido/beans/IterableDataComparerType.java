package beancmpr.dido.beans;

import beancmpr.dido.matchables.DidoMatchableFactoryProvider;
import dido.data.DidoData;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.beans.IterableBeansComparer;
import org.oddjob.beancmpr.beans.IterableComparerType;
import org.oddjob.beancmpr.composite.*;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.MatchableFactoryProvider;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;

/**
 * @oddjob.description Compares two Iterables of Generic Data. Iterables will
 * typically be lists or sets.
 * 
 *
 * @author rob
 *
 */
public class IterableDataComparerType
implements ComparerFactory<Iterable<DidoData>>,
		MultiItemComparerFactory<Iterable<DidoData>> {

	/**
	 * @oddjob.property
	 * @oddjob.description The key field names. The key
	 * properties decided if another data item is missing or not.
	 * @oddjob.required No. 
	 */
	private String[] keys;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description The value field names. The value properties
	 * decide if two data items match when their keys match.
	 * @oddjob.required No. 
	 */
	private String[] values;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Other field names. Other fields
	 * may be of interest on reports but take no part in the matching
	 * process. 
	 * @oddjob.required No. 
	 */
	private String[] others;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Are the collections sorted. If collections are sorted
	 * then key comparison will be much quicker.
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean sorted;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Comparers for comparing the field data.
	 * These comparers will override any other comparers defined
	 * in the comparer hierarchy for their type. This property is most
	 * often set with a {@link ComparersByTypeList}.
	 * @oddjob.required No. 
	 */
	private ComparersByTypeFactory comparersByType;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Comparers for comparing the field data of the
	 * Generic Data defined by the name of the field. This property is most
	 * often set with a {@link ComparersByNameType}.
	 * @oddjob.required No. 
	 */
	private ComparersByNameFactory comparersByName;
	

	@Override
	public MultiItemComparer<Iterable<DidoData>> createComparerWith(
			ComparersByType parentComparersByType) {
	
		if (keys == null && values == null && others == null) {

			IterableComparerType<DidoData> iterableComparer =
					new IterableComparerType<>();
			iterableComparer.setComparersByType(comparersByType);
			
			return iterableComparer.createComparerWith(parentComparersByType);
		}
		else {
			
			return createComparerWith(parentComparersByType, null);
		}
	}
		
	@Override
	public IterableBeansComparer<DidoData> createComparerWith(
			ComparersByType parentComparersByType,
			CompareResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByName, comparersByType);
		
		MatchDefinition matchDefinition = SimpleMatchDefinition.of(
				keys, values, others);
		
		MatchableFactoryProvider<DidoData> matchableFactoryProvider =
				new DidoMatchableFactoryProvider(matchDefinition);
		
		return new IterableBeansComparer<>(
				matchableFactoryProvider,
				comparerProviderFactory.createWith(parentComparersByType),
				sorted,
				resultHandler);
	}
	
	public String[] getKeys() {
		return keys;
	}

	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getOthers() {
		return others;
	}

	public void setOthers(String[] others) {
		this.others = others;
	}

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	public ComparersByTypeFactory getComparersByType() {
		return comparersByType;
	}

	public void setComparersByType(ComparersByTypeFactory comparersByType) {
		this.comparersByType = comparersByType;
	}


	public ComparersByNameFactory getComparersByName() {
		return comparersByName;
	}


	public void setComparersByName(ComparersByNameFactory comparersByName) {
		this.comparersByName = comparersByName;
	}

}
