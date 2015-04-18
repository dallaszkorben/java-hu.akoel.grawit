package hu.akoel.grawit.core.treenodedatamodel.variable;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.VariableDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseRootDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.XMLExtraRootTagPharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VariableRootDataModel extends VariableNodeDataModel{

	private static final long serialVersionUID = -4193611923372308352L;

	private static final Tag TAG = Tag.VARIABLEROOT;
	
	public static final String ATTR_NAME = "";
	
	public VariableRootDataModel(){
		super( "", "" );
	}
	
	public VariableRootDataModel( Document doc, BaseRootDataModel baseRootDataModel ) throws XMLPharseException{
		super("","");
		
		NodeList nList = doc.getElementsByTagName( TAG.getName() );
		
		//Ha tobb mint  1 db basepage tag van, akkor az gaz
		if( nList.getLength() > 1 ){
					
			throw new XMLExtraRootTagPharseException( TAG );
					
		}else if( nList.getLength() == 1 ){
		
			Node variablePageNode = nList.item(0);
			if (variablePageNode.getNodeType() == Node.ELEMENT_NODE) {
			
				NodeList nodeList = variablePageNode.getChildNodes();
				for( int i = 0; i < nodeList.getLength(); i++ ){
			
					Node variableNode = nodeList.item( i );
				
					if (variableNode.getNodeType() == Node.ELEMENT_NODE) {
						Element variableElement = (Element)variableNode;
					
						//Ha ujabb PARAMNODE van alatta
						if( variableElement.getTagName().equals( Tag.VARIABLENODE.getName() ) ){						
							this.add(new VariableNodeDataModel( variableElement, baseRootDataModel ));
					
						//Ha rogton a rootban van elhelyezve egy elem
						}else if( variableElement.getTagName().equals( Tag.VARIABLEELEMENT.getName() ) ){
							this.add(new VariableElementDataModel(variableElement, baseRootDataModel ));
						}
					}
				}
			}
		}		
	}
	
	@Override
	public String getName(){
		//return "/";
		return "Variable Root";
	}

	@Override
	public Tag getTag(){
		return TAG;
	}

	@Override
	public String getNodeTypeToShow(){
		return CommonOperations.getTranslation( "tree.nodetype.variable.root");
	}
	
	@Override
	public Element getXMLElement(Document document) {
		
		//ParamPageElement
		Element paramPageElement = document.createElement( TAG.getName() );

		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof VariableDataModelAdapter ){
				
				Element element = ((VariableDataModelAdapter)object).getXMLElement( document );
				paramPageElement.appendChild( element );		    		
		    	
			}
		}
		
		return paramPageElement;		
	}
	
}