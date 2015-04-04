package hu.akoel.grawit;

import hu.akoel.grawit.core.operations.ClearOperation;
import hu.akoel.grawit.core.operations.ClickLeftOperation;
import hu.akoel.grawit.core.operations.ClickRightOperation;
import hu.akoel.grawit.core.operations.CompareListToStoredElementOperation;
import hu.akoel.grawit.core.operations.CompareListToStringOperation;
import hu.akoel.grawit.core.operations.CompareListToVariableOperation;
import hu.akoel.grawit.core.operations.CompareTextToStoredElementOperation;
import hu.akoel.grawit.core.operations.CompareTextToStringOperation;
import hu.akoel.grawit.core.operations.CompareTextToVariableOperation;
import hu.akoel.grawit.core.operations.CompareValueToStoredElementOperation;
import hu.akoel.grawit.core.operations.CompareValueToStringOperation;
import hu.akoel.grawit.core.operations.CompareValueToVariableOperation;
import hu.akoel.grawit.core.operations.ElementOperationAdapter;
import hu.akoel.grawit.core.operations.FillWithBaseElementOperation;
import hu.akoel.grawit.core.operations.FillWithStringOperation;
import hu.akoel.grawit.core.operations.FillWithVariableElementOperation;
import hu.akoel.grawit.core.operations.GainListToElementStorageOperation;
import hu.akoel.grawit.core.operations.GainListToVariableOperation;
import hu.akoel.grawit.core.operations.GainTextToElementOperation;
import hu.akoel.grawit.core.operations.GainValueToElementStorageOperation;
import hu.akoel.grawit.core.operations.GainValueToVariableOperation;
import hu.akoel.grawit.core.operations.OutputStoredElementOperation;
import hu.akoel.grawit.core.operations.SelectBaseElementOperation;
import hu.akoel.grawit.core.operations.SelectStringOperation;
import hu.akoel.grawit.core.operations.SelectVariableElementOperation;
import hu.akoel.grawit.core.operations.SpecialBaseAddStoreToParametersOperation;
import hu.akoel.grawit.core.operations.SpecialBaseAddStringToParametersOperation;
import hu.akoel.grawit.core.operations.SpecialBaseAddVariableToParametersOperation;
import hu.akoel.grawit.core.operations.SpecialBaseClearParametersOperation;
import hu.akoel.grawit.core.operations.SpecialBaseExecuteOperation;
import hu.akoel.grawit.core.operations.TabOperation;
import hu.akoel.grawit.core.treenodedatamodel.BaseDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.BaseElementDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.DataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.NormalBaseElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.ScriptBaseElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.variable.VariableRootDataModel;
import hu.akoel.grawit.enums.Tag;
import hu.akoel.grawit.enums.list.ElementTypeListEnum;
import hu.akoel.grawit.enums.list.VariableTypeListEnum;
import hu.akoel.grawit.exceptions.XMLBaseConversionPharseException;
import hu.akoel.grawit.exceptions.XMLMissingAttributePharseException;
import hu.akoel.grawit.exceptions.XMLWrongAttributePharseException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.w3c.dom.Element;

import com.opera.core.systems.OperaDriver;

public class CommonOperations {
	
	private static Random rnd = new Random();
	
	private static SimpleDateFormat yearMonthFormat = new SimpleDateFormat("MM/yyyy");
	private static SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

	public static enum Browser{
		FIREFOX,
		EXPLORER,
		CHROME,
		OPERA		
	}
	
	public static String getTranslation( String code ){
		return ResourceBundle.getBundle("hu.akoel.grawit.resourcebundle.Translation", WorkingDirectory.getInstance().getLocale() ).getString( code );
	}
	
	public static DateFormat getDateFormat(){
		return DateFormat.getDateInstance(DateFormat.SHORT, WorkingDirectory.getInstance().getLocale() );
	}

	public static SimpleDateFormat getYearFormat(){
		return yearFormat;
	}

	public static SimpleDateFormat getYearMonthFormat(){
		return yearMonthFormat;
	}
	
	public static String getRandomString( String sampleString, int size) {
		StringBuilder sb = new StringBuilder(size);
		for (int i = 0; i < size; i++)
			sb.append( sampleString.charAt( rnd.nextInt( sampleString.length() ) ) );
		return sb.toString();
	}
	
