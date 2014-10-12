package hu.akoel.grawit.core.operations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.ElementProgressInterface;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamElementDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.enums.list.ElementTypeListEnum;
import hu.akoel.grawit.exceptions.ElementException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;

public class GainValueToElementOperation extends ElementOperationAdapter{
	
	private static final String NAME = "GAINVALUETOELEMENT";
	private static final String ATTR_PATTERN = "pattern";
	
	private Pattern pattern;
	private Matcher matcher;
	
	//--- Data model
	private String stringPattern;
	//---
	
	public GainValueToElementOperation( String stringPattern ){
		this.stringPattern = stringPattern;
		
		common( stringPattern );
	}
	
	public GainValueToElementOperation( Element element, Tag rootTag, Tag tag ) throws XMLMissingAttributePharseException{
		
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
		
		//GAIN VALUE
/*		//Ha LIST
		if( element.getBaseElement().getElementType().equals(ElementTypeListEnum.LIST)){

			Select select = new Select(webElement);
			origText = select.getFirstSelectedOption().getAttribute("value");
*/
		//CHECKBOX/RADIOBUTTON
		if( element.getBaseElement().getElementType().equals(ElementTypeListEnum.CHECKBOX) || element.getBaseElement().getElementType().equals(ElementTypeListEnum.RADIOBUTTON) ){

			if( webElement.isSelected() ){
				origText = "on";
			}else{
				origText = "off";
			}
			
		//Ha FIELD
		}else{		

			origText = webElement.getAttribute("value");
		}
	
		//EXECUTE OPERATION = Elmenti az elem tartalmat a valtozoba		
		if( null == pattern ){
			element.getBaseElement().setGainedValue( origText );
		}else{
			matcher = pattern.matcher( origText );
			if( matcher.find() ){
				String resultText = matcher.group();
				element.getBaseElement().setGainedValue( resultText );
			}			
		}	
		
	}
	
	@Override
	public void setXMLAttribute(Document document, Element element) {
		Attr attr = document.createAttribute( ATTR_PATTERN );
		attr.setValue( stringPattern );
		element.setAttributeNode(attr);		
	}


	
}

