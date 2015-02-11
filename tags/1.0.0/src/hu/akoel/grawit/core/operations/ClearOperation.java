package hu.akoel.grawit.core.operations;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import hu.akoel.grawit.core.treenodedatamodel.BaseElementDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.base.NormalBaseElementDataModel;
import hu.akoel.grawit.exceptions.ElementInvalidOperationException;
import hu.akoel.grawit.gui.interfaces.progress.ElementProgressInterface;

public class ClearOperation extends ElementOperationAdapter{
	private static final String NAME = "CLEAR";
	
	public static String getStaticName(){
		return NAME;
	}
	
	@Override
	public String getName() {		
		return getStaticName();
	}
	
	@Override
	public void doOperation(WebDriver driver, BaseElementDataModelAdapter baseElement, WebElement webElement, ElementProgressInterface elementProgress) throws ElementInvalidOperationException {

		if( baseElement instanceof NormalBaseElementDataModel ){

			try{
			
elementProgress.outputCommand( "		webElement.clear();" );				
				//Execute the operation
				webElement.clear();
			
			}catch (WebDriverException webDriverException){
				throw new ElementInvalidOperationException( getName(), baseElement.getName(), ((NormalBaseElementDataModel)baseElement).getSelector(), webDriverException );
			}
		}
		
	}
	
	@Override
	public void setXMLAttribute(Document document, Element element) {
		//No parameter, no operation				
	}
	
	@Override
	public Object clone(){
		
		ClearOperation cloned = new ClearOperation();
		
		return cloned;
		
	}

	@Override
	public String getOperationToString() {		
		return "Clear Field()";
	}
}