package hu.akoel.grawet.page;

import hu.akoel.grawet.exceptions.PageException;

public interface ExecutablePageInterface {

	public String getName();
	
	public void doAction() throws PageException;
	
	public void setPageProgressInterface( PageProgressInterface pageProgressInterface );
	
}