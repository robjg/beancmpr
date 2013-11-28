package org.oddjob.beancmpr.comparers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.oddjob.arooa.convert.ArooaConversionException;
import org.oddjob.arooa.types.ValueFactory;
import org.oddjob.arooa.utils.ClassUtils;
import org.oddjob.beancmpr.Comparer;

/**
 * An implementation of {@link ComparersByType} backed by a {@code List}.
 * 
 * @author rob
 * 
 */
public class ComparersByTypeList implements ValueFactory<ComparersByType>{

	private ClassLoader classLoader;

	private final List<Comparer<?>> comparers = 
			new ArrayList<Comparer<?>>();
	
	private final Map<String, Comparer<?>> specialisations = 
			new LinkedHashMap<String, Comparer<?>>();

	@Inject
	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setComparers(int index, Comparer<?> comparer) {
		if (comparer == null) {
			comparers.remove(index);
		}
		else {
			comparers.add(index, comparer);
		}
	}
	
	public void setSpecialisations(String className, Comparer<?> comparer) {
		if (comparer == null) {
			specialisations.remove(className);
		}
		else {
			specialisations.put(className, comparer);
		}
	}
	
	@Override
	public ComparersByType toValue() throws ArooaConversionException {
		
		Map<Class<?>, Comparer<?>> byClass = 
				new LinkedHashMap<Class<?>, Comparer<?>>();
		
		for (Map.Entry<String, Comparer<?>> entry : specialisations.entrySet()) {
			
			Class<?> theClass;
			try {
				theClass = ClassUtils.classFor(
						entry.getKey(), classLoader);
			} catch (ClassNotFoundException e) {
				throw new ArooaConversionException(e);
			}

			if (theClass.isPrimitive()) {
				theClass = ClassUtils.wrapperClassForPrimitive(theClass);
			}
			
			Comparer<?> comparer = entry.getValue();
			
			if (comparer.getType().isAssignableFrom(theClass)) {
				byClass.put(theClass, comparer);
			}
			else {
				throw new ArooaConversionException(
						"Can't add a comparer for " +
						comparer.getType() + 
						" as it is not type compatible with " + 
						theClass);
			}
		}
		
		for (Comparer<?> comparer : comparers) {
			byClass.put(comparer.getType(), comparer);
		}
		
		return new InternalComparersByType(byClass);
	}

}
