package hu.akoel.grawit.core.treenodedatamodel.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.DriverDataModelInterface;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.XMLCastPharseException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLMissingTagPharseException;

public class DriverExplorerCapabilityDataModel extends DriverDataModelInterface{

	private static final long serialVersionUID = 2921936584954476404L;

	public static Tag TAG = Tag.DRIVEREXPLORERCAPABILITY;
	
//	public static final String ATTR_KEY = "key";
	public static final String ATTR_VALUE = "value";
	
	private String key;
	private String value;
	
	public DriverExplorerCapabilityDataModel( String name, String value ){
		super( );
		this.key = name;
		this.value = value;
	}
	
	public DriverExplorerCapabilityDataModel( Element element ) throws XMLMissingAttributePharseException, XMLMissingTagPharseException, XMLCastPharseException{
		
		//tag
		if( !element.getTagName().equals( TAG.getName() ) ){
			Element parentElement = (Element)element.getParentNode();
			throw new XMLMissingTagPharseException( getRootTag().getName(), parentElement.getTagName(), parentElement.getAttribute( ATTR_NAME ), TAG.getName() );
		}	
		
		//Key
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME );			
		}
		this.key = element.getAttribute( ATTR_NAME );		
		
		//Value
		if( !element.hasAttribute( ATTR_VALUE ) ){
			throw new XMLMissingAttributePharseException( DriverNodeDataModel.getRootTag(), Tag.DRIVERFIREFOXPROPERTY, ATTR_NAME, getName(), ATTR_VALUE );			
		}		
		this.value = element.getAttribute( ATTR_VALUE );		

	}
	
	public static String getModelNameToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.driver.firefox.property" );
	}
	
	@Override
	public String getModelNameToShow(){
		return getModelNameToShowStatic();
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
		return key;
	}

	public void setName( String name ){
		this.key = name;
	}
/*	public String getKey(){
		return key;
	}
	
	public void setKey( String key ){
		this.key = key;
	}
*/
	public String getValue(){
		return value;
	}
	
	public void setValue( String value ){
		this.value = value;
	}

	@Override
	public Element getXMLElement(Document document) {
		Attr attr;
		
		//
		//Node element
		//
		Element elementElement = document.createElement( TAG.getName() );
		
		//Key
		attr = document.createAttribute( ATTR_NAME );
		attr.setValue( getName() );
		elementElement.setAttributeNode(attr);	
		
		//Value
		attr = document.createAttribute( ATTR_VALUE );
		attr.setValue( getValue() );
		elementElement.setAttributeNode(attr);	
		
		return elementElement;	
	}

	@Override
	public void add( DriverDataModelInterface node ) {
	}

	@Override
	public Object clone(){
		
		DriverExplorerCapabilityDataModel cloned = (DriverExplorerCapabilityDataModel)super.clone();
	
		return cloned;
		
	}

}
