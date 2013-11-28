package org.oddjob.beancmpr.comparers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.oddjob.beancmpr.Comparer;

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
		doPut(new ArrayComparer(), map);
		doPut(new IterableComparer<Object>(), map);
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
	
	@Override
	public void injectComparers(ComparersByType comparers) {
		this.comparers.injectComparers(comparers);
	}
}
