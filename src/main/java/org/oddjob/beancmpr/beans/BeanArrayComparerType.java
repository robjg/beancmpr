package org.oddjob.beancmpr.beans;

import java.util.Arrays;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByNameFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.multiitem.DelegatingMultiItemComparison;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparison;

public class BeanArrayComparerType<T> 
implements ComparerFactory<T[]>,
		MultiItemComparerFactory<T[]>,
		ArooaSessionAware {

	private ArooaSession session;
	
	private String[] keys;
	
	private String[] values;
	
	private String[] others;
	
	private boolean sorted;
	
	private ComparersByTypeFactory comparersByType;
	
	private ComparersByNameFactory comparersByName;
	
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.session = session;
	}

	
	@Override
	public MultiItemComparer<T[]> createComparerWith(
			ComparersByType parentComparersByType) {
	
		if (keys == null && values == null && others == null) {
			
			ArrayComparerType<T> arrayComparerType = new ArrayComparerType<>();
			arrayComparerType.setComparersByType(comparersByType);
			
			return arrayComparerType.createComparerWith(parentComparersByType);
		}
		
		return createComparerWith(parentComparersByType, null);
	}
		
	@Override
	public MultiItemComparer<T[]> createComparerWith(
			ComparersByType parentComparersByType,
			BeanCmprResultsHandler resultHandler) {
		
		
		IterableBeansComparerType<T> iterableBeansComparerType = 
				new IterableBeansComparerType<>();
				
		if (session != null) {
			iterableBeansComparerType.setArooaSession(session);
		}
		
		iterableBeansComparerType.setComparersByName(comparersByName);
		iterableBeansComparerType.setComparersByType(comparersByType);
		iterableBeansComparerType.setKeys(keys);
		iterableBeansComparerType.setValues(values);
		iterableBeansComparerType.setOthers(others);
		iterableBeansComparerType.setSorted(sorted);
		
		final IterableBeansComparer<T> iterableBeansComparer = 
				iterableBeansComparerType.createComparerWith(
						parentComparersByType, resultHandler);
	
		return new MultiItemComparer<T[]>() {
			@Override
			public MultiItemComparison<T[]> compare(T[] x, T[] y) {
				
				Iterable<T> xIt = Arrays.asList(x);
				Iterable<T> yIt = Arrays.asList(y);
				
				return new DelegatingMultiItemComparison<
						T[]>(x, y, iterableBeansComparer.compare(xIt, yIt));
			}
			
			@Override
			public Class<?> getType() {
				return Object[].class;
			}
		};
		
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
