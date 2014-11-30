package hu.akoel.grawit.core.treenodedatamodel.testcase;

import org.openqa.selenium.WebDriver;

import hu.akoel.grawit.core.treenodedatamodel.TestcaseDataModelAdapter;
import hu.akoel.grawit.exceptions.CompilationException;
import hu.akoel.grawit.exceptions.PageException;
import hu.akoel.grawit.gui.interfaces.progress.ElementProgressInterface;
import hu.akoel.grawit.gui.interfaces.progress.PageProgressInterface;

public abstract class TestcasePageModelInterface extends TestcaseDataModelAdapter{

	private static final long serialVersionUID = -3451125455593871748L;
	
	public abstract void doAction( WebDriver driver, PageProgressInterface pageProgress, ElementProgressInterface elementProgress ) throws PageException, CompilationException;

}
