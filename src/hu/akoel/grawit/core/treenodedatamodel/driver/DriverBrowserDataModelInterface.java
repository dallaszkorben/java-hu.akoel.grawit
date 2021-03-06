package hu.akoel.grawit.core.treenodedatamodel.driver;

import org.openqa.selenium.WebDriver;

import hu.akoel.grawit.gui.interfaces.progress.ProgressIndicatorInterface;

public abstract class DriverBrowserDataModelInterface<E> extends DriverDataModelAdapter{

	private static final long serialVersionUID = 7926898001139103501L;

	public abstract WebDriver getDriver( ProgressIndicatorInterface elementProgres, String tab );
	
	public abstract void add( E node );
	
	public void add( DriverDataModelAdapter node ){
		add( (E)node );
	}
	
}