	public static String getRandomStringIntegerRange( int from, int to ){
		return String.valueOf( rnd.nextInt( from + to ) + from );
	}
	
	public static String getRandomStringDouble( double from, double to, int decimalSize ) {
		
		double randomValue = from + (to - from) * rnd.nextDouble();		
		DecimalFormat df = new DecimalFormat( StringUtils.rightPad("#.", decimalSize, "#") );
		
		return df.format( randomValue );
	}
	
	public static String getRandomStringDate( Calendar begin, Calendar end, SimpleDateFormat dateFormat ){
		long rangeBegin = begin.getTimeInMillis();
		long rangeEnd = end.getTimeInMillis();
		long diff = (long)((rangeEnd - rangeBegin + 1) * rnd.nextDouble());
		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( new Timestamp(diff + rangeBegin));
		
		return dateFormat.format( calendar.getTime() );		
	}
	
	public static String getRandomStringYear( int begin, int end ){
		Calendar calendarBegin = new GregorianCalendar(begin, 01, 01 );
		Calendar calendarEnd = new GregorianCalendar(end, 01, 01 );
		return getRandomStringDate( calendarBegin, calendarEnd, yearFormat );
	}
	
	public static String getRandomStringYearMonth( int beginYear, int beginMonth, int endYear, int endMonth, SimpleDateFormat yearMonthFormat ){
		Calendar calendarBegin = new GregorianCalendar(beginYear, beginMonth, 01 );
		Calendar calendarEnd = new GregorianCalendar(endYear, endMonth, 01 );
		return getRandomStringDate( calendarBegin, calendarEnd, yearMonthFormat );
	}
	
	
	public static Calendar getRandomDate( Calendar begin, Calendar end ){
		long rangeBegin = begin.getTimeInMillis();
		long rangeEnd = end.getTimeInMillis();
		long diff = (long)((rangeEnd - rangeBegin + 1) * rnd.nextDouble());
		
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime( new Timestamp(diff + rangeBegin));
		
		return calendar;
	}
	
	public static WebDriver getDriver( Browser browser ){
		WebDriver driver = null;
		
		if( browser.equals(Browser.FIREFOX ) ){
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("pdfjs.disabled", true);		
			profile.setPreference("media.navigator.permission.disabled", true);
			driver =  new FirefoxDriver(profile);
		}else if( browser.equals( Browser.CHROME )){
			driver = new ChromeDriver();
		}else if( browser.equals( Browser.EXPLORER)){
			driver = new InternetExplorerDriver();
		}else if( browser.equals( Browser.OPERA )){
			driver = new OperaDriver();
		}
		return driver;
	}

	 public static ImageIcon createImageIcon(String file) {
		 ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		 //TODO kitalalni valami inteligensebb format
		 java.net.URL imgURL = classLoader.getResource( "hu/akoel/grawit/icons/" + file );		 

		 if (imgURL != null) {
			 return new ImageIcon(imgURL);
		 } else {	           
			 return null;
		 }
	 }
	 
	 public static String getMethodName(int node){
		 final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		 return ste[ node ].getMethodName();
	 }
	 
	 public static String getClassName(int node){
		 final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		 return ste[ node ].getClassName();
	 }
	 
	 public static int getLineNumber(int node){
		 final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		 return ste[ node ].getLineNumber();
	 }

	 /**
	  * Megkeresi a parameterkent megadott DataModelInterface aktualis szintjen a name nevu es tag Tag-u node-ot
	  * Ha nem talalja, akkor null-t ad vissza, ha megtalalja, akkor pedig a node-ot
	  * 
	  * @param actualBaseDataModel
	  * @param name
	  * @return
	  */
	 public static DataModelAdapter getDataModelByNameInLevel( DataModelAdapter actualBaseDataModel, Tag tag, String name ){
		 int childCount = actualBaseDataModel.getChildCount();
		 for( int i = 0; i < childCount; i++ ){
			 DataModelAdapter dm = (DataModelAdapter)actualBaseDataModel.getChildAt( i );
			 if( dm.getName().equals( name ) && dm.getTag().equals( tag )){
				 return dm;
			 }
		 }
		 return null;
	 }	 
	 
	 public static class ValueVerifier extends InputVerifier{
		 private ArrayList<Object> parameterList;
		 private String defaultValue;
		 private int parameterOrder;
		 private VariableTypeListEnum type;
			
