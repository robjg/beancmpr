package org.oddjob.beancmpr.composite;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.Comparer;

/**
 * An implementation of {@link ComparersByType} backed by a {@code List}.
 * 
 * @author rob
 * 
 */
public class ComparersByTypeList implements ComparersByTypeFactory {

	private ClassLoader classLoader;

	private final List<ComparerFactory<?>> comparers = 
			new ArrayList<ComparerFactory<?>>();
	
	private final Map<String, ComparerFactory<?>> specialisations = 
			new LinkedHashMap<String, ComparerFactory<?>>();

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
		
		Map<Class<?>, Comparer<?>> byClass = 
				new LinkedHashMap<Class<?>, Comparer<?>>();
		
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
						"Can't add a comparer for " +
						comparer.getType() + 
						" as it is not type compatible with " + 
						theClass);
			}
		}
		
		for (ComparerFactory<?> comparerFactory : comparers) {
			Comparer<?> comparer = comparerFactory.createComparerWith(
					comparersByType);
			byClass.put(comparer.getType(), comparer);
		}
		
		return new InternalComparersByType(byClass);
	}

}
