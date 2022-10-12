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
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.MapMatchableFactoryProvider;
import org.oddjob.beancmpr.matchables.MatchableFactoryProvider;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;

/**
 * @oddjob.description Provides a Comparer that can compare two maps. The maps
 * will be compared first by their keys to decide if entries are missing, and
 * then by their values to decide if entries match. Comparison of keys and
 * values can be either via the actual key and value or by bean properties of
 * the keys and values.
 * 
 * @oddjob.example
 * 
 * Comparing two maps by the values of their keys and values. The comparer declares 
 * a text comparer which is case insensitive and this is used for comparison
 * of the keys.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/MapCompareSimple.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/MapCompareSimpleOut.txt}
 * 
 * @oddjob.example
 * 
 * Comparing two Beans by a property that is a Map.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/MapCompareOfBeanProperty.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/MapCompareOfBeanPropertyOut.txt}
 * 
 * @oddjob.example
 * 
 * Comparing two Maps of Beans by the properties of their Keys and Values.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/MapCompareComplex.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/MapCompareComplexOut.txt}
 * 
 * @author rob
 *
 * @param <K> The Key Type
 * @param <V> The Value Type
 */
public class MapComparerType<K, V> 
implements ComparerFactory<Map<K, V>>, 
		MultiItemComparerFactory<Map<K , V>>,
		ArooaSessionAware {

	/** Taken from the {@link ArooaSession} for property access. */
	private PropertyAccessor accessor;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description The property names for the key. The key
	 * properties decided if an entry is missing or not. 
	 * @oddjob.required No. When missing the value of the key is used for comparison.
	 */
	private String[] keys;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description The property names for the value. When two keys
	 * match their values are compared using these properties. 
	 * @oddjob.required No. When missing the value itself is used for comparison.
	 */
	private String[] values;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description The property names for other properties to
	 * be reported on as part of the results but not used for comparison.
	 * @oddjob.required No.
	 */
	private String[] others;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Is the map sorted or not.
	 * @oddjob.required No.
	 */
	private boolean sorted;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Sub Comparers to use for comparison based on 
	 * the types of properties or values.
	 * @oddjob.required No.
	 */
	private ComparersByTypeFactory comparersByType;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Sub Comparers to use for comparison of the named
	 * properites of keys or values. Note that if the key and value have a 
	 * property of the same name then this applies to both.
	 * @oddjob.required No.
	 */
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
			CompareResultsHandler resultHandler) {
		
		ComparersByNameOrTypeFactory comparerProviderFactory =
				new ComparersByNameOrTypeFactory(
						comparersByName, comparersByType);
		
		MatchDefinition matchDefinition = new SimpleMatchDefinition(
				keys, values, others);
		
		MatchableFactoryProvider<Map.Entry<K, V>> matchableFactoryProvider = 
				new MapMatchableFactoryProvider<>(matchDefinition, accessor);
		
		return new MapComparer<K, V>(matchableFactoryProvider, 
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
