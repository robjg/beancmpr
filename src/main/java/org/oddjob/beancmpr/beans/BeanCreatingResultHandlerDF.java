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
 * Design for {@link BeanCreatingResultHandler}.
 */
public class BeanCreatingResultHandlerDF implements DesignFactory {
	
	public DesignInstance createDesign(ArooaElement element,
			ArooaContext parentContext) {

		return new BeanCreatingResultDesign(element, parentContext);
	}
}

class BeanCreatingResultDesign extends DesignValueBase {

	private final SimpleDesignProperty out;
	
	private final SimpleTextAttribute xPropertyPrefix;
	
	private final SimpleTextAttribute yPropertyPrefix;
	
	private final SimpleTextAttribute ignoreMatches;
	
	public BeanCreatingResultDesign(ArooaElement element, ArooaContext parentContext) {
		super(element, parentContext);
		
		out = new SimpleDesignProperty("out", this);
		
		xPropertyPrefix = new SimpleTextAttribute("xPropertyPrefix", this);
		
		yPropertyPrefix = new SimpleTextAttribute("yPropertyPrefix", this);
		
		ignoreMatches = new SimpleTextAttribute("ignoreMatches", this);
	}
	
	public Form detail() {
		return new StandardForm(this).addFormItem(
				new BorderedGroup("Properties")				
			.add(out.view().setTitle("Out"))
			.add(xPropertyPrefix.view().setTitle("X Property Prefix"))
			.add(yPropertyPrefix.view().setTitle("Y Property Prefix"))
			.add(ignoreMatches.view().setTitle("Ignore Matches"))
		);
	}

	@Override
	public DesignProperty[] children() {
		return new DesignProperty[] { out, 
				xPropertyPrefix, yPropertyPrefix,
				ignoreMatches, ignoreMatches };
	}
}
