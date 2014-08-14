package org.oddjob.beancmpr.beans;

import org.oddjob.arooa.design.DesignFactory;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignProperty;
import org.oddjob.arooa.design.DesignValueBase;
import org.oddjob.arooa.design.SimpleDesignProperty;
import org.oddjob.arooa.design.SimpleTextAttribute;
import org.oddjob.arooa.design.screem.BorderedGroup;
import org.oddjob.arooa.design.screem.Form;
import org.oddjob.arooa.design.screem.StandardForm;
import org.oddjob.arooa.parsing.ArooaContext;
import org.oddjob.arooa.parsing.ArooaElement;

/**
 * A {@link DesignFactory} for a {@link IterableBeansComparer}
 */
public class IterableBeansComparerDF implements DesignFactory {
	
	public DesignInstance createDesign(ArooaElement element,
			ArooaContext parentContext) {

		return new IterableBeansComparerDesign(element, parentContext);
	}
}

class IterableBeansComparerDesign extends DesignValueBase {

	private final SimpleTextAttribute keyProperties;
	
	private final SimpleTextAttribute valueProperties;
	
	private final SimpleTextAttribute otherProperties;
	
	private final SimpleTextAttribute sorted;
	
	private final SimpleDesignProperty comparersByType;
	
	private final SimpleDesignProperty comparersByName;
	
	
	public IterableBeansComparerDesign(ArooaElement element, ArooaContext parentContext) {
		super(element, parentContext);

		keyProperties = new SimpleTextAttribute("keys", this);
		
		valueProperties = new SimpleTextAttribute("values", this);
		
		otherProperties = new SimpleTextAttribute("others", this);
		
		sorted = new SimpleTextAttribute("sorted", this);
		
		comparersByType = new SimpleDesignProperty("comparersByType", this);
		
		comparersByName = new SimpleDesignProperty("comparersByName", this);
	}
	
	public Form detail() {
		return new StandardForm(this)
			.addFormItem(new BorderedGroup("Properties")
				.add(keyProperties.view().setTitle("Key Properties"))
				.add(valueProperties.view().setTitle("Value Properties"))
				.add(otherProperties.view().setTitle("Other Properties"))
				.add(sorted.view().setTitle("Sorted"))
				.add(comparersByType.view().setTitle("Comparers By Type"))
				.add(comparersByName.view().setTitle("Comparers By Name"))
			);					
	}
			
	@Override
	public DesignProperty[] children() {
		return new DesignProperty[] { 
				keyProperties, valueProperties, otherProperties, 
				sorted, comparersByName, comparersByType };
	}
}
