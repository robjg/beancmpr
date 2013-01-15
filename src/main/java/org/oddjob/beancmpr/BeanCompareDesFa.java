package org.oddjob.beancmpr;

import org.oddjob.arooa.design.DesignComponentBase;
import org.oddjob.arooa.design.DesignFactory;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignProperty;
import org.oddjob.arooa.design.SimpleDesignProperty;
import org.oddjob.arooa.design.SimpleTextAttribute;
import org.oddjob.arooa.design.screem.BorderedGroup;
import org.oddjob.arooa.design.screem.FieldGroup;
import org.oddjob.arooa.design.screem.Form;
import org.oddjob.arooa.design.screem.StandardForm;
import org.oddjob.arooa.design.screem.TabGroup;
import org.oddjob.arooa.parsing.ArooaContext;
import org.oddjob.arooa.parsing.ArooaElement;

/**
 * {@link DesignFactory} for {@link BeanCompareJob}
 */
public class BeanCompareDesFa implements DesignFactory {
	
	public DesignInstance createDesign(ArooaElement element,
			ArooaContext parentContext) {

		return new BeanCompareDesign(element, parentContext);
	}
}

class BeanCompareDesign extends DesignComponentBase {

	private final SimpleTextAttribute name;

	private final SimpleTextAttribute keyProperties;
	
	private final SimpleTextAttribute valueProperties;
	
	private final SimpleTextAttribute otherProperties;
	
	private final SimpleDesignProperty inX;
	
	private final SimpleDesignProperty inY;
	
	private final SimpleDesignProperty out;
	
	private final SimpleTextAttribute xPropertyPrefix;
	
	private final SimpleTextAttribute yPropertyPrefix;
	
	private final SimpleTextAttribute sorted;
	
	private final SimpleDesignProperty comparersByType;
	
	private final SimpleDesignProperty comparersByProperty;
	
	
	public BeanCompareDesign(ArooaElement element, ArooaContext parentContext) {
		super(element, parentContext);

		name = new SimpleTextAttribute("name", this);
		
		keyProperties = new SimpleTextAttribute("keyProperties", this);
		
		valueProperties = new SimpleTextAttribute("valueProperties", this);
		
		otherProperties = new SimpleTextAttribute("otherProperties", this);
		
		inX = new SimpleDesignProperty("inX", this);

		inY = new SimpleDesignProperty("inY", this);

		out = new SimpleDesignProperty("out", this);
		
		sorted = new SimpleTextAttribute("sorted", this);
		
		xPropertyPrefix = new SimpleTextAttribute("xPropertyPrefix", this);
		
		yPropertyPrefix = new SimpleTextAttribute("yPropertyPrefix", this);
		
		comparersByType = new SimpleDesignProperty("comparersByType", this);
		
		comparersByProperty = new SimpleDesignProperty("comparersByProperty", this);
		
	}
	
	public Form detail() {
		return new StandardForm(this)
			.addFormItem(new BorderedGroup("General")
				.add(name.view().setTitle("Name"))
			    )
			.addFormItem(new BorderedGroup("Properties")
				.add(keyProperties.view().setTitle("Key Properties"))
				.add(valueProperties.view().setTitle("Value Properties"))
				.add(otherProperties.view().setTitle("Other Properties"))
				)
			.addFormItem(
				new TabGroup()
					.add(new FieldGroup("Beans")
						.add(inX.view().setTitle("In X"))
						.add(inY.view().setTitle("In Y"))
						.add(out.view().setTitle("Out"))
						.add(sorted.view().setTitle("Sorted"))
					)
					.add(new FieldGroup("Prefixes")
						.add(xPropertyPrefix.view().setTitle("X Property Prefix"))
						.add(yPropertyPrefix.view().setTitle("Y Property Prefix"))
					)
					.add(new FieldGroup("Comparers")
						.add(comparersByType.view().setTitle("Comparers By Type"))
						.add(comparersByProperty.view().setTitle("Comparers By Property"))
					)
				);					
	}
			
	@Override
	public DesignProperty[] children() {
		return new DesignProperty[] { name, 
				keyProperties, valueProperties, otherProperties, 
				inX, inY, out, sorted,
				xPropertyPrefix, yPropertyPrefix,
				comparersByProperty, comparersByType };
	}
}
