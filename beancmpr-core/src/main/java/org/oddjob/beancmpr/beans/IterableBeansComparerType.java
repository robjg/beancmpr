package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByNameFactory;
import org.oddjob.beancmpr.composite.ComparersByNameOrTypeFactory;
import org.oddjob.beancmpr.composite.ComparersByNameType;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;
import org.oddjob.beancmpr.composite.ComparersByTypeList;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.BeanMatchableFactoryProvider;
import org.oddjob.beancmpr.matchables.MatchableFactoryProvider;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;

/**
 * @oddjob.description Compares two Iterables of Java Beans. Iterables will
 * typeically be lists or sets. If no Java Bean 
 * properties are specified for the comparison then a comparison of the 
 * elements is made using a comparer defined for the class of the elements, 
 * or a default comparer if none is specified.
 * 
 * @oddjob.example
 * 
 * {@link BeanArrayComparerType} has an example of comparing two lists
 * of beans.
 * 
 * @author rob
 *
 * @param <T>
 */
public class IterableBeansComparerType<T> 
implements ComparerFactory<Iterable<T>>,
		MultiItemComparerFactory<Iterable<T>>,
		ArooaSessionAware {

	/** Used to access bean properties. */
	private PropertyAccessor accessor;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description The key property names. The key
	 * properties decided if another bean is missing or not. 
	 * @oddjob.required No. 
	 */
	private String[] keys;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description The value property names. The value properties
	 * decide if two beans match when their keys match. 
	 * @oddjob.required No. 
	 */
	private String[] values;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Other property names. Other properties
	 * may be of interest on reports but take no part in the matching
	 * process. 
	 * @oddjob.required No. 
	 */
	private String[] others;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Are the collections sorted. If arrays are sorted
	 * then key comparision will be much quicker. 
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean sorted;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Comparers for comparing the properties of the
	 * beans. These comparers will override any other comparers defined
	 * in the comparer hierarchy for their type. This property is most
	 * often set with a {@link ComparersByTypeList}.
	 * @oddjob.required No. 
	 */
	private ComparersByTypeFactory comparersByType;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Comparers for comparing the properties of the
	 * beans defined by the name of the property. This property is most
	 * often set with a {@link ComparersByNameType}.
	 * @oddjob.required No. 
	 */
	private ComparersByNameFactory comparersByName;
	
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.accessor = session.getTools().getPropertyAccessor();
	}

	@Override
	public MultiItemComparer<Iterable<T>> createComparerWith(
			ComparersByType parentComparersByType) {
	
		if (keys == null && values == null && others == null) {

			IterableComparerType<T> iterableComparer = 
					new IterableComparerType<>();
			iterableComparer.setComparersByType(comparersByType);
			
			return iterableComparer.createComparerWith(parentComparersByType);
		}
		else {
			
			return createComparerWith(parentComparersByType, null);
		}
	}
		
	@Override
	public IterableBeansComparer<T> createComparerWith(
			ComparersByType parentComparersByType,
			CompareResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByName, comparersByType);
		
		if (keys == null && values == null) {
			 keys = new String[] { BeanMatchableFactory.SELF_TOKEN };
		}
		
		MatchDefinition matchDefinition = new SimpleMatchDefinition(
				keys, values, others);
		
		MatchableFactoryProvider<T> matchableFactoryProvider =
				new BeanMatchableFactoryProvider<>(matchDefinition, accessor);
		
		return new IterableBeansComparer<>(
				matchableFactoryProvider,
				comparerProviderFactory.createWith(parentComparersByType),
				sorted,
				resultHandler);
	}
	
	public String[] getKeys() {
		return keys;
	}

	@ArooaAttribute
	public void setKeys(String[] keys) {
		this.keys = keys;
	}

	public String[] getValues() {
		return values;
	}

	@ArooaAttribute
	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getOthers() {
		return others;
	}

	@ArooaAttribute
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
