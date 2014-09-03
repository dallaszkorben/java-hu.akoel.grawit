package hu.akoel.grawit.core.treenodedatamodel.testcase;

import java.io.StringReader;

import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.BaseDataModelInterface;
import hu.akoel.grawit.core.treenodedatamodel.ParamDataModelInterface;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseNodeDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.BasePageDataModel;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamNodeDataModel;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamPageDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.XMLBaseConversionPharseException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class TestcasePageDataModel extends TestcaseDataModelInterface{

	private static final long serialVersionUID = 5313170692938571481L;

	public static final Tag TAG = Tag.TESTCASEPAGE;
	
	public static final String ATTR_DETAILS = "details";
	public static final String ATTR_PARAM_PAGE_PATH = "parampagepath";
	
	private String name;
	private String details;
	private ParamPageDataModel paramPage;
	
	public TestcasePageDataModel( String name, String details, ParamPageDataModel paramPage ){
		super( );
		this.name = name;
		this.details = details;
		this.paramPage = paramPage;
	}
	
	/**
	 * XML alapjan legyartja a TESTCASEPAGE-et es az alatta elofordulo 
	 * 
	 * @param element
	 * @throws XMLMissingAttributePharseException 
	 */
	public TestcasePageDataModel( Element element, ParamDataModelInterface paramDataModel ) throws XMLPharseException{
		
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( TestcasePageDataModel.getRootTag(), Tag.TESTCASEPAGE, ATTR_NAME );			
		}
		String nameString = element.getAttribute( ATTR_NAME );
		this.name = nameString;
		
		if( !element.hasAttribute( ATTR_DETAILS ) ){
			throw new XMLMissingAttributePharseException( TestcasePageDataModel.getRootTag(), Tag.TESTCASEPAGE, ATTR_NAME, getName(), ATTR_DETAILS );			
		}		
		String detailsString = element.getAttribute( ATTR_DETAILS );		
		this.details = detailsString;
		
		
		
		
		
		
		
		
		
		
		
		if( !element.hasAttribute( ATTR_PARAM_PAGE_PATH ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH );			
		}	
		String paramElementPathString = element.getAttribute(ATTR_PARAM_PAGE_PATH);				
		paramElementPathString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + paramElementPathString;  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder;
	    Document document = null;
	    try  
	    {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse( new InputSource( new StringReader( paramElementPathString ) ) );  
	    } catch (Exception e) {  
	    
	    	//Nem sikerult az atalakitas
	    	throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH, element.getAttribute(ATTR_PARAM_PAGE_PATH), e );
	    } 
	    
	    
	    //Megkeresem a PARAMPAGEROOT-ben a PARAMPAGE-hez vezeto utat
	    Node actualNode = document;
	    while( actualNode.hasChildNodes() ){
		
	    	actualNode = actualNode.getFirstChild();
	    	Element actualElement = (Element)actualNode;
	    	String tagName = actualElement.getTagName();
	    	String attrName = null;
	    	
	    	//Ha PARAMNODE
	    	if( tagName.equals( ParamNodeDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(ParamNodeDataModel.ATTR_NAME);	    		
	    		paramDataModel = (ParamDataModelInterface) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.PARAMNODE, attrName );

	    		if( null == paramDataModel ){

	    			throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH, element.getAttribute(ATTR_PARAM_PAGE_PATH) );
	    		}
	    		
	    	//Ha PARAMPAGE
	    	}else if( tagName.equals( ParamPageDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(ParamPageDataModel.ATTR_NAME);
	    		paramDataModel = (ParamDataModelInterface) CommonOperations.getDataModelByNameInLevel( paramDataModel, Tag.PARAMPAGE, attrName );
	    		if( null == paramDataModel ){

	    			throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH, element.getAttribute(ATTR_PARAM_PAGE_PATH) );
	    		}
	    		
	    	//Ha PARAMELEMENT - ez nem lehet, torold ki ezt a feltetelt
	    	}else if( tagName.equals( ParamElementDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(ParamElementDataModel.ATTR_NAME);

	    		throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH, element.getAttribute(ATTR_PARAM_PAGE_PATH) );	    		
	    	}else{
	    		
	    		throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH, element.getAttribute(ATTR_PARAM_PAGE_PATH) );	    		
	    	}
	    }	    
	    try{
	    	
	    	paramPage = (ParamPageDataModel)paramDataModel;
	    	
	    }catch(ClassCastException e){

	    	//Nem sikerult az utvonalat megtalalni
	    	throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_PARAM_PAGE_PATH, element.getAttribute(ATTR_PARAM_PAGE_PATH), e );
	    } 
	    

	}
	
	public static Tag getTagStatic(){
		return TAG;
	}

	@Override
	public Tag getTag() {
		return getTagStatic();
	}

	@Override
	public void add(TestcaseDataModelInterface node) {
		super.add( (MutableTreeNode)node );
	}
	
	public static String  getModelNameToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.testcase.page");
	}
	
	@Override
	public String getModelNameToShow(){
		return getModelNameToShowStatic();
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	public String getDetails(){
		return details;
	}
	
	public void setDetails( String details ){
		this.details = details;
	}
	
	public void setName( String name ){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}

	public void setParamPage( ParamPageDataModel paramPage ){
		this.paramPage = paramPage;		
	}
	
	public ParamPageDataModel getParamPage(){
		return paramPage;
	}
	
	@Override
	public Element getXMLElement(Document document) {
		Attr attr;
		
		//Node element
		Element nodeElement = document.createElement( TestcasePageDataModel.this.getTag().getName() );
		attr = document.createAttribute( ATTR_NAME );
		attr.setValue( getName() );
		nodeElement.setAttributeNode(attr);	
		
		attr = document.createAttribute( ATTR_DETAILS );
		attr.setValue( getDetails() );
		nodeElement.setAttributeNode(attr);	
		
		attr = document.createAttribute( ATTR_PARAM_PAGE_PATH );
		attr.setValue( paramPage.getPathTag() );
		nodeElement.setAttributeNode( attr );
			
		return nodeElement;		
	}

}