		 String goodValue = defaultValue;
			
		 public ValueVerifier( ArrayList<Object> parameterList, VariableTypeListEnum type, String defaultValue, int parameterOrder ){
			 this.parameterList = parameterList;
			 this.defaultValue = defaultValue;
			 this.parameterOrder = parameterOrder;
			 this.type = type;
			 
			 goodValue = defaultValue;
		 }	
			
		 @Override
		 public boolean verify(JComponent input) {
			 JTextField text = (JTextField)input;
			 String possibleValue = text.getText();

			 try {
				 //Kiprobalja, hogy konvertalhato-e
				 Object value = getConverted(possibleValue);//type.getParameterClass(parameterOrder).getConstructor(String.class).newInstance(possibleValue);
				 parameterList.set( parameterOrder, value );
				 goodValue = possibleValue;
					
			 } catch (Exception e) {
				 text.setText( goodValue );
				 return false;
			 }				
			 return true;
		 }
			
		 public Object getConverted( String possibleValue ) throws Exception{
			 return type.getParameterClass(parameterOrder).getConstructor(String.class).newInstance(possibleValue);
		 }
	 }
	 
	 
		public static ElementOperationAdapter getElementOperation( Element element, BaseElementDataModelAdapter baseElement, DataModelAdapter dataModel, ElementOperationAdapter elementOperation, Tag rootTag, String attr_operation, VariableRootDataModel variableRootDataModel ) throws XMLMissingAttributePharseException, XMLBaseConversionPharseException, XMLWrongAttributePharseException{		
		
			//=============================
			//
			// Operation a muvelet alapjan
			//
			//=============================
			if( !element.hasAttribute( attr_operation ) ){
				throw new XMLMissingAttributePharseException( rootTag, dataModel.getTag(), attr_operation );			
			}
			String operationString = element.getAttribute( attr_operation );		

			//Ha SpecialBase ElementDataModel
			if( baseElement instanceof ScriptBaseElementDataModel ){

				if( baseElement.getElementType().equals( ElementTypeListEnum.SCRIPT ) ){

					//ADD_STORED_TO_PARAMETERS
					if( operationString.equals( SpecialBaseAddStoreToParametersOperation.getStaticName() ) ){
						elementOperation = new SpecialBaseAddStoreToParametersOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName()  );
					
						//ADD_VARIABLE_TO_PARAMETERS
					}else if( operationString.equals( SpecialBaseAddVariableToParametersOperation.getStaticName() ) ){
						elementOperation = new SpecialBaseAddVariableToParametersOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
					
						//ADD_STRING_TO_PARAMETERS
					}else if( operationString.equals( SpecialBaseAddStringToParametersOperation.getStaticName() ) ){
						elementOperation = new SpecialBaseAddStringToParametersOperation( element, rootTag, dataModel.getTag() );
					
						//CLEAR_PARAMETERS
					}else if( operationString.equals( SpecialBaseClearParametersOperation.getStaticName() ) ){
						elementOperation = new SpecialBaseClearParametersOperation();
					
						//EXECUTE_SCRIPT
					}else if( operationString.equals( SpecialBaseExecuteOperation.getStaticName() ) ){
						elementOperation = new SpecialBaseExecuteOperation();

					}
				
				//Minden egyeb esetben error
				}else{
					throw new XMLWrongAttributePharseException( BaseDataModelAdapter.getRootTag(), ScriptBaseElementDataModel.TAG, DataModelAdapter.ATTR_NAME, baseElement.getName(), ScriptBaseElementDataModel.ATTR_ELEMENT_TYPE, baseElement.getElementType().name() );
				}
			
			
			//Ha NormalBase ElementDataModel
			}else if( baseElement instanceof NormalBaseElementDataModel ){

				//
				// A tipus az erosebb. Ha a tipushoz nem megfelelo operation van rendelve, akkor egy 
				// alapertelmezett operationt kap
				//
				//-------------
				// Link	a tipus
				//-------------
				if( baseElement.getElementType().equals( ElementTypeListEnum.LINK ) ){
			
					//CLICK
					if( operationString.equals( ClickLeftOperation.getStaticName() ) ){
				
						elementOperation = new ClickLeftOperation();
					
					//COMPARE TEXT TO VARIABLE
					}else if( operationString.equals( CompareTextToVariableOperation.getStaticName() ) ){
				
						elementOperation = new CompareTextToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE TEXT TO STORED
					}else if( operationString.equals( CompareTextToStoredElementOperation.getStaticName() ) ){
				
						elementOperation = new CompareTextToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE TEXT TO STRING
					}else if( operationString.equals( CompareTextToStringOperation.getStaticName() ) ){
				
						elementOperation = new CompareTextToStringOperation( element, rootTag, dataModel.getTag() );
					
					//GAIN TEXT TO ELEMENT
					}else if( operationString.equals( GainTextToElementOperation.getStaticName() ) ){
										
						elementOperation = new GainTextToElementOperation( element, rootTag, dataModel.getTag() );				
			
					//OUTPUT STORED
					}else if( operationString.equals( OutputStoredElementOperation.getStaticName() ) ){ 
				
						elementOperation = new OutputStoredElementOperation( element, rootTag, dataModel.getTag() );
				
					//Ha nem a tipusnak megfelelo a muvelet, akkor azt Click-nek vesszuk
					}else{
				
						elementOperation = new ClickLeftOperation();
					}
		
				//---------------
				// Button a tipus
				//---------------
				}else if( ((NormalBaseElementDataModel)baseElement).getElementType().equals(ElementTypeListEnum.BUTTON)){
			
					//CLICK
					if( operationString.equals( ClickLeftOperation.getStaticName() ) ){

						elementOperation = new ClickLeftOperation();
				
					//Ha nem a tipusnak megfelelo az muvelet, akkor is Click az operation
					}else{
					
						elementOperation = new ClickLeftOperation();				
					}
			
				//-----------------
				// Checkbox a tipus
				//-----------------
				}else if( ((NormalBaseElementDataModel)baseElement).getElementType().equals( ElementTypeListEnum.CHECKBOX) ){
			
					//CLICK
					if( operationString.equals( ClickLeftOperation.getStaticName() ) ){
				
						elementOperation = new ClickLeftOperation();
				
					//COMPARE VALUE TO VARIABLE
					}else if( operationString.equals( CompareValueToVariableOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//COMPARE VALUE TO STORED
					}else if( operationString.equals( CompareValueToStoredElementOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE VALUE TO STRING
					}else if( operationString.equals( CompareValueToStringOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToStringOperation( element, rootTag, dataModel.getTag() );				
				
					//GAIN VALUE TO VARIABLE
					}else if( operationString.equals( GainValueToVariableOperation.getStaticName() ) ){
					
						elementOperation = new GainValueToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//GAIN VALUE TO ELEMENT STORAGE
					}else if( operationString.equals( GainValueToElementStorageOperation.getStaticName() ) ){
						
						elementOperation = new GainValueToElementStorageOperation( element, rootTag, dataModel.getTag() );

					//OUTPUT STORED
					}else if( operationString.equals( OutputStoredElementOperation.getStaticName() ) ){ 
				
						elementOperation = new OutputStoredElementOperation( element, rootTag, dataModel.getTag() );
				
					//Ha nem a tipusnak megfelelo az muvelet, akkor is Click az operation
					}else{
				
						elementOperation = new ClickLeftOperation();
				
					}
			
				//---------------------
				// Radio button a tipus
				//---------------------
				}else if( ((NormalBaseElementDataModel)baseElement).getElementType().equals( ElementTypeListEnum.RADIOBUTTON ) ){
			
					//CLICK
					if( operationString.equals( ClickLeftOperation.getStaticName() ) ){
				
						elementOperation = new ClickLeftOperation();
				
					//COMPARE TEXT TO VARIABLE
					}else if( operationString.equals( CompareTextToVariableOperation.getStaticName() ) ){
	
						elementOperation = new CompareTextToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
	
					//COMPARE TEXT TO STORED
					}else if( operationString.equals( CompareTextToStoredElementOperation.getStaticName() ) ){
	
						elementOperation = new CompareTextToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
	
					//COMPARE TEXT TO STRING
					}else if( operationString.equals( CompareTextToStringOperation.getStaticName() ) ){
	
						elementOperation = new CompareTextToStringOperation( element, rootTag, dataModel.getTag() );
				
					//COMPARE VALUE TO VARIABLE
					}else if( operationString.equals( CompareValueToVariableOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//COMPARE VALUE TO STORED
					}else if( operationString.equals( CompareValueToStoredElementOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE VALUE TO STRING
					}else if( operationString.equals( CompareValueToStringOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToStringOperation( element, rootTag, dataModel.getTag() );
				
					//GAIN VALUE TO VARIABLE
					}else if( operationString.equals( GainValueToVariableOperation.getStaticName() ) ){
					
						elementOperation = new GainValueToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//GAIN VALUE TO ELEMENT STORAGE
					}else if( operationString.equals( GainValueToElementStorageOperation.getStaticName() ) ){
						
						elementOperation = new GainValueToElementStorageOperation( element, rootTag, dataModel.getTag() );

					//OUTPUT STORED
					}else if( operationString.equals( OutputStoredElementOperation.getStaticName() ) ){ 
				
						elementOperation = new OutputStoredElementOperation( element, rootTag, dataModel.getTag() );
				
					//Ha nem a tipusnak megfelelo az muvelet, akkor is Click az operation
					}else{
					
						elementOperation = new ClickLeftOperation();
					
					}
		
				//--------------
				// Field a tipus
				//--------------
				}else if( ((NormalBaseElementDataModel)baseElement).getElementType().equals( ElementTypeListEnum.FIELD ) ){
			
					//CLEAR
					if( operationString.equals( ClearOperation.getStaticName() ) ){
			
						elementOperation = new ClearOperation();
				
					//CLICK
					}else if( operationString.equals( ClickLeftOperation.getStaticName() ) ){
				
						elementOperation = new ClickLeftOperation();
				
					//TAB
					}else if( operationString.equals( TabOperation.getStaticName() ) ){
				
						elementOperation = new TabOperation();
				
					//FILL VARIABLE
					}else if( operationString.equals( FillWithVariableElementOperation.getStaticName() ) ){
				
						elementOperation = new FillWithVariableElementOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//FILL BASEELEMENT
					}else if( operationString.equals( FillWithBaseElementOperation.getStaticName() ) ){
				
						elementOperation = new FillWithBaseElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//FILL STRING
					}else if( operationString.equals( FillWithStringOperation.getStaticName() ) ){
				
						elementOperation = new FillWithStringOperation( element, rootTag, dataModel.getTag() );
				
					//COMPARE VALUE TO VARIABLE
					}else if( operationString.equals( CompareValueToVariableOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//COMPARE VALUE TO STORED
					}else if( operationString.equals( CompareValueToStoredElementOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE VALUE TO STRING
					}else if( operationString.equals( CompareValueToStringOperation.getStaticName() ) ){
				
						elementOperation = new CompareValueToStringOperation( element, rootTag, dataModel.getTag() );
			
					//GAIN VALUE TO VARIABLE
					}else if( operationString.equals( GainValueToVariableOperation.getStaticName() ) ){
					
						elementOperation = new GainValueToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//GAIN VALUE TO ELEMENT STORAGE
					}else if( operationString.equals( GainValueToElementStorageOperation.getStaticName() ) ){
						
						elementOperation = new GainValueToElementStorageOperation( element, rootTag, dataModel.getTag() );

					//OUTPUT STORED
					}else if( operationString.equals( OutputStoredElementOperation.getStaticName() ) ){ 
				
						elementOperation = new OutputStoredElementOperation( element, rootTag, dataModel.getTag() );
				
					//Ha nem a tipusnak megfelelo az muvelet, akkor Clear lesz a muvelet
					}else{
					
						elementOperation = new ClearOperation();
					}
		
				//---------
				// Text
				//---------
				}else if( ((NormalBaseElementDataModel)baseElement).getElementType().equals( ElementTypeListEnum.TEXT ) ){
			
					//COMPARE TEXT TO VARIABLE
					if( operationString.equals( CompareTextToVariableOperation.getStaticName() ) ){
				
						elementOperation = new CompareTextToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE TEXT TO STORED
					}else if( operationString.equals( CompareTextToStoredElementOperation.getStaticName() ) ){
				
						elementOperation = new CompareTextToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE TEXT TO STRING
					}else if( operationString.equals( CompareTextToStringOperation.getStaticName() ) ){
				
						elementOperation = new CompareTextToStringOperation( element, rootTag, dataModel.getTag() );
				
					//LEFT MOUSE CLICK
					}else if( operationString.equals( ClickLeftOperation.getStaticName() ) ){
					
						elementOperation = new ClickLeftOperation();						

					//RIGHT MOUSE CLICK
					}else if( operationString.equals( ClickRightOperation.getStaticName() ) ){
						
						elementOperation = new ClickRightOperation();						
						
					//GAIN TEXT TO ELEMENT
					}else if( operationString.equals( GainTextToElementOperation.getStaticName() ) ){
									
						elementOperation = new GainTextToElementOperation( element, rootTag, dataModel.getTag() );				
				
					//OUTPUT STORED
					}else if( operationString.equals( OutputStoredElementOperation.getStaticName() ) ){ 
	
						elementOperation = new OutputStoredElementOperation( element, rootTag, dataModel.getTag() );
				
					//Ha nem a tipusnak megfelelo az muvelet
					}else{
				
						//Default muvelet
						elementOperation = new GainTextToElementOperation( element, rootTag, dataModel.getTag() );	
				
						//throw new XMLWrongAttributePharseException( BaseDataModelInterface.getRootTag(), BaseElementDataModel.TAG, DataModelAdapter.ATTR_NAME, baseElement.getName(), BaseElementDataModel.ATTR_ELEMENT_TYPE, baseElement.getElementType().name() );
						//TODO ez nem jo uzenet
					}
			
				//---------
				// List
				//---------
				}else if( ((NormalBaseElementDataModel)baseElement).getElementType().equals( ElementTypeListEnum.LIST ) ){
						
					//Select Variable Element
					if( operationString.equals( SelectVariableElementOperation.getStaticName() ) ){
				
						elementOperation = new SelectVariableElementOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//Select Base Element
					}else if( operationString.equals( SelectBaseElementOperation.getStaticName() ) ){
				
						elementOperation = new SelectBaseElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
		
					//Select String
					}else if( operationString.equals( SelectStringOperation.getStaticName() ) ){
				
						elementOperation = new SelectStringOperation( element, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
		
					//Click
					}else if( operationString.equals( ClickLeftOperation.getStaticName() ) ){
				
						elementOperation = new ClickLeftOperation( );
				
					//Tab
					}else if( operationString.equals( TabOperation.getStaticName() ) ){
				
						elementOperation = new TabOperation( );
				
					//COMPARE TO VARIABLE
					}else if( operationString.equals( CompareListToVariableOperation.getStaticName() ) ){
				
						elementOperation = new CompareListToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//COMPARE TO STORED
					}else if( operationString.equals( CompareListToStoredElementOperation.getStaticName() ) ){
				
						elementOperation = new CompareListToStoredElementOperation( element, (BaseRootDataModel)baseElement.getRoot(), rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );
				
					//COMPARE TO STRING
					}else if( operationString.equals( CompareListToStringOperation.getStaticName() ) ){
				
						elementOperation = new CompareListToStringOperation( element, rootTag, dataModel.getTag() );
				
					//GAIN TO VARIABLE
					}else if( operationString.equals( GainListToVariableOperation.getStaticName() ) ){
						
						elementOperation = new GainListToVariableOperation( element, variableRootDataModel, rootTag, dataModel.getTag(), attr_operation, dataModel.getName() );

					//GAIN TO ELEMENT
					}else if( operationString.equals( GainListToElementStorageOperation.getStaticName() ) ){
								
						elementOperation = new GainListToElementStorageOperation( element, rootTag, dataModel.getTag() );
			
					//OUTPUT STORED
					}else if( operationString.equals( OutputStoredElementOperation.getStaticName() ) ){ 
					
						elementOperation = new OutputStoredElementOperation( element, rootTag, dataModel.getTag() );				
			
					}else{
				
						elementOperation = new ClickLeftOperation( );
				
					}
			
				//Minden egyeb esetben error
				}else{
					throw new XMLWrongAttributePharseException( BaseDataModelAdapter.getRootTag(), NormalBaseElementDataModel.TAG, DataModelAdapter.ATTR_NAME, baseElement.getName(), NormalBaseElementDataModel.ATTR_ELEMENT_TYPE, ((NormalBaseElementDataModel)baseElement).getElementType().name() );
				}
		
			}
			
			return elementOperation;
		}


}