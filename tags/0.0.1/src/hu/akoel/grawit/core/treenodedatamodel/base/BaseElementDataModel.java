package hu.akoel.grawit.core.treenodedatamodel.base;

import javax.swing.tree.MutableTreeNode;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.BaseDataModelInterface;
import hu.akoel.grawit.enums.SelectorType;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.enums.list.ElementTypeListEnum;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;
import hu.akoel.grawit.exceptions.XMLWrongAttributePharseException;

public class BaseElementDataModel extends BaseDataModelInterface{
	private static final long serialVersionUID = -8916078747948054716L;

	public static Tag TAG = Tag.BASEELEMENT;
	
	public static final String ATTR_ELEMENT_TYPE="elementtype";
	public static final String ATTR_IDENTIFIER = "identifier";
	public static final String ATTR_IDENTIFICATION_TYPE = "identificationtype";
	public static final String ATTR_FRAME = "frame";
//	public static final String ATTR_VARIABLE_SAMPLE = "variablesample";	
	
	//Adatmodel ---
	private String name;
	private ElementTypeListEnum elementType;
//	private VariableSampleListEnum variableSample;
	private String frame;
	private String identifier;
	private SelectorType identificationType;
	//----
	
	//Ide menti az erre a mezore hivatkozo ParamElement Mezo mentett erteket
	private String variableValue = "";
	//---

	/**
	 * 
	 * Modify
	 * 
	 * @param name
	 * @param elementType
	 * @param identifier
	 * @param identificationType
	 * @param variableSample
	 * @param frame
	 */
	//public BaseElementDataModel(String name, ElementTypeListEnum elementType, String identifier, SelectorType identificationType, VariableSampleListEnum variableSample, String frame){
	public BaseElementDataModel(String name, ElementTypeListEnum elementType, String identifier, SelectorType identificationType, String frame){
		//common( name, elementType, identifier, identificationType, variableSample, frame );	
		common( name, elementType, identifier, identificationType, frame );
	}

	/**
	 * Capture new
	 * 
	 * XML alapjan gyartja le a BASEELEMENT-et
	 * 
	 * @param element
	 * @throws XMLPharseException 
	 */
	public BaseElementDataModel( Element element ) throws XMLPharseException{
		
		//name
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME );			
		}
		String nameString = element.getAttribute( ATTR_NAME );		
		this.name = nameString;
		
		//frame             
		if( !element.hasAttribute( ATTR_FRAME ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_FRAME );			
		}
		String frameString = element.getAttribute( ATTR_FRAME );
		this.frame = frameString;
		
		//identifier             
		if( !element.hasAttribute( ATTR_IDENTIFIER ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_IDENTIFIER );			
		}
		String identifierString = element.getAttribute( ATTR_IDENTIFIER );
		this.identifier = identifierString;
		
		//element type             
		if( !element.hasAttribute( ATTR_ELEMENT_TYPE ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_ELEMENT_TYPE );			
		}
		String elementTypeString = element.getAttribute( ATTR_ELEMENT_TYPE );
		this.elementType = ElementTypeListEnum.valueOf( elementTypeString );
		
		//identificationtype
		if( !element.hasAttribute( ATTR_IDENTIFICATION_TYPE ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_IDENTIFICATION_TYPE );
		}
		String identificationTypeString = element.getAttribute( ATTR_IDENTIFICATION_TYPE );
		if( SelectorType.ID.name().equals( identificationTypeString ) ){
			identificationType = SelectorType.ID;
		}else if( SelectorType.CSS.name().equals( identificationTypeString ) ){
			identificationType = SelectorType.CSS;
		}else{			
			throw new XMLWrongAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_IDENTIFICATION_TYPE, identificationTypeString ); 
		}
		
/*		//variablesemple
		String variablesempleString = element.getAttribute(ATTR_VARIABLE_SAMPLE);
		if( nameString.isEmpty() ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_VARIABLE_SAMPLE );			
		}		
*/
/*		if( VariableSample.NO.name().equals(variablesempleString)){
			variableSample = VariableSample.NO;
		}else 
*/
/*		if( VariableSampleListEnum.PRE.name().equals(variablesempleString)){
			variableSample = VariableSampleListEnum.PRE;
		}else if( VariableSampleListEnum.POST.name().equals(variablesempleString)){
			variableSample = VariableSampleListEnum.POST;
		}else{
			throw new XMLWrongAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_VARIABLE_SAMPLE, variablesempleString );
		}
*/				
	}
	
	//private void common( String name, ElementTypeListEnum elementType, String identifier, SelectorType identificationType, VariableSampleListEnum variableSample, String frame ){
	private void common( String name, ElementTypeListEnum elementType, String identifier, SelectorType identificationType, String frame ){
		this.name = name;
		this.elementType = elementType;
		this.identifier = identifier;
		this.identificationType = identificationType;
//		this.variableSample = variableSample;
		this.frame = frame;
	}

	public static Tag getTagStatic(){
		return TAG;
	}

	@Override
	public Tag getTag() {
		return getTagStatic();
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ElementTypeListEnum getElementType(){
		return elementType;
	}
	
	public void setElementType( ElementTypeListEnum elementType ){
		this.elementType = elementType;
	}
	
	public String getSelector() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public SelectorType getSelectorType() {
		return identificationType;
	}

	public void setIdentificationType(SelectorType identificationType) {
		this.identificationType = identificationType;
	}

/*	public VariableSampleListEnum getVariableSample() {
		return variableSample;
	}

	public void setVariableSample(VariableSampleListEnum variableSample) {
		this.variableSample = variableSample;
	}
*/
	public String getFrame(){
		return frame;
	}
	
	public void setFrame( String frame ){
		this.frame = frame;
	}
	
	@Override
	public void add(BaseDataModelInterface node) {
		super.add( (MutableTreeNode)node );
	}
	
	public static String  getModelNameToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.base.element");
	}
	
	@Override
	public String getNodeTypeToShow(){
		return getModelNameToShowStatic();
	}
	
	/**
	 * 
	 * Visszaadja a valtozokent, az osztaly altal reprezentalt elem tartalmat elmentett erteket
	 * 
	 * @return
	 */
	public String getVariableValue() {
		return variableValue;
	}

	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}
	
	@Override
	public Element getXMLElement(Document document) {
		Attr attr;

		//Node element
		Element elementElement = document.createElement( BaseElementDataModel.this.getTag().getName() );
		attr = document.createAttribute( ATTR_NAME );
		attr.setValue( getName() );
		elementElement.setAttributeNode(attr);	
		
		attr = document.createAttribute( ATTR_FRAME);
		attr.setValue( getFrame() );
		elementElement.setAttributeNode(attr);	

/*		attr = document.createAttribute( ATTR_VARIABLE_SAMPLE);
		attr.setValue( getVariableSample().name() );
		elementElement.setAttributeNode(attr);
*/		
		attr = document.createAttribute( ATTR_IDENTIFIER );
		attr.setValue( getSelector() );
		elementElement.setAttributeNode(attr);	
		
		attr = document.createAttribute( ATTR_ELEMENT_TYPE );
		attr.setValue( getElementType().name() );
		elementElement.setAttributeNode(attr);	

		attr = document.createAttribute( ATTR_IDENTIFICATION_TYPE );
		attr.setValue( getSelectorType().name() );
		elementElement.setAttributeNode(attr);	

		return elementElement;	
	}


}