package hu.akoel.grawet.element;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import hu.akoel.grawet.operation.ElementOperation;


public class ParameterizedElement{
	private ElementOperation operation;
	private PureElement element;
	
	private String variableValue = "";
	

	public ParameterizedElement( PureElement element, ElementOperation operation){
		this.element = element;
		this.operation = operation;
	}
	
/*	public ParameterizedElement(WebDriver driver, String name, By by, boolean isVariable, VariableSample variableSample, ElementOperation operation ) {
		super( driver, name, by, isVariable, variableSample );
		
		this.operation = operation;
	}
*/
	/**
	 * 
	 * Executes the defined Operation with the defined Parameter
	 * 
	 */
	public void doAction(){
		this.getOperation().doAction( this );
	}
	
	public PureElement getElement(){
		return element;
	}	
	
	public ElementOperation getOperation() {
		return operation;
	}

	public void setOperation(ElementOperation operation) {
		this.operation = operation;
	}	
	
	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}

	/**
	 * 
	 * Akkor egyenlo a ket objektum, ha azonosak, vagy azonos az element tagjuk
	 * 
	 */
	public boolean equals( Object obj ){
		if( null == obj ){
			return false;
		}
		if( !( obj instanceof ParameterizedElement )){
			return false;
		}
		if( obj == this ){
			return true;
		}
		
		ParameterizedElement eo = (ParameterizedElement)obj;
		if( eo.getElement() != this.getElement() ){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * Azonos Hash erteket kap minden objektum melynek azonos az elemet tagja
	 * 
	 */
	public int hashCode() {
        return new HashCodeBuilder(17, 31).
            append(element).
            toHashCode();
    }
}

