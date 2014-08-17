package hu.akoel.grawit.gui.tree.datamodel;

import javax.swing.tree.MutableTreeNode;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.pages.ParamPage;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParamPagePageDataModel  extends ParamPageDataModelInterface{

	private static final long serialVersionUID = -5098304990124055586L;
	
	private ParamPage paramPage;
	
	private BasePageRootDataModel basePageRootDataModel;
	
	public ParamPagePageDataModel( ParamPage paramPage, BasePageRootDataModel basePageRootDataModel ){
		super();
		
		this.paramPage = paramPage;
		this.basePageRootDataModel = basePageRootDataModel;
	}

	@Override
	public void add(ParamPageDataModelInterface node) {
		super.add( (MutableTreeNode)node );
	}

	public String getNameToString(){
		return paramPage.getName();
	}
	
	public String getTypeToString(){
		return CommonOperations.getTranslation( "tree.nodetype.parampage");
	}
	
	public ParamPage getParamPage(){
		return paramPage;
	}
	
	@Override
	public Element getXMLElement(Document document) {
		Attr attr;

		//Node element
		Element pageElement = document.createElement("parampage");
		
		//NAME attributum
		attr = document.createAttribute("name");
		attr.setValue( paramPage.getName() );
		pageElement.setAttributeNode(attr);	

		//PAGEBASEPAGE attributum
		BasePagePageDataModel basePagePageDataModel = CommonOperations.getBasePagePageDataModelByBasePage( basePageRootDataModel, paramPage.getBasePage() );	
		attr = document.createAttribute("pagebasepagepath");
		attr.setValue( basePagePageDataModel.getPathToString() );
		pageElement.setAttributeNode(attr);
		

		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof ParamPageDataModelInterface ){
				
				Element element = ((ParamPageDataModelInterface)object).getXMLElement( document );
				pageElement.appendChild( element );		    		
		    	
			}
		}
		
		return pageElement;	
	}
}