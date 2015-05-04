package hu.akoel.grawit.core.operations;

import java.util.Set;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseElementDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.base.NormalBaseElementDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.enums.list.CompareTypeListEnum;
import hu.akoel.grawit.exceptions.ElementCompareOperationException;
import hu.akoel.grawit.exceptions.ElementException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.gui.interfaces.progress.ProgressIndicatorInterface;

public class CompareListLengthToStringOperation extends ElementOperationAdapter implements CompareOperation{
	
	private static final String NAME = "COMPARELISTLENGTHTOSTRING";
	private static final String ATTR_STRING = "string";
	private static final String ATTR_COMPARE_TYPE = "type";
	
	private boolean isInLoop = false;
	
	// Model
	private String stringToCompare;
	private CompareTypeListEnum compareType;
	//----
	
	public CompareListLengthToStringOperation( String stringToCompare, CompareTypeListEnum compareType ){
		this.stringToCompare = stringToCompare;
		this.compareType = compareType;
	}
	
	public CompareListLengthToStringOperation( Element element, Tag rootTag, Tag tag ) throws XMLMissingAttributePharseException{
		
		//ATTR_COMPARE_TYPE
		if( !element.hasAttribute( ATTR_COMPARE_TYPE ) ){
			throw new XMLMissingAttributePharseException( rootTag, tag, ATTR_COMPARE_TYPE );		
		}	
		String typeString = element.getAttribute(ATTR_COMPARE_TYPE);
		this.compareType = CompareTypeListEnum.valueOf( typeString );
		
		//ATTR_STRING
		if( !element.hasAttribute( ATTR_STRING ) ){
			throw new XMLMissingAttributePharseException( rootTag, tag, ATTR_STRING );			
		}
		stringToCompare = element.getAttribute( ATTR_STRING );		
	}
	
	public String getStringToShow() {
		return stringToCompare;
	}

	public static String getStaticName(){
		return NAME;
	}
	
	@Override
	public String getName() {		
		return getStaticName();
	}
		
	public CompareTypeListEnum getCompareType(){
		return compareType;
	}

	@Override
	public boolean isInLoop(){
		return this.isInLoop;
	}
	
	@Override
	public void setIsInLoop( boolean isInLoop ){
		this.isInLoop = isInLoop;
	}

	@Override
	public void doOperation( WebDriver driver, BaseElementDataModelAdapter baseElement, WebElement webElement, ProgressIndicatorInterface elementProgress, String tab, Set<String> definedElementSet, boolean needToPrintSource ) throws ElementException {

		//
		// SOURCE Starts
		//		
		if( needToPrintSource ){
			elementProgress.printSource( tab + "origText = \"0\";" );
			elementProgress.printSource( tab + "select = new Select(webElement);" );
		
			//VALUE			
			elementProgress.printSource( tab + "String.valueOf( origText = select.getOption().size() );" );
		
			if( compareType.equals( CompareTypeListEnum.EQUAL ) ){			
				elementProgress.printSource( tab + "if( !origText.equals( \"" + stringToCompare + "\" ) ){" );
				if( isInLoop() ){
					elementProgress.printSource( tab + CommonOperations.TAB_BY_SPACE + "break; //because the selected element in the Select '" + baseElement.getNameAsVariable() + "' does NOT equal to '" + stringToCompare + " + \"'.");
				}else{
					elementProgress.printSource( tab + CommonOperations.TAB_BY_SPACE + "fail(\"Stopped because the selected element in the Select '" + baseElement.getNameAsVariable() + "': '\" + origText + \"' does NOT equal to '" + stringToCompare + "' but it should.\");");
				}
				elementProgress.printSource( tab + "}" );
			
			}else if( compareType.equals( CompareTypeListEnum.DIFFERENT ) ){
				elementProgress.printSource( tab + "if( origText.equals( \"" + stringToCompare + "\" ) ){" );
				if( isInLoop() ){
					elementProgress.printSource( tab + CommonOperations.TAB_BY_SPACE + "break; //because the selected element in the Select '" + baseElement.getNameAsVariable() + "' equals to '" + stringToCompare + " + \"'.");
				}else{
					elementProgress.printSource( tab + CommonOperations.TAB_BY_SPACE + "fail(\"Stopped because the selected element in the Select '" + baseElement.getNameAsVariable() + "': '\" + origText + \"' equals to '" + stringToCompare + "' but it should NOT.\");");
				}
				elementProgress.printSource( tab + "}" );
			}		
		}
		
		//
		// CODE Starts
		//		
		String origText = "0";
		Select select = new Select(webElement);
		
		//VALUE
		origText = String.valueOf( select.getOptions().size() );

		//Ha az egyenloseg az elvart
		if( compareType.equals( CompareTypeListEnum.EQUAL ) ){
			
			if( !origText.equals( stringToCompare ) ){

				if( baseElement instanceof NormalBaseElementDataModel ){
					throw new ElementCompareOperationException(compareType, stringToCompare, baseElement.getName(), ((NormalBaseElementDataModel)baseElement).getSelector(), origText, new Exception() );
				//Special
				}else{
					throw new ElementCompareOperationException(compareType, stringToCompare, baseElement.getName(), "special", origText, new Exception() );
				}
			}
			
		//Ha a kulonbozoseg az elvart
		}else if( compareType.equals( CompareTypeListEnum.DIFFERENT ) ){
			
			if( origText.equals( stringToCompare ) ){
				
				if( baseElement instanceof NormalBaseElementDataModel ){
					throw new ElementCompareOperationException(compareType, stringToCompare, baseElement.getName(), ((NormalBaseElementDataModel)baseElement).getSelector(), origText, new Exception() );
				//Special
				}else{
					throw new ElementCompareOperationException(compareType, stringToCompare, baseElement.getName(), "special", origText, new Exception() );
				}
			}			
		}
	}
	
	@Override
	public void setXMLAttribute(Document document, Element element) {
		
		Attr attr = document.createAttribute( ATTR_STRING );
		attr.setValue( stringToCompare );
		element.setAttributeNode(attr);	
		
		attr = document.createAttribute( ATTR_COMPARE_TYPE );
		attr.setValue( compareType.name() );
		element.setAttributeNode( attr );	
	}

	@Override
	public Object clone() {
		String stringToCompare = new String( this.stringToCompare );

		//CompareTypeListEnum compareType = this.compareType;			
		//ListCompareByListEnum compareBy = this.compareBy;	
		
		return new CompareListLengthToStringOperation(stringToCompare, compareType );
	}
	
	@Override
	public String getOperationNameToString() {		
		return "CompareListLengthToString()";
	}
}
