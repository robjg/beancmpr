package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByNameFactory;
import org.oddjob.beancmpr.composite.ComparersByNameOrTypeFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;

/**
 * A bean that wraps a {@link BeanComparer}.
 * 
 * @author rob
 *
 */
public class BeanComparerType<T>
implements ArooaSessionAware, ComparerFactory<T>, 
		MultiItemComparerFactory<T> {
	
	private String[] values;
	
	private String[] others;
	
	private PropertyAccessor accessor;
	
	private ComparersByTypeFactory comparersByType;
	
	private ComparersByNameFactory comparersByProperty;
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.accessor = session.getTools().getPropertyAccessor();
	}
	
	
	@Override
	public BeanComparer<T> createComparerWith(ComparersByType parentComparersByType) {
		return createComparerWith(parentComparersByType, null);
	}
	
	@Override
	public BeanComparer<T> createComparerWith(
			ComparersByType parentComparersByType,
			BeanCmprResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByProperty, comparersByType);
						
		BeanPropertyComparerProvider comparerProvider = 
				comparerProviderFactory.createWith
					(parentComparersByType);
		
		MatchableFactory<T> matchableFactory = new BeanMatchableFactory<T>(
				new SimpleMatchDefinition(
						null, values, null), 
						accessor);
		
		return new BeanComparer<T>(matchableFactory, 
				comparerProvider, resultHandler);
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

	public ComparersByTypeFactory getComparersByType() {
		return comparersByType;
	}


	public void setComparersByType(ComparersByTypeFactory comparersByType) {
		this.comparersByType = comparersByType;
	}


	public ComparersByNameFactory getComparersByProperty() {
		return comparersByProperty;
	}


	public void setComparersByProperty(ComparersByNameFactory comparersByProperty) {
		this.comparersByProperty = comparersByProperty;
	}

}
