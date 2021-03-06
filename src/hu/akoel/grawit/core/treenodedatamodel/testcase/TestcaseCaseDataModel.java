package hu.akoel.grawit.core.treenodedatamodel.testcase;

import java.io.StringReader;

import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.constant.ConstantRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.driver.DriverDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.step.StepCollectorDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.step.StepDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.step.StepElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.step.StepFolderDataModel;
import hu.akoel.grawit.core.treenodedatamodel.step.StepLoopCollectorDataModel;
import hu.akoel.grawit.core.treenodedatamodel.step.StepNormalCollectorDataModel;
import hu.akoel.grawit.core.treenodedatamodel.step.StepRootDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.XMLBaseConversionPharseException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TestcaseCaseDataModel extends TestcaseNodeDataModelAdapter{

	private static final long serialVersionUID = -2139557326147525999L;

	public static final Tag TAG = Tag.TESTCASECASE;
	
	public static final String ATTR_LAST_SELECTED_STEP_COLLECTOR_PATH = "laststepcollectorpath";
	public static final String ATTR_DETAILS = "details";
	private static final String ATTR_ON = "on";
	
	private StepCollectorDataModelAdapter lastStepCollector = null;	
	
	public TestcaseCaseDataModel( String name, String details ){			
		super( name, details );
		
		//Engedelyezi a Node Ki/Be kapcsolasat
		this.setEnabledToTurnOnOff( true );
		
	}
	
	/**
	 * XML alapjan legyartja a TESTCASECASE-ot es az alatta elofordulo 
	 * TESTCASECASE-okat, illetve TESTCASEPAGE-eket
	 * 
	 * @param element
	 * @throws XMLMissingAttributePharseException 
	 */
	public TestcaseCaseDataModel( Element element, ConstantRootDataModel constantRootDataModel, BaseRootDataModel baseRootDataModel, StepRootDataModel paramRootDataModel, DriverDataModelAdapter driverDataModel ) throws XMLPharseException{
		super( element, constantRootDataModel, baseRootDataModel, paramRootDataModel, driverDataModel );
		
		//Engedelyezi a Node Ki/Be kapcsolasat
		this.setEnabledToTurnOnOff( true );
	
		//========
		//
		// On
		//
		//========		
		if( !element.hasAttribute( ATTR_ON ) ){
			this.setOn( Boolean.TRUE );
		}else{
			String enabledString = element.getAttribute( ATTR_ON );
			this.setOn( Boolean.parseBoolean( enabledString ));
		}				
			
		try{
			lastStepCollector = (StepCollectorDataModelAdapter) getParamDataModelFromPath(element, paramRootDataModel, TAG, getName() );
		}catch (XMLBaseConversionPharseException e){
			lastStepCollector = null;
		}		
		
	    //========
		//
		// Gyermekei
		//
	    //========
		NodeList nodelist = element.getChildNodes();
		for( int i = 0; i < nodelist.getLength(); i++ ){
			Node node = nodelist.item( i );
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element testcaseElement = (Element)node;
				
				//Ha TESTCASEPARAMCONTAINER van alatta
				if( testcaseElement.getTagName().equals( Tag.TESTCASESTEPCOLLECTOR.getName() )){
					
					this.add(new TestcaseStepCollectorDataModel(testcaseElement, constantRootDataModel, baseRootDataModel, paramRootDataModel, driverDataModel ));
					
				}
			}
		}
	}
	
	public StepCollectorDataModelAdapter getLastStepCollector(){
		return lastStepCollector;
	}
	
	public void setLastParamCollector( StepCollectorDataModelAdapter lastParamCollector ){
		this.lastStepCollector = lastParamCollector;
	}
	
	public static Tag getTagStatic(){
		return TAG;
	}

	@Override
	public Tag getTag() {
		return getTagStatic();
	}

	@Override
	public void add(TestcaseDataModelAdapter node) {
		super.add( (MutableTreeNode)node );
	}
	
	public static String  getModelNameToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.testcase.case");
	}
	
	@Override
	public String getNodeTypeToShow(){
		return getModelNameToShowStatic();
	}
	
	@Override
	public Element getXMLElement(Document document) {
		Attr attr;
		
//TODO meg kellene csinalni, hogy meghivja a super()-t
		
		//========
		//
		//Node element
		//
		//========
		Element nodeElement = document.createElement( TestcaseCaseDataModel.this.getTag().getName() );
		attr = document.createAttribute( ATTR_NAME );
		attr.setValue( getName() );
		nodeElement.setAttributeNode(attr);	
		
		//========
		//
		// On
		//
		//========
		attr = document.createAttribute( ATTR_ON );
		attr.setValue( this.isOn().toString() );
		nodeElement.setAttributeNode(attr);
		
		//========
		//
		// Details
		//
		//========
		attr = document.createAttribute( ATTR_DETAILS );
		attr.setValue( getDetails() );
		nodeElement.setAttributeNode(attr);	
		
		//========
		//
		//LASTBASEELEMENT attributum
		//
		//========
		attr = document.createAttribute( ATTR_LAST_SELECTED_STEP_COLLECTOR_PATH );
		if( null != lastStepCollector){
			attr.setValue( lastStepCollector.getPathTag() );
		}
		nodeElement.setAttributeNode(attr);
		
		//========
		//
		// Gyermekek
		//
		//========
		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof TestcaseDataModelAdapter ){
				
				Element element = ((TestcaseDataModelAdapter)object).getXMLElement( document );
				nodeElement.appendChild( element );		    		
		    	
			}
		}
	
		return nodeElement;		
	}
	
	public static StepDataModelAdapter getParamDataModelFromPath(Element element, StepRootDataModel paramRootDataModel, Tag tag, String name ) throws XMLBaseConversionPharseException{
		
		String attribute = ATTR_LAST_SELECTED_STEP_COLLECTOR_PATH;
		StepDataModelAdapter paramDataModel = paramRootDataModel;
		
		//Nincs megadva eleresi utvonal egyaltalan
		if( !element.hasAttribute( attribute ) ){
			
			return null;
			
		//Van utvonal
		}else{
		
			String paramPagePathString = element.getAttribute(attribute);
			
			if( paramPagePathString.trim().isEmpty() ){
				
				//Else [Fatal Error] :-1:-1: Premature end of file.  
				return null;
			}
			
			paramPagePathString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + paramPagePathString;  
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder; 
			Document document = null;
			try {
				
				//attributum-kent tarolt utvonal atalakitasa Documentum-ma
				builder = factory.newDocumentBuilder();  
				
				
				StringReader sr = new StringReader( paramPagePathString );
				InputSource is = new InputSource( sr );
				
				document = builder.parse( new InputSource( new StringReader( paramPagePathString ) ) ); 
     
			} catch (Exception e) {	    	
				
				//Nem sikerult az atalakitas
				throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute), e );
	    	
			} 
			
			//Megkeresem a PARAMROOT-ben a PARAMELEMENT-hez vezeto utat
			Node actualNode = document;
			while( actualNode.hasChildNodes() ){
	
				actualNode = actualNode.getFirstChild();
				Element actualElement = (Element)actualNode;
				String tagName = actualElement.getTagName();
				String attrName = null;
	    	
				//Ha BASEFOLDER
				if( tagName.equals( StepFolderDataModel.TAG.getName() ) ){
					attrName = actualElement.getAttribute(StepFolderDataModel.ATTR_NAME);	    		
					paramDataModel = (StepDataModelAdapter) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.STEPFOLDER, attrName );

					if( null == paramDataModel ){

						throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute) );
					}
	    		
				//Ha PARAMNORMALCOLLECTOR
				}else if( tagName.equals( StepNormalCollectorDataModel.TAG.getName() ) ){
					attrName = actualElement.getAttribute(StepNormalCollectorDataModel.ATTR_NAME);
					paramDataModel = (StepDataModelAdapter) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.STEPNORMALELEMENTCOLLECTOR, attrName );
					if( null == paramDataModel ){

						throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute) );
					}
	    
				//Ha PARAMLOOPCOLLECTOR
				}else if( tagName.equals( StepLoopCollectorDataModel.TAG.getName() ) ){
					attrName = actualElement.getAttribute(StepLoopCollectorDataModel.ATTR_NAME);
					paramDataModel = (StepDataModelAdapter) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.STEPLOOPELEMENTCOLLECTOR, attrName );
					if( null == paramDataModel ){

						throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute) );
					}
				
				//Ha PARAMLOOPCOLLECTOR
				}else if( tagName.equals( StepLoopCollectorDataModel.TAG.getName() ) ){
					attrName = actualElement.getAttribute(StepLoopCollectorDataModel.ATTR_NAME);
					paramDataModel = (StepDataModelAdapter) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.STEPLOOPELEMENTCOLLECTOR, attrName );
					if( null == paramDataModel ){

						throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute) );
					}
						
				//Ha PARAMELEMENT
				}else if( tagName.equals( StepElementDataModel.TAG.getName() ) ){
					attrName = actualElement.getAttribute(StepElementDataModel.ATTR_NAME);						    		
	    	
					paramDataModel = (StepDataModelAdapter) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.STEPELEMENT, attrName );
					if( null == paramDataModel ){

						throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute) );
					}
					
				}else{
	    		
					throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute) );	    		
				}	    	
	    	
			}	    
			try{				
				
				//return (ParamElementDataModelAdapter)paramDataModel;
				return paramDataModel;
	    	
			}catch(ClassCastException e){

				//Nem sikerult az utvonalat megtalalni
				throw new XMLBaseConversionPharseException( getRootTag(), tag, ATTR_NAME, name, attribute, element.getAttribute(attribute), e );
			}
				
		}
		
	}
	
}
