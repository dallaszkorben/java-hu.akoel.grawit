package hu.akoel.grawit.core.treenodedatamodel.param;

import java.io.StringReader;
import java.util.Vector;

import javax.swing.tree.MutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.ElementProgressInterface;
import hu.akoel.grawit.ExecutablePageInterface;
import hu.akoel.grawit.PageProgressInterface;
import hu.akoel.grawit.Settings;
import hu.akoel.grawit.core.treenodedatamodel.BaseDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.BaseElementDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.ParamDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseNodeDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.BasePageDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.NormalBaseElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.SpecialBaseElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.variable.VariableRootDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.exceptions.ElementException;
import hu.akoel.grawit.exceptions.PageException;
import hu.akoel.grawit.exceptions.XMLBaseConversionPharseException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLPharseException;

public class ParamPageDataModel  extends ParamDataModelAdapter implements ExecutablePageInterface{//, BasePageChangeListener{
	
	private static final long serialVersionUID = -5098304990124055586L;
	
	public static final Tag TAG = Tag.PARAMPAGE;
	
	private static final String ATTR_BASE_PAGE_PATH = "basepagepath";
	
	private String name;
	private BasePageDataModel basePage;	
	
	public ParamPageDataModel( String name, BasePageDataModel basePage ){
		super();
		
		this.name = name;
		this.basePage = basePage;

	}

