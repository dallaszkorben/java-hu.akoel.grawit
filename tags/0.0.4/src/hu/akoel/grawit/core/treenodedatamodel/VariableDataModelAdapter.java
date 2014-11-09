package hu.akoel.grawit.core.treenodedatamodel;

import hu.akoel.grawit.enums.Tag;

public abstract class VariableDataModelAdapter extends DataModelAdapter{

	private static final long serialVersionUID = 5051738894429183426L;

	public abstract void add( VariableDataModelAdapter node );
	
	public static Tag getRootTag(){
		return Tag.VARIABLEROOT;
	}
}