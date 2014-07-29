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
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;
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

	private PropertyAccessor accessor;
	
	private String[] keys;
	
	private String[] values;
	
	private String[] others;
	
	private boolean sorted;
	
	private ComparersByTypeFactory comparersByType;
	
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
			BeanCmprResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByName, comparersByType);
		
		if (keys == null && values == null) {
			 keys = new String[] { BeanMatchableFactory.SELF_TOKEN };
		}
		
		MatchDefinition matchDefinition = new SimpleMatchDefinition(
				keys, values, others);
		
		MatchableFactory<T> matchableFactory = 
				new BeanMatchableFactory<T>(matchDefinition, accessor);
	
		return new IterableBeansComparer<T>(matchableFactory, 
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