	/**
	 * XML alapjan gyartja le az objektumot
	 * 
	 * @param element
	 * @throws XMLPharseException
	 */
	public ParamPageDataModel( Element element, BaseDataModelAdapter baseDataModel, VariableRootDataModel variableRootDataModel ) throws XMLPharseException{
		
		//========
		//
		//name
		//
		//========
		if( !element.hasAttribute( ATTR_NAME ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME );			
		}
		String nameString = element.getAttribute( ATTR_NAME );		
		this.name = nameString;
				
		//========
		//
		//BasePage
		//
		//========
		if( !element.hasAttribute( ATTR_BASE_PAGE_PATH ) ){
			throw new XMLMissingAttributePharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH );			
		}
		String paramPagePathString = element.getAttribute(ATTR_BASE_PAGE_PATH);				
		paramPagePathString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + paramPagePathString;  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder; 
	    Document document = null;
	    try {  
	    	//attributum-kent tarolt utvonal atalakitasa Documentum-ma
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse( new InputSource( new StringReader( paramPagePathString ) ) ); 
	        
	    } catch (Exception e) { 
	    	
	    	//Nem sikerult az atalakitas
	    	throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH), e );
	    	
	    } 
		//Megkeresem a BASEROOT-ben a BASEPAGE-hez vezeto utat
	    Node actualNode = document;
	    while( actualNode.hasChildNodes() ){
		
	    	actualNode = actualNode.getFirstChild();
	    	Element actualElement = (Element)actualNode;
	    	String tagName = actualElement.getTagName();
	    	String attrName = null;
	    	
	    	//Ha BASENODE
	    	if( tagName.equals( BaseNodeDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(BaseNodeDataModel.ATTR_NAME);	    		
	    		baseDataModel = (BaseDataModelAdapter) CommonOperations.getDataModelByNameInLevel( baseDataModel, Tag.BASENODE, attrName );

	    		if( null == baseDataModel ){

	    			throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH) );
	    		}
	    		
	    	//Ha BASEPAGE
	    	}else if( tagName.equals( BasePageDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(BasePageDataModel.ATTR_NAME);
	    		baseDataModel = (BaseDataModelAdapter) CommonOperations.getDataModelByNameInLevel( baseDataModel, Tag.BASEPAGE, attrName );
	    		if( null == baseDataModel ){

	    			throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH) );
	    		}
	    		
	    	//Ha NORMALBASEELEMENT
	    	}else if( tagName.equals( NormalBaseElementDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(NormalBaseElementDataModel.ATTR_NAME);

	    		throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH) );	    		
	    	
	    	//Ha SPECIALBASEELEMENT
	    	}else if( tagName.equals( SpecialBaseElementDataModel.TAG.getName() ) ){
	    		attrName = actualElement.getAttribute(SpecialBaseElementDataModel.ATTR_NAME);

	    		throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH) );	    		
	    	
	    	
	    	}else{
	    		
	    		throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH) );	    		
	    	}	    	
	    	
	    }	    
	    try{
	    	
	    	basePage = (BasePageDataModel)baseDataModel;
	    	
	    }catch(ClassCastException e){

	    	//Nem sikerult az utvonalat megtalalni
	    	throw new XMLBaseConversionPharseException( getRootTag(), TAG, ATTR_NAME, getName(), ATTR_BASE_PAGE_PATH, element.getAttribute(ATTR_BASE_PAGE_PATH), e );
	    }
		
		//Vegig a PARAMELEMENT-ekent
		NodeList nodelist = element.getChildNodes();
		for( int i = 0; i < nodelist.getLength(); i++ ){
			Node node = nodelist.item( i );
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element paramElement = (Element)node;
				if( paramElement.getTagName().equals( Tag.PARAMELEMENT.getName() )){					
					this.add(new ParamElementDataModel(paramElement, basePage, variableRootDataModel ));
				}
			}
		}	
	}

	public static Tag getTagStatic(){
		return TAG;
	}

	@Override
	public Tag getTag() {
		return getTagStatic();
	}
	
	public BasePageDataModel getBasePage(){
		return basePage;
	}
	
	@Override
	public void add(ParamDataModelAdapter node) {
		super.add( (MutableTreeNode)node );
	}
	
	public static String  getNodeTypeToShowStatic(){
		return CommonOperations.getTranslation( "tree.nodetype.param.page");
	}
	
	@Override
	public String getNodeTypeToShow(){
		return getNodeTypeToShowStatic();
	}

	@Override
	public String getName() {		
		return name;
	}
	
	public void setName( String name ){
		this.name = name;
	}

	@Override
	public void doAction( WebDriver driver, PageProgressInterface pageProgress, ElementProgressInterface elementProgress ) throws PageException {
		
		ParamElementDataModel parameterElement;
		
		//Jelzi, hogy elindult az oldal feldolgozasa
		if( null != pageProgress ){
			pageProgress.pageStarted( getName(), getNodeTypeToShow() );
		}	

		int childrenCount = this.getChildCount();
		for( int i = 0; i < childrenCount; i++ ){

			//TODO BaseElement Waiting time ... atadhato lenne parameterkent a doAction szamara
			
			//Parameterezett elem
			parameterElement = (ParamElementDataModel)this.getChildAt( i );
			
			//Ha a parameterezett elem be van kapcsolva
			if( parameterElement.isOn() ){
			
				//Bazis elem
				BaseElementDataModelAdapter baseElement = parameterElement.getBaseElement();
			
				if( baseElement instanceof NormalBaseElementDataModel ){
				
					//TODO lehet, hogy ennek a framere varakozo idonek kulonboznie kellene
					//a Bazis elemhez tartozo warakozasi ido
					Integer waitingTime = ((NormalBaseElementDataModel)baseElement).getWaitingTime();
					if( null == waitingTime ){
						waitingTime = Settings.getInstance().getWaitingTime();
					}
					WebDriverWait wait = new WebDriverWait(driver, waitingTime);
			
					// Ha az alapertelmezettol kulonbozo frame van meghatarozva, akkor valt			
					String frameName = ((NormalBaseElementDataModel)parameterElement.getBaseElement()).getFrame();

					if( null != frameName && frameName.trim().length() > 0 ){
				
						driver.switchTo().defaultContent();
						wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
						driver.switchTo().defaultContent();

						driver.switchTo().frame( frameName );		
					}				
				}
				
				try{			
					parameterElement.doAction( driver, elementProgress );
			
					//Ha nem futott le rendesen a teszteset
				}catch (ElementException e){
					throw new PageException( this.getName(), e.getElementName(), e.getElementSelector(), e);
			
				}
			
			}
				
		}
		
		//Jelzi, hogy befejezodott az oldal feldolgozasa
		if( null != pageProgress ){
			pageProgress.pageEnded( getName(), getNodeTypeToShow() );
		}
		
	}
	
	@Override
	public Element getXMLElement(Document document) {
		Attr attr;

		//========
		//
		//Node element
		//
		//========
		Element pageElement = document.createElement(TAG.getName());
		
		//========
		//
		//NAME attributum
		//
		//========
		attr = document.createAttribute(ATTR_NAME);
		attr.setValue( getName() );
		pageElement.setAttributeNode(attr);	

		//========
		//
		//PAGEBASEPAGE attributum
		//
		//========
		attr = document.createAttribute( ATTR_BASE_PAGE_PATH );
		attr.setValue( basePage.getPathTag() );
		pageElement.setAttributeNode(attr);		

		int childrens = this.getChildCount();
		for( int i = 0; i < childrens; i++ ){
			
			Object object = this.getChildAt( i );
			
			if( !object.equals(this) && object instanceof ParamDataModelAdapter ){
				
				Element element = ((ParamDataModelAdapter)object).getXMLElement( document );
				pageElement.appendChild( element );		    		
		    	
			}
		}
		
		return pageElement;	
	}
	
	@Override
	public Object clone(){
		
		//Leklonozza a ParamPage-et
		ParamPageDataModel cloned = (ParamPageDataModel)super.clone();

		//Ha vannak gyerekei (ELEMENT)
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
		
		return cloned;
		
	}
	
	@Override
	public Object cloneWithParent() {
		
		ParamPageDataModel cloned = (ParamPageDataModel) this.clone();
		
		//Le kell masolni a felmenoit is, egyebkent azok automatikusan null-ok
		cloned.setParent( (MutableTreeNode) this.getParent() );
		
		return cloned;
	}
	
}