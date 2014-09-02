package org.oddjob.beancmpr;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.beancmpr.beans.BeanArrayComparerType;
import org.oddjob.beancmpr.beans.BeanComparerType;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;
import org.oddjob.beancmpr.beans.MapComparerType;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparisonCounts;
import org.oddjob.beancmpr.results.BeanCreatingResultHandler;
import org.oddjob.beancmpr.results.PlaysWithBeanbus;

/**
 * @oddjob.description A job that takes two streams of beans and
 * attempts to match the beans according to their properties.
 * 
 * @oddjob.example
 * 
 * A simple example.
 * 
 * {@oddjob.xml.resource org/oddjob/beancmpr/BeanCompareJob2Example1.xml}
 * 
 * 
 * @author rob
 *
 */
public class BeanCompareJob<T> 
implements Runnable, MultiItemComparisonCounts, ArooaSessionAware {

	private static final Logger logger = Logger.getLogger(BeanCompareJob.class);
	
	private ArooaSession session;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The name of the job as seen in Oddjob.
	 * @oddjob.required No.
	 */
	private String name;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A source of beans.
	 * @oddjob.required Yes.
	 */
	private T inX;
	
	/**
	 * @oddjob.property
	 * @oddjob.description A source of beans.
	 * @oddjob.required Yes.
	 */
	private T inY;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Something to handle results. Typically a 
	 * {@link BeanCmprResultsHandler}.
	 * @oddjob.required No.
	 */
	private BeanCmprResultsHandler results;
		
	/**
	 * @oddjob.property
	 * @oddjob.description A destination for results that create beans. This
	 * allows this job to play with Oddjob's Bean Bus Framework.
	 * @oddjob.required No.
	 */
	private Collection<? super Object> to;
	
	private MultiItemComparerFactory<T> comparer;
	
	/** Counts. */
	private MultiItemComparisonCounts counts;
	
	@Override
	@ArooaHidden
	public void setArooaSession(ArooaSession session) {
		this.session = session;
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
		
		if (to != null) {
			
			if (results == null) {
				BeanCreatingResultHandler results = new BeanCreatingResultHandler();
				results.setArooaSession(session);
				results.configured();
				this.results = results;
			}			
			
			if (results instanceof PlaysWithBeanbus) {
				((PlaysWithBeanbus) results).setOut(to);
			}
			else {
				logger.warn("The 'to' property is set but results will ignore it");
			}
		}
		
		MultiItemComparerFactory<T> comparerFactory = this.comparer;
		if (comparerFactory == null) {
			comparerFactory = inferComparerFactory();
		}
		
		MultiItemComparer<T> comparer = comparerFactory.createComparerWith(
				new DefaultComparersByType(), results);
		
		this.counts = comparer.compare(inX, inY);
		
		logger.info("Xs Missing " + getXMissingCount() +
				", Ys Missing " + getYMissingCount() + 
				", Different " + getDifferentCount() + 
				", Same " + getMatchedCount());
	}	
	
	@SuppressWarnings("unchecked")
	protected MultiItemComparerFactory<T> inferComparerFactory() {

		if (inX instanceof Map && inY instanceof Map) {
			
			MapComparerType<Object, Object> comparerFactory = 
					new MapComparerType<>();
			comparerFactory.setArooaSession(session);
			
			return (MultiItemComparerFactory<T>) comparerFactory;
		}
		
		if (inX instanceof Iterable && inY instanceof Iterable) {
			
			IterableBeansComparerType<Iterable<?>> comparerFactory = 
					new IterableBeansComparerType<>();
			comparerFactory.setArooaSession(session);
			
			return (MultiItemComparerFactory<T>) comparerFactory;
		}
		
		if (inX.getClass().isArray() && inY.getClass().isArray()) {
			
			BeanArrayComparerType comparerFactory = 
					new BeanArrayComparerType();
			comparerFactory.setArooaSession(session);
			
			return (MultiItemComparerFactory<T>) comparerFactory;
		}
		
		BeanComparerType<T> comparerFactory = new BeanComparerType<>();
		comparerFactory.setArooaSession(session);
		
		return comparerFactory;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public T getInX() {
		return inX;
	}

	public void setInX(T x) {
		this.inX = x;
	}

	public T getInY() {
		return inY;
	}

	public void setInY(T y) {
		this.inY = y;
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
	
	/**
	 * Used by Oddjob's Bean Bus to Automatically set a destination.
	 * 
	 * @param destination
	 */
	public void acceptDestination(Collection<? super Object> destination) {
		this.to = destination;
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

	public MultiItemComparerFactory<T> getComparer() {
		return comparer;
	}

	public void setComparer(
			MultiItemComparerFactory<T> comparer) {
		this.comparer = comparer;
	}

}
