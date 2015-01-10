package hu.akoel.grawit.core.treenodedatamodel.param;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.ParamDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.variable.VariableRootDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParamFolderDataModel extends ParamNodeDataModelAdapter{

	private static final long serialVersionUID = -2466202302741284519L;
	
	public static final Tag TAG = Tag.PARAMFOLDER;
	
	public ParamFolderDataModel( String name, String details ){
		super( name, details );
	}
	
	public ParamFolderDataModel( Element element, BaseRootDataModel baseRootDataModel, VariableRootDataModel variableRootDataModel ) throws XMLPharseException{

		super( element, baseRootDataModel, variableRootDataModel );
		
/*		//========
		//
		// Name
		//
		//========
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( ParamFolderDataModel.getRootTag(), Tag.PARAMFOLDER, ATTR_NAME );			
		}
		String nameString = element.getAttribute( ATTR_NAME );
		this.name = nameString;

		//========
		//
		// Details
		//
		//========
		String detailsString;
		if( !element.hasAttribute( ATTR_DETAILS ) ){
		//	throw new XMLMissingAttributePharseException( ParamNodeDataModel.getRootTag(), Tag.PARAMNODE, ATTR_NAME, getName(), ATTR_DETAILS );
			detailsString = "";
		}else{		
			detailsString = element.getAttribute( ATTR_DETAILS );
		}
		this.details = detailsString;
*/
		
	    //========
		//
		// Gyermekei
		//
	    //========
		NodeList nodelist = element.getChildNodes();
		for( int i = 0; i < nodelist.getLength(); i++ ){
			Node node = nodelist.item( i );
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element pageElement = (Element)node;
				
				//Ha PARAMPAGE van alatta
				if( pageElement.getTagName().equals( Tag.PARAMNORMALELEMENTCOLLECTOR.getName() )){					
					this.add(new ParamNormalCollectorDataModel(pageElement, baseRootDataModel, variableRootDataModel ) );

				//Ha PARAMLOOP van alatta
				}else if( pageElement.getTagName().equals( Tag.PARAMLOOPELEMENTCOLLECTOR.getName() )){					
					this.add(new ParamLoopCollectorDataModel(pageElement, variableRootDataModel, baseRootDataModel ) );
						
				//Ha ujabb PARAMNODE van alatta
				}else if( pageElement.getTagName().equals( Tag.PARAMFOLDER.getName() )){					
					this.add(new ParamFolderDataModel(pageElement, baseRootDataModel, variableRootDataModel ) );
				}
			}
		}
		
	}

	public static Tag getTagStatic(){
		return TAG;
	}
	
	@Override
	public Tag getTag(){
		return getTagStatic();
	}
	
	@Override
	public void add(ParamDataModelAdapter node) {
		super.add( (MutableTreeNode)node );
	}
	
	public static String  getModelNameToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.param.folder");
	}
	
	@Override
	public String getNodeTypeToShow(){
		return getModelNameToShowStatic();
	}

	@Override
	public Element getXMLElement(Document document) {
		Attr attr;
		
		Element nodeElement = super.getXMLElement(document); 
		
/*		//Node element
		Element nodeElement = document.createElement("node");
		attr = document.createAttribute("name");
		attr.setValue( getName() );
		nodeElement.setAttributeNode(attr);	
		
		attr = document.createAttribute("details");
		attr.setValue( getDetails() );
		nodeElement.setAttributeNode(attr);	
	
		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof ParamDataModelAdapter ){
				
				Element element = ((ParamDataModelAdapter)object).getXMLElement( document );
				nodeElement.appendChild( element );		    		
		    	
			}
		}
*/			
		return nodeElement;		
	}

/*	
	@Override
	public Object clone(){
		
		//Leklonozza a NODE-ot
		ParamFolderDataModel cloned = (ParamFolderDataModel)super.clone();
	
		//Ha vannak gyerekei (NODE vagy PAGE)
		if( null != this.children ){
					
			//Akkor azokat is leklonozza
			cloned.children = new Vector<>();
					
			for( Object o : this.children ){
						
				if( o instanceof ParamDataModelAdapter ){
					
					ParamDataModelAdapter child = (ParamDataModelAdapter) ((ParamDataModelAdapter)o).clone();
					
					//Szulo megadasa, mert hogy nem lett hozzaadva direkt modon a Tree-hez
					child.setParent( cloned );					
					
					cloned.children.add(child);
					
				}
			}
		}
				
		//Es a valtozokat is leklonozza
		cloned.name = new String( this.name );
		cloned.details = new String( this.details );		
		
		return cloned;
		
	}
	
	@Override
	public Object cloneWithParent() {
		
		ParamFolderDataModel cloned = (ParamFolderDataModel) this.clone();
		
		//Le kell masolni a felmenoit is, egyebkent azok automatikusan null-ok
		cloned.setParent( (MutableTreeNode) this.getParent() );
		
		return cloned;
	}
*/
	
}
