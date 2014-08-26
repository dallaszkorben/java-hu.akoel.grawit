package hu.akoel.grawit.core.parameter;

import hu.akoel.grawit.core.treenodedatamodel.elements.ParamElementDataModel;

public class VariableParameter implements EParameter{
	String name;
	ParamElementDataModel element;

	public VariableParameter( String name, ParamElementDataModel element ){
		this.name = name;
		this.element = element;
	}
	
	@Override
	public String getName() {		
		return name;
	}

	@Override
	public String getValue() {
		return element.getVariableValue();
	}

}
