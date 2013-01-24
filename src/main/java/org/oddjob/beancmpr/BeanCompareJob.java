package org.oddjob.beancmpr;

import org.apache.log4j.Logger;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaAttribute;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.beans.ComparersByProperty;
import org.oddjob.beancmpr.beans.ComparersByPropertyOrType;
import org.oddjob.beancmpr.comparers.ComparersByType;
import org.oddjob.beancmpr.comparers.MultiItemComparisonCounts;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.BeanMatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableFactory;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.OrderedMatchablesComparer;
import org.oddjob.beancmpr.matchables.SortedBeanMatchables;
import org.oddjob.beancmpr.matchables.UnsortedBeanMatchables;

/**
 * @oddjob.description A job that takes two streams of beans and
 * attempts to match the beans according to their properties.
 * 
 * @oddjob.example
 * 
 * A simple example.
 * 
 * {@oddjob.xml.resource org/oddjob/dido/match/MatchExample2.xml}
 * 
 * 
 * @author rob
 *
 */
public class BeanCompareJob 
implements ArooaSessionAware, Runnable, MultiItemComparisonCounts {

	private static final Logger logger = Logger.getLogger(BeanCompareJob.class);
	
	/**
	 * @oddjob.property
	 * @oddjob.description The name of the job as seen in Oddjob.
	 * @oddjob.required No.
	 */
	private String name;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The key properties. A comma separated list of
	 * the properties of the beans that will be used to decide which can
	 * be matched.
	 * @oddjob.required No.
	 */
	private String[] keyProperties;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The value properties. A comma separated list of
	 * the properties of the beans that will be used to match the two 
	 * beans against each other.
	 * @oddjob.required No.
	 */
	private String[] valueProperties;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Other properties. A comma separated list of
	 * the properties of the beans that aren't used in the match but
	 * will be passed through to the results.
	 * @oddjob.required No.
	 */
	private String[] otherProperties;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Used to specify additional comparers to the
	 * default ones. If a comparer is specified for an existing type it
	 * takes precedence.
	 * @oddjob.required No.
	 */
	private ComparersByType comparersByType;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Used to specify comparers that will be used for 
	 * particular properties of the beans.
	 * @oddjob.required No.
	 */
	private ComparersByProperty comparersByProperty;
	
	/** From the {@link ArooaSession}. */
	private PropertyAccessor accessor;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A source of beans.
	 * @oddjob.required Yes.
	 */
	private Iterable<?> inX;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A source of beans.
	 * @oddjob.required Yes.
	 */
	private Iterable<?> inY;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Are the input Iterables sorted? If they
	 * are then they will be read immediately, if not they will be 
	 * copied and sorted.
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean sorted;
		
	/**
	 * @oddjob.property
	 * @oddjob.description Something to handle results. Typically a 
	 * {@link BeanCmprResultsHandler}.
	 * @oddjob.required No.
	 */
	private BeanCmprResultsHandler results;
	
	/** Counts. */
	private MultiItemComparisonCounts counts;
	
	@Override
	@ArooaHidden
	public void setArooaSession(ArooaSession session) {
		this.accessor = session.getTools().getPropertyAccessor();
	}
	
	synchronized public void reset() {
		counts = null;
	}
	
	@Override
	final public void run() {
		
		if (inX == null) {
			throw new NullPointerException("No X");
		}
		if (inY == null) {
			throw new NullPointerException("No Y");
		}
		
		MatchDefinition definition = new SimpleMatchDefinition(
				getKeyProperties(), getValueProperties(), 
				getOtherProperties());
		
		BeanMatchableFactory factory = new BeanMatchableFactory(
				definition, accessor);
		
		ComparersByPropertyOrType comparerProvider =
			new ComparersByPropertyOrType(
					comparersByProperty, comparersByType);
			
		OrderedMatchablesComparer rec = new OrderedMatchablesComparer(
				accessor,
				comparerProvider,
				results);
		this.counts = rec.getMultiItemComparisonStats();
		
		rec.compare(getIterableMatchables(inX, factory), 
				getIterableMatchables(inY, factory));		
		
		logger.info("Xs Missing " + getXMissingCount() +
				", Ys Missing " + getYMissingCount() + 
				", Different " + getDifferentCount() + 
				", Same " + getMatchedCount());
	}	
	
	private Iterable<MatchableGroup> getIterableMatchables(
			Iterable<?> in, MatchableFactory<Object> factory) {
		if (sorted) {
			return new SortedBeanMatchables<Object>(in, factory);
		}
		else {
			return new UnsortedBeanMatchables<Object>(in, factory);
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Iterable<?> getInX() {
		return inX;
	}

	public void setInX(Iterable<?> x) {
		this.inX = x;
	}

	public Iterable<?> getInY() {
		return inY;
	}

	public void setInY(Iterable<?> y) {
		this.inY = y;
	}

	public String[] getValueProperties() {
		return valueProperties;
	}

	@ArooaAttribute
	public void setValueProperties(String[] matchProperties) {
		this.valueProperties = matchProperties;
	}

	public String[] getKeyProperties() {
		return keyProperties;
	}

	@ArooaAttribute
	public void setKeyProperties(String[] keys1) {
		this.keyProperties = keys1;
	}

	public String[] getOtherProperties() {
		return otherProperties;
	}

	@ArooaAttribute
	public void setOtherProperties(String[] others) {
		this.otherProperties = others;
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

	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}
	
	public BeanCmprResultsHandler getResults() {
		return results;
	}

	public void setResults(BeanCmprResultsHandler results) {
		this.results = results;
	}

	@Override
	synchronized public int getXMissingCount() {
		return counts == null ? 0 : counts.getXMissingCount();
	}
	
	@Override
	synchronized public int getYMissingCount() {
		return counts == null ? 0 : counts.getYMissingCount();
	}
	
	@Override
	synchronized public int getMatchedCount() {
		return counts == null ? 0 : counts.getMatchedCount();
	}
	
	@Override
	synchronized public int getDifferentCount() {
		return counts == null ? 0 : counts.getDifferentCount();
	}
	
	@Override
	synchronized public int getBreaksCount() {
		return counts == null ? 0 : counts.getBreaksCount();
	}
	
	@Override
	synchronized public int getComparedCount() {
		return counts == null ? 0 : counts.getComparedCount();
	}
	
	@Override
	public String toString() {
		if (name == null) {
			return getClass().getSimpleName();
		}
		else {
			return name;
		}
	}
}
