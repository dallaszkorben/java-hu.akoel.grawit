package hu.akoel.grawit.core.operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.ElementProgressInterface;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamElementDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.enums.list.ListGainByListEnum;
import hu.akoel.grawit.enums.list.ListSelectionByListEnum;
import hu.akoel.grawit.exceptions.ElementException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;

public class GainListToElementStorageOperation extends ElementOperationAdapter{
	
	private static final String NAME = "GAINTOELEMENT";
	private static final String ATTR_PATTERN = "pattern";
	private static final String ATTR_GAIN_BY = "gainby";
	
	private Pattern pattern;
	
	//--- Data model
	private String stringPattern;
	private ListGainByListEnum gainBy;
	//---
	
	public GainListToElementStorageOperation( String stringPattern, ListGainByListEnum gainBy ){
		this.stringPattern = stringPattern;
		this.gainBy = gainBy;
		
		common( stringPattern );
	}
	
	public GainListToElementStorageOperation( Element element, Tag rootTag, Tag tag ) throws XMLMissingAttributePharseException{
		
		//GAIN BY
		String stringGainBy = "";
		if( !element.hasAttribute( ATTR_GAIN_BY ) ){
			stringGainBy = ListGainByListEnum.BYVALUE.name();
			//throw new XMLMissingAttributePharseException( rootTag, tag, ATTR_GAIN_BY );			
		}
		stringGainBy = element.getAttribute( ATTR_GAIN_BY );		
		if( stringGainBy.equals( ListSelectionByListEnum.BYVALUE.name() ) ){
			gainBy = ListGainByListEnum.BYVALUE;
		}else if( stringGainBy.equals( ListSelectionByListEnum.BYVISIBLETEXT.name() ) ){
			gainBy = ListGainByListEnum.BYVISIBLETEXT;
		}else{
			gainBy = ListGainByListEnum.BYVISIBLETEXT;
		}			
		
		if( !element.hasAttribute( ATTR_PATTERN ) ){
			throw new XMLMissingAttributePharseException( rootTag, tag, ATTR_PATTERN );			
		}
		stringPattern = element.getAttribute( ATTR_PATTERN );	
		
		common( stringPattern );
		
	}
	
	private void common( String stringPattern ){
		
		if( stringPattern.trim().length() == 0 ){
			pattern = null;
		}else{		
			pattern = Pattern.compile( stringPattern );
		}
		
	}
	
	public static String getStaticName(){
		return NAME;
	}
	
	@Override
	public String getName() {
		return getStaticName();
	}
		
	public String getStringPattern(){
		return stringPattern;
	}

	@Override
	public void doOperation(WebDriver driver, ParamElementDataModel element, WebElement webElement, ElementProgressInterface elementProgress) throws ElementException {

		String origText = "";
		
		Select select = new Select(webElement);
		
		//VALUE
		if( gainBy.equals( ListGainByListEnum.BYVALUE ) ){
			
			origText = select.getFirstSelectedOption().getAttribute("value");
			
		//TEXT
		}else if( gainBy.equals( ListGainByListEnum.BYVISIBLETEXT ) ){
		
			origText = select.getFirstSelectedOption().getText();	
		}	
		
		//EXECUTE_SCRIPT OPERATION = Elmenti az elem tartalmat a valtozoba		
		if( null == pattern ){
			element.getBaseElement().setStoredValue( origText );
		}else{
			Matcher matcher = pattern.matcher( origText );
			if( matcher.find() ){
				String resultText = matcher.group();
				element.getBaseElement().setStoredValue( resultText );
			}			
		}	
		
	}
	
	@Override
	public void setXMLAttribute(Document document, Element element) {
		Attr attr = document.createAttribute( ATTR_PATTERN );
		attr.setValue( stringPattern );
		element.setAttributeNode(attr);	
		
		attr = document.createAttribute( ATTR_GAIN_BY );
		attr.setValue( gainBy.name() );
		element.setAttributeNode( attr );	
	}

	@Override
	public Object clone() {
		
		String stringPattern = new String( this.stringPattern );
		ListGainByListEnum gainBy = this.gainBy;

		return new GainListToElementStorageOperation(stringPattern, gainBy);
	}


	
}
