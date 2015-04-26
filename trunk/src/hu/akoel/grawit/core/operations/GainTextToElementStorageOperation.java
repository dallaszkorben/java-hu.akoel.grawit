package hu.akoel.grawit.core.operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseElementDataModelAdapter;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.ElementException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.gui.interfaces.progress.ProgressIndicatorInterface;

public class GainTextToElementStorageOperation extends ElementOperationAdapter{
	
	private static final String NAME = "GAINTEXTTOELEMENT";
	private static final String ATTR_PATTERN = "pattern";
	
	private Pattern pattern;
	private Matcher matcher;
	
	//--- Data model
	private String stringPattern;
	//---
	
	public GainTextToElementStorageOperation( String stringPattern ){
		this.stringPattern = stringPattern;
		
		common( stringPattern );
	}
	
	public GainTextToElementStorageOperation( Element element, Tag rootTag, Tag tag ) throws XMLMissingAttributePharseException{
		
		if( !element.hasAttribute( ATTR_PATTERN ) ){
			stringPattern = "";
		}else{
			stringPattern = element.getAttribute( ATTR_PATTERN );
		}
		
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
	public void doOperation(WebDriver driver, BaseElementDataModelAdapter baseElement, WebElement webElement, ProgressIndicatorInterface elementProgress, String tab) throws ElementException {
		
		if( null != elementProgress ){
		//
		// SOURCE Starts
		//	
		elementProgress.printSource( tab + "origText = webElement.getText();" ); 
	
		//Elmenti az elem tartalmat a valtozoba		
		if( null == pattern ){			
			elementProgress.printSource( tab + "String " + baseElement.getNameAsVariable() + " = origText;" );
		}else{
			elementProgress.printSource( tab + "pattern = Pattern.compile( \"" + pattern.pattern().replace("\\", "\\\\") + "\" );" );
			elementProgress.printSource( tab + "matcher = pattern.matcher( origText );");
			elementProgress.printSource( tab + "String " + baseElement.getNameAsVariable() + " = null;" );
			elementProgress.printSource( tab + "if( matcher.find() ){" );	
			elementProgress.printSource( tab + CommonOperations.TAB_BY_SPACE + baseElement.getNameAsVariable() + " = matcher.group();" );
			elementProgress.printSource( tab + "}" );
		}	
		}
		
		//
		// CODE Starts
		//	
		String origText = webElement.getText();
	
		//Elmenti az elem tartalmat a valtozoba		
		if( null == pattern ){
			
			baseElement.setStoredValue( origText );			
			
		}else{
			matcher = pattern.matcher( origText );
			
			if( matcher.find() ){
				
				baseElement.setStoredValue( matcher.group() );
			}			
		}		
	}

	@Override
	public void setXMLAttribute(Document document, Element element) {
		Attr attr = document.createAttribute( ATTR_PATTERN );
		attr.setValue( stringPattern );
		element.setAttributeNode(attr);		
	}

	@Override
	public Object clone() {
		
		String stringPattern = new String( this.stringPattern );
		
		return new GainTextToElementStorageOperation(stringPattern);
	}
	
	@Override
	public String getOperationNameToString() {		
		return "GainTextToElementStorage()";
	}

}

