package org.oddjob.beancmpr;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.beancmpr.beans.BeanArrayComparerType;
import org.oddjob.beancmpr.beans.BeanComparerType;
import org.oddjob.beancmpr.beans.IterableBeansComparerType;
import org.oddjob.beancmpr.beans.MapComparerType;
import org.oddjob.beancmpr.composite.DefaultComparersByType;
import org.oddjob.beancmpr.matchables.CompareResultsHandler;
import org.oddjob.beancmpr.matchables.CompareResultsHandlerFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparer;
import org.oddjob.beancmpr.multiitem.MultiItemComparerFactory;
import org.oddjob.beancmpr.multiitem.MultiItemComparisonCounts;
import org.oddjob.beancmpr.results.BeanCreatingResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @oddjob.description A job that takes two inputs and
 * attempts to match that data using a provided Comparer.
 *
 * @oddjob.example
 *
 * An example of comparing two lists of ints.
 * {@oddjob.xml.resource org/oddjob/beancmpr/CompareTwoListsOfIntegers.xml}
 * The example creates the following output:
 * {@oddjob.text.resource org/oddjob/beancmpr/CompareTwoListsOfIntegersOut.txt}
 *
 * @oddjob.example
 * 
 * An example of comparing two lists of beans.
 * {@oddjob.xml.resource org/oddjob/beancmpr/BeanCompareJobExample.xml}
 * The example creates the following output:
 * {@oddjob.text.resource org/oddjob/beancmpr/BeanCompareJobExampleOut.txt}
 *
 *
 * @author rob
 *
 */
public class BeanCompareJob<T> 
implements Runnable, MultiItemComparisonCounts, ArooaSessionAware {

	private static final Logger logger = LoggerFactory.getLogger(BeanCompareJob.class);
	
	private ArooaSession session;
	
	/**
	 * @oddjob.property
	 * @oddjob.description The name of the job as seen in Oddjob.
	 * @oddjob.required No.
	 */
	private String name;
	
	/**
	 * @oddjob.property
	 * @oddjob.description First source of data.
	 * @oddjob.required Yes.
	 */
	private T inX;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Second source of data.
	 * @oddjob.required Yes.
	 */
	private T inY;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Something to handle results. Typically, a
	 * {@link CompareResultsHandler}.
	 * @oddjob.required No.
	 */
	private CompareResultsHandlerFactory results;
		
	/**
	 * @oddjob.property
	 * @oddjob.description A destination for results that create beans. This
	 * allows this job to play with Oddjob's Bean Bus Framework.
	 * @oddjob.required No.
	 */
	private Consumer<? super Object> to;

    /**
     * @oddjob.property
     * @oddjob.description An appropriate Comparer for the type of inputs.
     * Historically this was commonly {@link IterableBeansComparerType} which
     * compares Beans by their properties. However, integration with
     * <a href="https://github.com/robjg/dido">Dido</a> has been the recent focus
     * with {@link beancmpr.dido.beans.IterableDataComparerType} now being
     * the most common Comparer. If no comparer is provided then one
     * is inferred from the data provided.
     * @oddjob.required No.
     */
	@SuppressWarnings("JavadocReference")
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

		CompareResultsHandler resultsHandler;

		if (to == null ) {
			if (results == null) {
				resultsHandler = null;
			}
			else {
				resultsHandler = this.results.createResultsHandlerTo(null);
			}
		}
		else {
			if (results == null) {
				BeanCreatingResultHandler results = new BeanCreatingResultHandler();
				results.setArooaSession(session);
				results.configured();
				this.results = results;
			}

			resultsHandler = results.createResultsHandlerTo(to);
		}

		MultiItemComparerFactory<T> comparerFactory = this.comparer;
		if (comparerFactory == null) {
			comparerFactory = inferComparerFactory();
		}

		MultiItemComparer<T> comparer = comparerFactory.createComparerWith(
				new DefaultComparersByType(), resultsHandler);
		
		this.counts = comparer.compare(inX, inY);

        logger.info("Xs Missing {}, Ys Missing {}, Different {}, Same {}",
                getXMissingCount(), getYMissingCount(), getDifferentCount(), getMatchedCount());
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

	public CompareResultsHandlerFactory getResults() {
		return results;
	}

	public void setResults(CompareResultsHandlerFactory results) {
		this.results = results;
	}

    /**
     * @oddjob.property
     * @oddjob.description The number of items missing from the first source.
     * @oddjob.required Read only.
     */
	@Override
	synchronized public int getXMissingCount() {
		return counts == null ? 0 : counts.getXMissingCount();
	}

    /**
     * @oddjob.property
     * @oddjob.description The number of items missing from the second source.
     * @oddjob.required Read only.
     */
	@Override
	synchronized public int getYMissingCount() {
		return counts == null ? 0 : counts.getYMissingCount();
	}

    /**
     * @oddjob.property
     * @oddjob.description The number of items matched.
     * @oddjob.required Read only.
     */
	@Override
	synchronized public int getMatchedCount() {
		return counts == null ? 0 : counts.getMatchedCount();
	}

    /**
     * @oddjob.property
     * @oddjob.description The number of items different.
     * @oddjob.required Read only.
     */
	@Override
	synchronized public int getDifferentCount() {
		return counts == null ? 0 : counts.getDifferentCount();
	}

    /**
     * @oddjob.property
     * @oddjob.description The number of differences. This is the sum of the
     * missing and the different.
     * @oddjob.required Read only.
     */
	@Override
	synchronized public int getBreaksCount() {
		return counts == null ? 0 : counts.getBreaksCount();
	}

    /**
     * @oddjob.property
     * @oddjob.description The total number of items compared.
     * @oddjob.required Read only.
     */
	@Override
	synchronized public int getComparedCount() {
		return counts == null ? 0 : counts.getComparedCount();
	}
	
	/**
	 * Used by Oddjob's Bean Bus to Automatically set a destination.
	 * 
	 * @param destination A destination for results.
	 */
	public void acceptDestination(Consumer<? super Object> destination) {
		this.to = destination;
	}	
	
	public MultiItemComparerFactory<T> getComparer() {
		return comparer;
	}

	public void setComparer(
			MultiItemComparerFactory<T> comparer) {
		this.comparer = comparer;
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
