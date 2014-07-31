package hu.akoel.grawet.operation;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import hu.akoel.grawet.element.ParameterizedElement;
import hu.akoel.grawet.element.PureElement;

public class LinkOperation implements ElementOperation{

	/**
	 * 
	 * Executes a Click action on the WebElement (Link)
	 * 
	 */
	@Override
	public void doAction( ParameterizedElement element ) {
		PureElement pureElement = element.getElement();
		
		//Searching for the element - waiting for it
		WebDriverWait wait = new WebDriverWait(pureElement.getDriver(), 10);		
		wait.until(ExpectedConditions.elementToBeClickable( pureElement.getBy() ) );		

		WebElement webElement = pureElement.getDriver().findElement(pureElement.getBy());
	
		//Execute the operation
		//element.getDriver().findElement(element.getBy()).click();
		
		//Sajnos csak a javascipt hivassal mukodik. a webElement.click() hatasara nem tortenik semmi
		//Feltehetoleg idozitesi problema, mert debug-kor mukodik
		JavascriptExecutor executor = (JavascriptExecutor)pureElement.getDriver();
		executor.executeScript("arguments[0].click();", webElement);
		
	}
}