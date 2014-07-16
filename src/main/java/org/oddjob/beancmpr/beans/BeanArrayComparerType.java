package org.oddjob.beancmpr.beans;

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

/**
 * @oddjob.description Compares an Array of Java Beans. If no Java Bean 
 * properties are specified for the comparison then a comparison of the 
 * elements is made using a comparer defined for the class of the elements, 
 * or a default comparer if none is specified.
 * 
 * @oddjob.example
 * 
 * Comparing two arrays of beans. Properties of the beans are used to make
 * the comparison.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/ArrayOfBeansCompareByProperties.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/ArrayOfBeansCompareByPropertiesOut.txt}
 * 
 * @oddjob.example
 * 
 * Comparing two arrays of ints.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/ArrayOfIntsCompare.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/ArrayOfIntsCompareOut.txt}
 * 
 * @oddjob.example
 * 
 * Specifying how to compare an array that is the property of two beans
 * being compared.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/BeanWithArrayPropertyCompare.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/BeanWithArrayPropertyCompareOut.txt}
 * 
 * 
 * @see ArrayComparerFactory
 * 
 * @author rob
 *
 * @param <T> The type of the elements of the array.
 */
public class BeanArrayComparerType 
implements ComparerFactory<Object>,
		MultiItemComparerFactory<Object>,
		ArooaSessionAware {

	/** The session provided by the Arooa framework. */
	private ArooaSession session;
	
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
	 * @oddjob.description Are the arrays sorted. If arrays are sorted
	 * then key comparision will be much quicker. 
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean sorted;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Comparers for comparing the properties of the
	 * beans. These comparers will override any other comparers defined
	 * in the comparer hierarchy for their type. 
	 * @oddjob.required No. 
	 */
	private ComparersByTypeFactory comparersByType;
	
	/** 
	 * @oddjob.property
	 * @oddjob.description Comparers for comparing the properties of the
	 * beans defined for properties of a given name. 
	 * @oddjob.required No. 
	 */
	private ComparersByNameFactory comparersByName;
	
	
	@ArooaHidden
	@Override
	public void setArooaSession(ArooaSession session) {
		this.session = session;
	}

	
	@Override
	public MultiItemComparer<Object> createComparerWith(
			ComparersByType parentComparersByType) {
	
		if (keys == null && values == null && others == null) {
			
			ArrayComparerFactory arrayComparerType = new ArrayComparerFactory();
			arrayComparerType.setComparersByType(comparersByType);
			
			return arrayComparerType.createComparerWith(parentComparersByType);
		}
		
		return createComparerWith(parentComparersByType, null);
	}
		
	@Override
	public MultiItemComparer<Object> createComparerWith(
			ComparersByType parentComparersByType,
			BeanCmprResultsHandler resultHandler) {
		
		
		IterableBeansComparerType<Object> iterableBeansComparerType = 
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
		
		final IterableBeansComparer<Object> iterableBeansComparer = 
				iterableBeansComparerType.createComparerWith(
						parentComparersByType, resultHandler);
	
		return new MultiItemComparer<Object>() {
			@Override
			public MultiItemComparison<Object> compare(Object x, Object y) {
				
				Iterable<Object> xIt = new ArrayIterable(x);
				Iterable<Object> yIt = new ArrayIterable(y);
				
				return new DelegatingMultiItemComparison<
						Object>(x, y, iterableBeansComparer.compare(xIt, yIt));
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
