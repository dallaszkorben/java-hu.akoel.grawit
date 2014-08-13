package hu.akoel.grawit.gui.tree.datamodel;

import hu.akoel.grawit.CommonOperations;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PageBaseRootDataModel extends PageBaseNodeDataModel{

	private static final long serialVersionUID = 5361088361756620748L;

	public PageBaseRootDataModel(){
		super( "", "" );
	}
	
	public String getNameToString(){
		return "Root";
	}
	
	public String getTypeToString(){
		return CommonOperations.getTranslation( "tree.nodetype.root");
	}
	
	@Override
	public Element getXMLElement(Document document) {
		
		//PageBaseElement
		Element pageBaseElement = document.createElement("pagebase");

		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof PageBaseDataModelInterface ){
				
				Element element = ((PageBaseDataModelInterface)object).getXMLElement( document );
				pageBaseElement.appendChild( element );		    		
		    	
			}
		}
		
/*		//Enumeration<?> e = this.preorderEnumeration();
		Enumeration<?> e = this.breadthFirstEnumeration();
		//Enumeration<?> e = this.depthFirstEnumeration();
		while(e.hasMoreElements()){
			
			Object object = e.nextElement();
			
			if( !object.equals(this) && object instanceof DataModelInterface ){
				
				Element element = ((DataModelInterface)object).getXMLElement( document );
				rootElement.appendChild( element );		    		
		    	
			}
		}
*/			
		return pageBaseElement;		
	}
	
}
