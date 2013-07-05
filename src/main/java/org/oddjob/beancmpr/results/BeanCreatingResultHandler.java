package org.oddjob.beancmpr.results;

import java.util.Collection;

import org.oddjob.arooa.ArooaSession;
import org.oddjob.arooa.deploy.annotations.ArooaHidden;
import org.oddjob.arooa.life.ArooaSessionAware;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.matchables.BeanCmprResultsHandler;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableGroup;
import org.oddjob.beancmpr.matchables.MultiValueComparison;

/**
 * @oddjob.description Creates result beans for matches and adds them
 * to a collection.
 * 
 * @author rob
 *
 */
public class BeanCreatingResultHandler 
implements BeanCmprResultsHandler, PlaysWithBeanbus, ArooaSessionAware {

	/**
	 * @oddjob.property
	 * @oddjob.description A collection that result beans will be added to.
	 * @oddjob.required No.
	 */
	private Collection<? super Object> out;
	
	
	/** From the {@link ArooaSession}. */
	private PropertyAccessor accessor;
	

	/**
	 * @oddjob.property
	 * @oddjob.description This will be prefixed to property names in
	 * the result bean.
	 * @oddjob.required No.
	 */
	private String xPropertyPrefix;
	
	/**
	 * @oddjob.property
	 * @oddjob.description This will be prefixed to property names in
	 * the result bean.
	 * @oddjob.required No.
	 */
	private String yPropertyPrefix;
	
	/**
	 * @oddjob.property
	 * @oddjob.description If true then result beans will not be created
	 * when a comparison results in a match. If false result beans
	 * for all comparisons will be created.
	 * @oddjob.required No. Defaults to false.
	 */
	private boolean ignoreMatches;
	
	/** Factory to create beans. */
	private ResultBeanFactory factory;
	
	/**
	 * @oddjob.property
	 * @oddjob.description Allow For a different 
	 * {@link ResultBeanFactory}.
	 * @oddjob.required No. Defaults to 
	 * {@link SimpleResultBeanFactoryBuilder}.
	 */
	private ResultBeanFactoryBuilder factoryBuilder;
	
	@Override
	@ArooaHidden
	public void setArooaSession(ArooaSession session) {
		this.accessor = session.getTools().getPropertyAccessor();
	}
	
	/**
	 * Called by the framework. Defined in arooa.xml.
	 */
	public void configured() {
		
		if (factoryBuilder == null) {
			
			factoryBuilder = new SimpleResultBeanFactoryBuilder();
		}

		factory = factoryBuilder.factoryFor(accessor, 
					xPropertyPrefix, yPropertyPrefix);
	}
	
	@Override
	public void compared(MultiValueComparison<Matchable> comparison) {
		if (ignoreMatches && comparison.getResult() == 0) {
			return;
		}
		out.add(factory.createComparisonResult(comparison));
	}
	
	@Override
	public void xMissing(MatchableGroup ys) {
		for (Matchable y : ys.getGroup()) {			
			out.add(factory.createXMissingResult(y));
		}
	}
	
	@Override
	public void yMissing(MatchableGroup xs) {
		for (Matchable x : xs.getGroup()) {
			out.add(factory.createYMissingResult(x));
		}
	}	

	public Collection<? super Object> getOut() {
		return out;
	}

	@Override
	public void setOut(Collection<? super Object> out) {
		this.out = out;
	}

	public String getxPropertyPrefix() {
		return xPropertyPrefix;
	}

	public void setxPropertyPrefix(String xPropertyPrefix) {
		this.xPropertyPrefix = xPropertyPrefix;
	}

	public String getyPropertyPrefix() {
		return yPropertyPrefix;
	}

	public void setyPropertyPrefix(String yPropertyPrefix) {
		this.yPropertyPrefix = yPropertyPrefix;
	}

	public boolean isIgnoreMatches() {
		return ignoreMatches;
	}

	public void setIgnoreMatches(boolean ignoreMatches) {
		this.ignoreMatches = ignoreMatches;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + 
				": xPropertyPrefix=" + xPropertyPrefix +
				", yPropertyPrefix=" + yPropertyPrefix + 
				", ignoreMatches=" + ignoreMatches;
	}

	public ResultBeanFactoryBuilder getFactoryBuilder() {
		return factoryBuilder;
	}

	public void setFactoryBuilder(ResultBeanFactoryBuilder beanFactoryBuilder) {
		this.factoryBuilder = beanFactoryBuilder;
	}
}
