package hu.akoel.grawit.core.treenodedatamodel.testcase;

import java.util.Vector;

import javax.swing.tree.MutableTreeNode;

import hu.akoel.grawit.core.treenodedatamodel.base.BaseRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.constant.ConstantRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.driver.DriverDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.step.StepRootDataModel;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class TestcaseNodeDataModelAdapter extends TestcaseDataModelAdapter{

	private static final long serialVersionUID = -2139557326147525999L;
	
	public static final String ATTR_DETAILS = "details";
	
	private String name;
	private String details;
	
	public TestcaseNodeDataModelAdapter( String name, String details ){
		super( );
		this.name = name;
		this.details = details;
	}
	
	/**
	 * XML alapjan legyartja a TESTCASENODE-ot es az alatta elofordulo 
	 * TESTCASENODE-okat, illetve TESTCASECASE-eket
	 * 
	 * @param element
	 * @throws XMLMissingAttributePharseException 
	 */
	public TestcaseNodeDataModelAdapter( Element element, ConstantRootDataModel constantRootDataModel, BaseRootDataModel baseRootDataModel, StepRootDataModel paramRootDataModel, DriverDataModelAdapter driverRootDataModel ) throws XMLPharseException{
		
		//========
		//
		// Name
		//
		//========	
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( TestcaseNodeDataModelAdapter.getRootTag(), getTag(), ATTR_NAME );			
		}
		String nameString = element.getAttribute( ATTR_NAME );
		this.name = nameString;
		
		//========
		//
		// Details
		//
		//========	
		if( !element.hasAttribute( ATTR_DETAILS ) ){
			throw new XMLMissingAttributePharseException( TestcaseNodeDataModelAdapter.getRootTag(), getTag(), ATTR_NAME, getName(), ATTR_DETAILS );			
		}		
		String detailsString = element.getAttribute( ATTR_DETAILS );		
		this.details = detailsString;
		
	}
	
	@Override
	public void add(TestcaseDataModelAdapter node) {
		super.add( (MutableTreeNode)node );
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
		
		//========
		//
		// Name
		//
		//========	
		Element nodeElement = document.createElement( TestcaseNodeDataModelAdapter.this.getTag().getName() );
		attr = document.createAttribute( ATTR_NAME );
		attr.setValue( getName() );
		nodeElement.setAttributeNode(attr);	
		
		//========
		//
		// Details
		//
		//========	
		attr = document.createAttribute( ATTR_DETAILS );
		attr.setValue( getDetails() );
		nodeElement.setAttributeNode(attr);	
	
		//========
		//
		// Gyermekei
		//
		//========	
		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof TestcaseDataModelAdapter ){
				
				Element element = ((TestcaseDataModelAdapter)object).getXMLElement( document );
				nodeElement.appendChild( element );		    		
		    	
			}
		}
	
		return nodeElement;		
	}

	@Override
	public Object clone(){
		
		//Leklonozza a NODE-ot
		TestcaseNodeDataModelAdapter cloned = (TestcaseNodeDataModelAdapter)super.clone();
	
		//Ha vannak gyerekei 
		if( null != this.children ){
			
			//Akkor azokat is leklonozza
			cloned.children = new Vector<>();
			
			for( Object o : this.children ){
				
				if( o instanceof TestcaseDataModelAdapter ){					
					
					TestcaseDataModelAdapter child = (TestcaseDataModelAdapter) ((TestcaseDataModelAdapter)o).clone();
					
					//Szulo megadasa, mert hogy nem lett hozzaadva direkt modon a Tree-hez
					child.setParent( cloned );					
					
					cloned.children.add(child);
					
				}
			}
		}
		
		//Es a valtozokat is leklonozza
		cloned.name = new String( this.name );
		cloned.details = new String( this.details );
		
		//Le kell masolni a felmenoit is, egyebkent azok automatikusan null-ok
		cloned.setParent( (MutableTreeNode) this.getParent() );
		
		return cloned;
		
	}

}
