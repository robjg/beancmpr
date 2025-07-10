package org.oddjob.beancmpr.results;

import org.oddjob.arooa.beanutils.MagicBeanClassCreator;
import org.oddjob.arooa.reflect.ArooaClass;
import org.oddjob.arooa.reflect.BeanOverview;
import org.oddjob.arooa.reflect.PropertyAccessor;
import org.oddjob.beancmpr.beans.PropertyTypeHelper;
import org.oddjob.beancmpr.matchables.Matchable;
import org.oddjob.beancmpr.matchables.MatchableMetaData;
import org.oddjob.beancmpr.matchables.MultiValueComparison;
import org.oddjob.beancmpr.util.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.Objects;


/**
 * Provides super classes with implementation for naming of properties of 
 * a {@link ResultBeanFactory}.
 * 
 * @author rob
 *
 */
abstract public class SharedNameResultBeanFactory extends AbstractResultBeanFactory
implements ResultBeanFactory {

	private static final Logger logger = LoggerFactory.getLogger(
			SharedNameResultBeanFactory.class);
	
	public static final String MATCH_RESULT_TYPE_PROPERTY = "matchResultType";
	
	public static final String DEFAULT_X_PROPERTY_PREFIX = "x";
	
	public static final String DEFAULT_Y_PROPERTY_PREFIX = "y";
	
	public static final String COMPARISON_PROPERTY_SUFFIX = "Comparison";
	
	private final String xPropertyPrefix;
	
	private final String yPropertyPrefix;
	
	public SharedNameResultBeanFactory(
			String xPropertyPrefix, String yPropertyPrefix) {

        this.xPropertyPrefix = Objects.requireNonNullElse(xPropertyPrefix, DEFAULT_X_PROPERTY_PREFIX);
        this.yPropertyPrefix = Objects.requireNonNullElse(yPropertyPrefix, DEFAULT_Y_PROPERTY_PREFIX);
	}	
	
	protected String xify(String propertyName) {
		return xPropertyPrefix + upperCaseFirstLetter(propertyName);
	}
	
	protected String yify(String propertyName) {
		return yPropertyPrefix + upperCaseFirstLetter(propertyName);
	}
	
	@Override
	protected final void populateMatchResultType(Object resultBean,
			MatchResultType matchResultType) {
		
		switch (matchResultType) {
		case X_MISSING: 
			populateMatchResultType(resultBean, new MatchResultTypeText.XMissing() {
				@Override
				public String getText() {
					return xPropertyPrefix + "_MISSING";
				}
			});
			break;
		case Y_MISSING: 
			populateMatchResultType(resultBean, new MatchResultTypeText.YMissing() {
				@Override
				public String getText() {
					return yPropertyPrefix + "_MISSING";
				}
			});
			break;
		case NOT_EQUAL: 
			populateMatchResultType(resultBean, new MatchResultTypeText.NotEqual() {
				@Override
				public String getText() {
					return "NOT_EQUAL";
				}
			});
			break;
		case EQUAL: 
			populateMatchResultType(resultBean, new MatchResultTypeText.Equal() {
				@Override
				public String getText() {
					return "EQUAL";
				}
			});
			break;
		}
	}
	
	abstract protected void populateMatchResultType(Object resultBean,
			MatchResultTypeText matchResultType);
	
	/**
	 * Something that can prefix a property name.
	 */
	private interface Ifyer {
		/**
		 * Prefix the property name.
		 * 
		 * @param propertyName The property name.
		 * 
		 * @return The prefixed property name.
		 */
		String ify(String propertyName);
	}
	
	abstract protected class AbstractResultBeanClassProvider 
	implements ResultBeanClassProvider {
		
		private ArooaClass resultClass;
		
		private boolean haveX;
		
		private boolean haveY;
		
		@Override
		public final ArooaClass classForX(Matchable x) {
			
			if (haveX) {
				return resultClass;				
			}
			
			if (resultClass == null) {
				logger.debug("Creating Result Class based on [" + 
						xPropertyPrefix + "] types only.");
				
				resultClass = classForResult(x, x);
			}
			else {
				resultClass = mergeXintoY(x);
			}
			haveX = true;
			
			return resultClass;
		}
		
		@Override
		public final ArooaClass classForY(Matchable y) {
			
			if (haveY) {
				return resultClass;				
			}
			
			if (resultClass == null) {
				logger.debug("Creating Result Class based on [" + 
						yPropertyPrefix + "] types only.");
				
				resultClass = classForResult(y, y);
			}
			else {
				resultClass = mergeYintoX(y);
			}
			haveY = true;
			
			return resultClass;
		}
		
		@Override
		public final ArooaClass classForComparison(
				MultiValueComparison<Matchable> matchableComparison) {
			
			if (haveX && haveY) {
				return resultClass;
			}
			
			if (haveX) {
				resultClass = mergeYintoX(matchableComparison.getY());
			}
			else if (haveY) {
				resultClass = mergeXintoY(matchableComparison.getX());
			}
			else {
				logger.debug("Creating Result Class based on both [" + 
						xPropertyPrefix + "] and [" +
						yPropertyPrefix + "] types.");
				
				resultClass = classForResult(matchableComparison.getX(), 
						matchableComparison.getY());
			}
			
			haveX = true;
			haveY = true;
			
			return resultClass;
		}
		
		private ArooaClass mergeXintoY(Matchable x) {
			
			logger.debug("Merging [" + 
					xPropertyPrefix + "] types into Result Class.");
			
			return merge(
					new Ifyer() {
						public String ify(String propertyName) {
							return yify(propertyName);
						}
					},
					new Ifyer() {
						public String ify(String propertyName) {
							return xify(propertyName);
						}
					},
					x.getMetaData());
		}
		
		private ArooaClass mergeYintoX(Matchable y) {
			
			logger.debug("Merging [" + 
					yPropertyPrefix + "] types into Result Class.");
			
			return merge(
					new Ifyer() {
						public String ify(String propertyName) {
							return xify(propertyName);
						}
					},
					new Ifyer() {
						public String ify(String propertyName) {
							return yify(propertyName);
						}
					},
					y.getMetaData());
		}
		
		private ArooaClass merge(Ifyer existingIfyer, Ifyer newIfyer,
				MatchableMetaData metaData) {

			boolean change = false;
			
			MagicBeanClassCreator magicDef = new MagicBeanClassCreator(
					"MatchResultBean");
			
			magicDef.addProperty(MATCH_RESULT_TYPE_PROPERTY,
					classForResultType());
			
			BeanOverview overview = resultClass.getBeanOverview(
					getPropertyAccessor());
			
			for (String key : metaData.getKeyProperties()) {			
				
				Type existingType = overview.getPropertyType(key);
				Type newType = metaData.getPropertyType(key);
				
				Type commonType = new PropertyTypeHelper().typeFor(
						key, existingType, newType);

				magicDef.addProperty(key, TypeUtil.classOf(commonType));

				if (commonType != newType) {
					change = true;
				}
			}
			
			for (String propertyName : metaData.getValueProperties()) {
				
				if (inspect(propertyName, existingIfyer, newIfyer, 
						overview, metaData, magicDef)) {
					change = true;
				}
				
				magicDef.addProperty(propertyName + COMPARISON_PROPERTY_SUFFIX, 
						classForComparison());
			}
			
			for (String propertyName : metaData.getOtherProperties()) {
				
				if (inspect(propertyName, existingIfyer, newIfyer, 
						overview, metaData, magicDef)) {
					change = true;
				}
			}
			
			if (!change) {
				return resultClass;
			}
			
			return magicDef.create();
		}		
		
		private boolean inspect(String propertyName, 
				Ifyer existingIfyer, Ifyer newIfyer, 
				BeanOverview overview, MatchableMetaData metaData,
				MagicBeanClassCreator magicDef) {
			
			String existingPropertyName = existingIfyer.ify(propertyName);
			Class<?> existingType = overview.getPropertyType(
					existingPropertyName);

			// add the existing type back.
			magicDef.addProperty(existingPropertyName, existingType);
			
			Type newType = metaData.getPropertyType(propertyName);
			
			String newPropertyName = newIfyer.ify(propertyName);
			
			if (TypeUtil.isAssignableFrom(existingType, newType)) {
				magicDef.addProperty(newPropertyName, existingType);
				return false;
			}
			else {
				magicDef.addProperty(newPropertyName, TypeUtil.classOf(newType));
                logger.debug("Changing Types for property {} from {} to {}",
						newPropertyName, existingType.getName(), newType);
				return true;
			}
		}
		
		private ArooaClass classForResult(Matchable x, Matchable y) {
				
			MatchableMetaData xMetaData = x.getMetaData();
			MatchableMetaData yMetaData = y.getMetaData();
			
			MagicBeanClassCreator magicDef = new MagicBeanClassCreator(
					"MatchResultBean");
			
			magicDef.addProperty(MATCH_RESULT_TYPE_PROPERTY,
					classForResultType());
			
			for (String key : xMetaData.getKeyProperties()) {			

				Type xType = xMetaData.getPropertyType(key);
				Type yType = xMetaData.getPropertyType(key);

				Type type = new PropertyTypeHelper().typeFor(
						key, xType, yType);

                logger.debug("Key Property: {}, type {}", key, type);

				magicDef.addProperty(key, TypeUtil.classOf(type));
			}
			
			for (String propertyName : xMetaData.getValueProperties()) {
				
				String xProperty = xify(propertyName);
				Type xValueType = xMetaData.getPropertyType(propertyName);

                logger.debug("X Value Property: {}, type {}", xProperty, xValueType);

				magicDef.addProperty(xProperty, TypeUtil.classOf(xValueType));
				
				String yProperty = yify(propertyName);
				Type yValueType = yMetaData.getPropertyType(propertyName);

                logger.debug("Y Value Property: {}, type {}", yProperty, yValueType);

				magicDef.addProperty(yProperty, TypeUtil.classOf(yValueType));
				
				magicDef.addProperty(propertyName + COMPARISON_PROPERTY_SUFFIX, 
						classForComparison());
			}
			
			for (String propertyName : xMetaData.getOtherProperties()) {
				
				String xProperty = xify(propertyName);
				Type xValueType = xMetaData.getPropertyType(propertyName);

                logger.debug("X Other Property: {}, type {}", xProperty, xValueType);

				magicDef.addProperty(xProperty, TypeUtil.classOf(xValueType));
				
				String yProperty = yify(propertyName);
				Type yValueType = yMetaData.getPropertyType(propertyName);

                logger.debug("Y Other Property: {}, type {}", yProperty, yValueType);

				magicDef.addProperty(yProperty, TypeUtil.classOf(yValueType));
			}
			
			return magicDef.create();
		}
				
		abstract protected Class<?> classForComparison();
		
		abstract protected Class<?> classForResultType();
		
		abstract protected PropertyAccessor getPropertyAccessor();
	}
}
