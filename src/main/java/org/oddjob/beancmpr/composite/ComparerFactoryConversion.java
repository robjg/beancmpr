package org.oddjob.beancmpr.composite;

import org.oddjob.arooa.convert.ConversionProvider;
import org.oddjob.arooa.convert.ConversionRegistry;
import org.oddjob.arooa.convert.Convertlet;
import org.oddjob.arooa.convert.ConvertletException;
import org.oddjob.beancmpr.Comparer;

public class ComparerFactoryConversion 
implements ConversionProvider {

	@SuppressWarnings("rawtypes")
	@Override
	public void registerWith(ConversionRegistry registry) {
		registry.register(Comparer.class, ComparerFactory.class,
				new Convertlet<Comparer, ComparerFactory>() {
					@SuppressWarnings("unchecked")
					@Override
					public ComparerFactory convert(Comparer from)
							throws ConvertletException {
						
						return new ComparerFactoryAdaptor(from);
					}
				});
	}
}
