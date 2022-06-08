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
import org.oddjob.beancmpr.composite.ComparersByTypeList;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;

/**
 * @oddjob.description Provides a definition for how to compare two Beans 
 * by the properties of the beans, or how to compare two Objects just by 
 * their values.
 * <p>
 * 
 * @oddjob.example
 * 
 * Comparing two number. The comparer declares a numeric comparer for
 * doubles with a tolerance of 0.1 so the two numbers are deemed equal.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/BeanComparerForTwoNumbers.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/BeanComparerForTwoNumbersOut.txt}
 * 
 * @oddjob.example
 * 
 * Comparing two bean by their properties. A case insensitve text comparer is
 * specified for type but not for colour so the beans do not match on colour.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/BeanComparerForTwoBeans.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/BeanComparerForTwoBeansOut.txt}
 * 
 * @oddjob.example
 * 
 * Specifying a Bean Comparer for the Property of a Bean. Here we specify the
 * comparer to be used for the fruit property of a Snack class.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/beans/BeanComparerForProperty.xml}
 * 
 * The output is:
 * 
 * {@oddjob.text.resource org/oddjob/beancmpr/beans/BeanComparerForPropertyOut.txt}
 * 
 * 
 * @see BeanComparer
 * 
 * @author rob
 *
 */
public class BeanComparerType<T>
implements ArooaSessionAware, ComparerFactory<T>, 
		MultiItemComparerFactory<T> {
	
	/**
	 * @oddjob.property
	 * @oddjob.description Names of properties to use for the comparison.
	 * @oddjob.required No. If no property names are given then the
	 * objects themselves are used for the comparison. 
	 */
	private String[] values;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Names of properties that to use for the comparison.
	 * @oddjob.required No. If no property names are given then the
	 * objects themselves are used for the comparison. 
	 */
	private String[] others;
	
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
	 * often set with a {@link org.oddjob.beancmpr.composite.ComparersByNameType}.
	 * @oddjob.required No. 
	 */
	private ComparersByNameFactory comparersByName;
	
	/** Used to access the properties of the beans. */
	private PropertyAccessor accessor;
	
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
						comparersByName, comparersByType);
						
		BeanPropertyComparerProvider comparerProvider = 
				comparerProviderFactory.createWith
					(parentComparersByType);
		
		if (values == null) {
			 values = new String[] { BeanMatchableFactory.SELF_TOKEN };
		}
		
		MatchableFactory<T> matchableFactory = new BeanMatchableFactory<>(
				new SimpleMatchDefinition(
						null, values, others),
				accessor);
		
		return new BeanComparer<>(matchableFactory,
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


	public ComparersByNameFactory getComparersByName() {
		return comparersByName;
	}


	public void setComparersByName(ComparersByNameFactory comparersByProperty) {
		this.comparersByName = comparersByProperty;
	}

}
