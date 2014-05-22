package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.composite.BeanPropertyComparerProvider;
import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByNameFactory;
import org.oddjob.beancmpr.composite.ComparersByNameOrTypeFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;

/**
 * A bean that wraps a {@link BeanComparer}.
 * 
 * @author rob
 *
 */
public class BeanComparerType implements ArooaSessionAware, 
		ComparerFactory<Comparer<Object>> {
	
	private String[] matchProperties;
	
	private PropertyAccessor accessor;
	
	private ComparersByTypeFactory comparersByType;
	
	private ComparersByNameFactory comparersByProperty;
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.accessor = session.getTools().getPropertyAccessor();
	}
	

	@Override
	public Comparer<Object> createComparerWith(ComparersByType parentComparersByType) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByProperty, comparersByType);
						
		BeanPropertyComparerProvider comparerProvider = 
				comparerProviderFactory.createWith
					(parentComparersByType);
		
		return new BeanComparer(matchProperties, accessor, comparerProvider);
	}

	public String[] getMatchProperties() {
		return matchProperties;
	}

	@ArooaAttribute
	public void setMatchProperties(String[] matchProperties) {
		this.matchProperties = matchProperties;
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
