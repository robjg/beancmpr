package org.oddjob.beancmpr.composite;

import java.util.LinkedHashMap;
import java.util.Map;

import org.oddjob.beancmpr.Comparer;
import org.oddjob.beancmpr.beans.ArrayComparerType;
import org.oddjob.beancmpr.beans.IterableComparerType;
import org.oddjob.beancmpr.comparers.ComparableComparer;
import org.oddjob.beancmpr.comparers.DateComparer;
import org.oddjob.beancmpr.comparers.EqualityComparer;
import org.oddjob.beancmpr.comparers.NumericComparer;

/**
 * Default {@link Comparer}s.
 * 
 * @author rob
 *
 */
public class DefaultComparersByType implements ComparersByType {

	private final InternalComparersByType comparers;
	
	public DefaultComparersByType() {
		
		Map<Class<?>, Comparer<?>> map = 
				new LinkedHashMap<Class<?>, Comparer<?>>();
		
		map.put(Integer.class, new ComparableComparer<Integer>());
		map.put(Long.class, new ComparableComparer<Long>());
		map.put(Short.class, new ComparableComparer<Short>());
		map.put(Byte.class, new ComparableComparer<Byte>());
		
		doPut(new NumericComparer(), map);
		doPut(new DateComparer(), map);
		doPut(new ArrayComparerType().createComparerWith(this), map);
		doPut(new IterableComparerType<Object>().createComparerWith(this), map);
		doPut(new EqualityComparer(), map);
		
		this.comparers = new InternalComparersByType(map);
	}
		
	private void doPut(Comparer<?> comparer, 
			Map<Class<?>, Comparer<?>> map) {
		
		map.put(comparer.getType(), comparer);
	}
	
	
	@Override
	public <T> Comparer<T> comparerFor(Class<T> type) {
		return comparers.comparerFor(type);
	}	
}
