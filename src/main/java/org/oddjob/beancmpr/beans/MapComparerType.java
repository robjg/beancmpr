package org.oddjob.beancmpr.beans;

import java.util.Map;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.MatchDefinition;
import org.oddjob.beancmpr.SimpleMatchDefinition;
import org.oddjob.beancmpr.composite.ComparerFactory;
import org.oddjob.beancmpr.composite.ComparersByNameFactory;
import org.oddjob.beancmpr.composite.ComparersByNameOrTypeFactory;
import org.oddjob.beancmpr.composite.ComparersByType;
import org.oddjob.beancmpr.composite.ComparersByTypeFactory;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.MapMatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;

public class MapComparerType<K, V> 
implements ComparerFactory<Map<K, V>>, 
		MultiItemComparerFactory<Map<K , V>>,
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
	public Comparer<Map<K, V>> createComparerWith(
			ComparersByType parentComparersByType) {
	
		return createComparerWith(parentComparersByType, null);
	}
		
	@Override
	public MultiItemComparer<Map<K, V>> createComparerWith(
			ComparersByType parentComparersByType,
			BeanCmprResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByName, comparersByType);
		
		MatchDefinition matchDefinition = new SimpleMatchDefinition(
				keys, values, others);
		
		MatchableFactory<Map.Entry<K, V>> matchableFactory = 
				new MapMatchableFactory<K, V>(matchDefinition, accessor);
	
		return new MapComparer<K, V>(matchableFactory, 
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
