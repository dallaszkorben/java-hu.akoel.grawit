package hu.akoel.grawit.core.treenodedatamodel.collector;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.CollectorDataModelAdapter;
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

public class CollectorNodeDataModel extends CollectorDataModelAdapter{

	private static final long serialVersionUID = -2466202302741284519L;
	
	public static final Tag TAG = Tag.COLLECTORNODE;
	
	private static final String ATTR_DETAILS = "details";
		
	private String name;
	private String details;
	
	public CollectorNodeDataModel( String name, String details ){
		super( );
		this.name = name;
		this.details = details;
	}
	
	public CollectorNodeDataModel( Element element, BaseRootDataModel baseRootDataModel, VariableRootDataModel variableRootDataModel ) throws XMLPharseException{
		
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( CollectorNodeDataModel.getRootTag(), Tag.COLLECTORNODE, ATTR_NAME );			
		}
		String nameString = element.getAttribute( ATTR_NAME );
		this.name = nameString;
		
		if( !element.hasAttribute( ATTR_DETAILS ) ){
			throw new XMLMissingAttributePharseException( CollectorNodeDataModel.getRootTag(), Tag.COLLECTORNODE, ATTR_NAME, getName(), ATTR_DETAILS );			
		}		
		String detailsString = element.getAttribute( ATTR_DETAILS );		
		this.details = detailsString;
		
		NodeList nodelist = element.getChildNodes();
		for( int i = 0; i < nodelist.getLength(); i++ ){
			Node node = nodelist.item( i );
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element pageElement = (Element)node;
				
				//Ha PARAMPAGE van alatta
				if( pageElement.getTagName().equals( Tag.COLLECTORNORMAL.getName() )){					
					this.add(new CollectorNormalDataModel(pageElement, baseRootDataModel, variableRootDataModel ) );

				//Ha PARAMLOOP van alatta
				}else if( pageElement.getTagName().equals( Tag.COLLECTORLOOP.getName() )){					
					this.add(new CollectorLoopDataModel(pageElement, variableRootDataModel, baseRootDataModel ) );
						
				//Ha ujabb BASENODE van alatta
				}else if( pageElement.getTagName().equals( Tag.COLLECTORNODE.getName() )){					
					this.add(new CollectorNodeDataModel(pageElement, baseRootDataModel, variableRootDataModel ) );
				}
			}
		}
	}

/*	public static Tag getTagStatic(){
		return TAG;
	}
*/	
	@Override
	public Tag getTag(){
		return TAG;
		//return getTagStatic();
	}
	
	@Override
	public void add(CollectorDataModelAdapter node) {
		super.add( (MutableTreeNode)node );
	}
	
	public static String  getModelNameToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.param.node");
	}
	
	@Override
	public String getNodeTypeToShow(){
		return getModelNameToShowStatic();
	}
	
	@Override
	public String getName(){
		return name;
	}
	
	public String getDetails(){
		return details;
	}
	
	public void setDetails( String details ){
		this.details = details;
	}
	
	public void setName( String name ){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}

	@Override
	public Element getXMLElement(Document document) {
		Attr attr;
		
		//Node element
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
			
			if( !object.equals(this) && object instanceof CollectorDataModelAdapter ){
				
				Element element = ((CollectorDataModelAdapter)object).getXMLElement( document );
				nodeElement.appendChild( element );		    		
		    	
			}
		}
			
		return nodeElement;		
	}

	@Override
	public Object clone(){
		
		//Leklonozza a NODE-ot
		CollectorNodeDataModel cloned = (CollectorNodeDataModel)super.clone();
	
		//Ha vannak gyerekei (NODE vagy PAGE)
		if( null != this.children ){
					
			//Akkor azokat is leklonozza
			cloned.children = new Vector<>();
					
			for( Object o : this.children ){
						
				if( o instanceof CollectorDataModelAdapter ){
					
					CollectorDataModelAdapter child = (CollectorDataModelAdapter) ((CollectorDataModelAdapter)o).clone();
					
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
		
		CollectorNodeDataModel cloned = (CollectorNodeDataModel) this.clone();
		
		//Le kell masolni a felmenoit is, egyebkent azok automatikusan null-ok
		cloned.setParent( (MutableTreeNode) this.getParent() );
		
		return cloned;
	}
	
}