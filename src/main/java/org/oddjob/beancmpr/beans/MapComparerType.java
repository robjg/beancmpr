package org.oddjob.beancmpr.beans;

import java.util.Map;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.arooa.types.ValueFactory;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.matchables.MapMatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;

public class MapComparerType 
implements ValueFactory<Comparer<Map<?, ?>>>, ArooaSessionAware {

	private PropertyAccessor accessor;
	
	private String[] keys;
	
	private String[] values;
	
	private String[] others;
	
	private boolean sorted;
	
	private ComparersByType comparersByType;
	
	private ComparersByProperty comparersByProperty;
	
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.accessor = session.getTools().getPropertyAccessor();
	}

	@Override
	public Comparer<Map<?, ?>> toValue() throws ArooaConversionException {
		
		ComparersByPropertyOrTypeFactory comparerProviderFactory =
				new ComparersByPropertyOrTypeFactory(
						comparersByProperty, comparersByType);
		
		MatchDefinition matchDefinition = new SimpleMatchDefinition(
				keys, values, others);
		
		MatchableFactory<Map.Entry<?, ?>> matchableFactory = 
				new MapMatchableFactory(matchDefinition, accessor);
	
		return new MapComparer(matchableFactory, comparerProviderFactory);
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

	public ComparersByType getComparersByType() {
		return comparersByType;
	}

	public void setComparersByType(ComparersByType comparersByType) {
		this.comparersByType = comparersByType;
	}


	public ComparersByProperty getComparersByProperty() {
		return comparersByProperty;
	}


	public void setComparersByProperty(ComparersByProperty comparersByProperty) {
		this.comparersByProperty = comparersByProperty;
	}

}
