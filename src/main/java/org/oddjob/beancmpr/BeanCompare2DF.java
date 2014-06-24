package org.oddjob.beancmpr;

import org.oddjob.arooa.design.DesignComponentBase;
import org.oddjob.arooa.design.DesignFactory;
import org.oddjob.arooa.design.DesignInstance;
import org.oddjob.arooa.design.DesignProperty;
import org.oddjob.arooa.design.SimpleDesignProperty;
import org.oddjob.arooa.design.SimpleTextAttribute;
import org.oddjob.arooa.design.screem.BorderedGroup;
import org.oddjob.arooa.design.screem.Form;
import org.oddjob.arooa.design.screem.StandardForm;
import org.oddjob.arooa.parsing.ArooaContext;
import org.oddjob.arooa.parsing.ArooaElement;

/**
 * {@link DesignFactory} for {@link BeanCompareJob}
 */
public class BeanCompare2DF implements DesignFactory {
	
	public DesignInstance createDesign(ArooaElement element,
			ArooaContext parentContext) {

		return new BeanCompareDesign2(element, parentContext);
	}
}

class BeanCompareDesign2 extends DesignComponentBase {

	private final SimpleTextAttribute name;
	
	private final SimpleDesignProperty inX;
	
	private final SimpleDesignProperty inY;
	
	private final SimpleDesignProperty results;
	
	private final SimpleDesignProperty comparer;
	
	
	public BeanCompareDesign2(ArooaElement element, ArooaContext parentContext) {
		super(element, parentContext);

		name = new SimpleTextAttribute("name", this);
				
		inX = new SimpleDesignProperty("inX", this);

		inY = new SimpleDesignProperty("inY", this);

		results = new SimpleDesignProperty("results", this);
		
		comparer = new SimpleDesignProperty("comparer", this);
		
	}
	
	public Form detail() {
		return new StandardForm(this)
			.addFormItem(new BorderedGroup("General")
				.add(name.view().setTitle("Name"))
			    )
			.addFormItem(new BorderedGroup("Properties")
				.add(inX.view().setTitle("In X"))
				.add(inY.view().setTitle("In Y"))
				.add(comparer.view().setTitle("Comparer"))
				.add(results.view().setTitle("Results"))
			);					
	}
			
	@Override
	public DesignProperty[] children() {
		return new DesignProperty[] { name, 
				inX, inY,
				comparer, results };
	}
}
