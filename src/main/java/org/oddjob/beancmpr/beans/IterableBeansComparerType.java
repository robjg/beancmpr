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
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;

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
	public IterableBeansComparer<T> createComparerWith(
			ComparersByType parentComparersByType) {
	
		return createComparerWith(parentComparersByType, null);
	}
		
	@Override
	public IterableBeansComparer<T> createComparerWith(
			ComparersByType parentComparersByType,
			BeanCmprResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByName, comparersByType);
		
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
