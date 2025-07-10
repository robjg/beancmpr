package org.oddjob.beancmpr.composite;

import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.Comparer;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link ComparersByType} backed by a {@code List}.
 * <p>
 * @oddjob.description Allows comparers to be defined by the type of 
 * thing they are comparing. 
 * <p>
 * Comparers can be listed using the <code>comparers</code> property
 * which provides a new configuration for all types the comparer is for,
 * or the <code>specialisations</code> property which allows a comparer
 * to be targeted at a particular class.
 * 
 * @author rob
 * 
 */
public class ComparersByTypeList implements ComparersByTypeFactory {

	/**
	 * @oddjob.property
	 * @oddjob.description The class loader to use to resolve the type
	 * of a specialisation.
	 * @oddjob.required No. This will be set from Oddjob automatically.
	 */
	private ClassLoader classLoader;

	/**
	 * @oddjob.property
	 * @oddjob.description Comparers listed. A comparer here will override
	 * the default comparer for a give type.
	 * @oddjob.required No.
	 */
	private final List<ComparerFactory<?>> comparers =
			new ArrayList<>();
	
	/**
	 * @oddjob.property
	 * @oddjob.description Comparers specialised by class name. The key
	 * povides the class a comparer is for.
	 * @oddjob.required No.
	 */
	private final Map<String, ComparerFactory<?>> specialisations =
			new LinkedHashMap<>();

	@Inject
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setComparers(int index, ComparerFactory<?> comparer) {
		if (comparer == null) {
			comparers.remove(index);
		}
		else {
			comparers.add(index, comparer);
		}
	}
	
	public void setSpecialisations(String className, ComparerFactory<?> comparer) {
		if (comparer == null) {
			specialisations.remove(className);
		}
		else {
			specialisations.put(className, comparer);
		}
	}
	
	@Override
	public ComparersByType createComparersByTypeWith(
			ComparersByType parentComparersByType) {
		
		Map<Type, Comparer<?>> byClass =
				new LinkedHashMap<>();
		
		ComparersByType comparersByType = new CompositeComparersByType(
				new InternalComparersByType(byClass), parentComparersByType);
		
		for (Map.Entry<String, ComparerFactory<?>> entry : 
					specialisations.entrySet()) {
			
			Class<?> theClass;
			try {
				theClass = ClassUtils.classFor(
						entry.getKey(), classLoader);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(e);
			}

			if (theClass.isPrimitive()) {
				theClass = ClassUtils.wrapperClassForPrimitive(theClass);
			}
			
			Comparer<?> comparer = 
					entry.getValue().createComparerWith(comparersByType);
			
			if (comparer.getType().isAssignableFrom(theClass)) {
				byClass.put(theClass, comparer);
			}
			else {
				throw new IllegalArgumentException(
						"Can't add specialised comparer for " +
						comparer.getType() + 
						" as it is not assignable from " + 
						theClass);
			}
		}
		
		for (ComparerFactory<?> comparerFactory : comparers) {
			Comparer<?> comparer = comparerFactory.createComparerWith(
					comparersByType);
			byClass.put(comparer.getType(), comparer);
		}
		
		return comparersByType;
	}

}
