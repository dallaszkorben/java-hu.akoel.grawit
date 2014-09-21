package hu.akoel.grawit.gui.editor.param;

import java.awt.Component;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.operations.ElementOperationInterface;
import hu.akoel.grawit.core.operations.GainTextPatternOperation;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.BasePageDataModel;
import hu.akoel.grawit.core.treenodedatamodel.base.BaseRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamPageDataModel;
import hu.akoel.grawit.core.treenodedatamodel.param.ParamRootDataModel;
import hu.akoel.grawit.core.treenodedatamodel.variable.VariableRootDataModel;
import hu.akoel.grawit.enums.list.ElementTypeListEnum;
import hu.akoel.grawit.enums.list.elementtypeoperations.ButtonElementTypeOperationsListEnum;
import hu.akoel.grawit.enums.list.elementtypeoperations.CheckboxElementTypeOperationsListEnum;
import hu.akoel.grawit.enums.list.elementtypeoperations.ElementTypeOperationsListEnumInterface;
import hu.akoel.grawit.enums.list.elementtypeoperations.FieldElementTypeOperationsListEnum;
import hu.akoel.grawit.enums.list.elementtypeoperations.LinkElementTypeOperationsListEnum;
import hu.akoel.grawit.enums.list.elementtypeoperations.RadiobuttonElementTypeOperationsListEnum;
import hu.akoel.grawit.enums.list.elementtypeoperations.TextElementTypeOperationsListEnum;
import hu.akoel.grawit.gui.editor.DataEditor;
import hu.akoel.grawit.gui.editors.component.EditorComponentInterface;
import hu.akoel.grawit.gui.editors.component.TextFieldComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.ButtonElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.CheckboxElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.EmptyElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.FieldElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.LinkElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.ElementTypeComponentInterface;
import hu.akoel.grawit.gui.editors.component.elementtype.RadiobuttonElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.elementtype.TextElementTypeComponent;
import hu.akoel.grawit.gui.editors.component.treeselector.BaseElementTreeSelectorComponent;
import hu.akoel.grawit.gui.tree.Tree;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.TreeNode;

public class ParamElementEditor extends DataEditor{
	
	private static final long serialVersionUID = -7285419881714492620L;
	
	private Tree tree;
	private ParamElementDataModel nodeForModify;
	private ParamPageDataModel nodeForCapture;
	private EditMode mode;
	
	private JLabel labelName;
	private TextFieldComponent fieldName;

	private JLabel labelBaseElementSelector;
	private BaseElementTreeSelectorComponent fieldBaseElementSelector;	
	
	private JLabel labelElementTypeSelector;
	private ElementTypeComponentInterface<?> elementTypeComponent;
	
	BaseRootDataModel baseRootDataModel;
	VariableRootDataModel variableRootDataModel;
	
	/**
	 *  Uj ParamPageElement rogzitese - Insert
	 *  
	 * @param tree
	 * @param selectedPage
	 */
	public ParamElementEditor( Tree tree, ParamPageDataModel selectedPage, ParamRootDataModel paramRootDataModel, VariableRootDataModel variableRootDataModel ){

		super( ParamElementDataModel.getModelNameToShowStatic());
		
		this.tree = tree;
		this.nodeForCapture = selectedPage;
		this.mode = null;

		commonPre(  paramRootDataModel, variableRootDataModel );
		
		//Name
		fieldName.setText( "" );

		//Base Element
		BasePageDataModel basePage = selectedPage.getBasePage();
		fieldBaseElementSelector = new BaseElementTreeSelectorComponent( basePage ); 

		//fieldOperationComponent = new EmptyElementTypeComponent();

		//baseRootDataModel = (BaseRootDataModel)basePage.getRoot();
			
		commonPost( null, basePage );
	}
		
	/**
	 * 
	 * Mar letezo ParamPageElement modositasa vagy megtekintese
	 * 
	 * @param tree
	 * @param selectedElement
	 * @param mode
	 */
	public ParamElementEditor( Tree tree, ParamElementDataModel selectedElement, ParamRootDataModel paramRootDataModel, VariableRootDataModel variableRootDataModel, EditMode mode ){		

		super( mode, selectedElement.getNodeTypeToShow());

		this.tree = tree;
		this.nodeForModify = selectedElement;
		this.mode = mode;	

		commonPre( paramRootDataModel, variableRootDataModel );
		
		//Name
		fieldName.setText( selectedElement.getName() );

		//Selector a BaseElement valasztashoz - A root a basePage (nem latszik)
		BaseElementDataModel baseElement = selectedElement.getBaseElement();
		BasePageDataModel basePage = ((ParamPageDataModel)selectedElement.getParent()).getBasePage();		
		fieldBaseElementSelector = new BaseElementTreeSelectorComponent( basePage, baseElement );
		
		commonPost( baseElement, basePage );
		
		//changeOperation(baseElement);
		
	}

	private void commonPre( final ParamRootDataModel paramRootDataModel, final VariableRootDataModel variableRootDataModel ){
			
		//Name
		fieldName = new TextFieldComponent();
		
		this.variableRootDataModel = variableRootDataModel;
		
		
/*		
		fieldOperation.addItemListener( new ItemListener() {
			
			private boolean hasBeenHere = false;
			
			@Override
			public void itemStateChanged(ItemEvent e) {

				int index = fieldOperation.getSelectedIndex();					

				//Ha megvaltoztattam a tipust
				if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ){ 
					
					 operation = Torlendo_Operation.getOperationByIndex(index);
					 
					 //Mindenkeppen torolni kell, ha letezett
					 if( null != fieldVariableSelector ){
						 ParamElementEditor.this.remove( labelVariableSelector, fieldVariableSelector.getComponent() );
						 ParamElementEditor.this.remove( labelListSelectionType, fieldListSelectionType );						
						 ParamElementEditor.this.repaint();
					 }else if( null != fieldFieldBaseElementSelector ){
						 ParamElementEditor.this.remove( labelFieldBaseElementSelector, fieldFieldBaseElementSelector );
						 ParamElementEditor.this.repaint();
					 }
					 
					 //LIST
					 if( operation.equals( Torlendo_Operation.LIST_VARIABLE ) ){
						
						 //Ha mar volt valtoztatas, vagy uj ParameterElem szerkesztes tortenik 
						 if( hasBeenHere || null == nodeForModify ){
							 
							 //Akkor uresen kell kapnom a mezot
							 fieldVariableSelector = new VariableTreeSelectorComponent( variableRootDataModel );
							 fieldListSelectionType.setSelectedIndex( ListEnumListSelectionBy.BYVISIBLETEXT.getIndex() );
							 
						 //Ha viszont most van itt eloszor es a ParameterElem modositasa tortenik
						 }else{
							 Torlendo_ListVariableOperation op = (Torlendo_ListVariableOperation)nodeForModify.getElementOperation();
							 
							 //akkor latnom kell a kivalasztott tartalmat
							 fieldVariableSelector = new VariableTreeSelectorComponent( variableRootDataModel, op.getVariableElement() );
							 fieldListSelectionType.setSelectedIndex(op.getListSelectionType().getIndex());
						 }
							
						 ParamElementEditor.this.add(labelListSelectionType, fieldListSelectionType );
						 ParamElementEditor.this.add( labelVariableSelector, fieldVariableSelector ); 
						 
					 //FIELD_VARIABLE
					 }else if( operation.equals( Torlendo_Operation.FIELD_VARIABLE ) ){
							 
						 //Ha mar volt valtoztatas, vagy uj ParameterElem szerkesztes tortenik 
						 if( hasBeenHere || null == nodeForModify ){
							 
							 //Akkor uresen kell kapnom a mezot
							 fieldVariableSelector = new VariableTreeSelectorComponent( variableRootDataModel );
						 
						 //Ha viszont most van itt eloszor es a ParameterElem modositasa tortenik
						 }else{
							 
							 Torlendo_FieldVariableOperation op = (Torlendo_FieldVariableOperation)nodeForModify.getElementOperation();
							 
							 //akkor latnom kell a kivalasztott tartalmat
							 fieldVariableSelector = new VariableTreeSelectorComponent( variableRootDataModel, op.getVariableElement() );
						 }
							
						 ParamElementEditor.this.add( labelVariableSelector, fieldVariableSelector );
//						 ParamElementEditor.this.revalidate();

					 //FIELD_ELEMENT
					 }else if( operation.equals( Torlendo_Operation.FIELD_ELEMENT ) ){
								 
						 //Ha mar volt valtoztatas, vagy uj ParameterElem szerkesztes tortenik 
						 if( hasBeenHere || null == nodeForModify ){
								 
							 //Akkor uresen kell kapnom a mezot
							 fieldFieldBaseElementSelector = new BaseElementTreeSelectorComponent( baseRootDataModel );
							 
						 //Ha viszont most van itt eloszor es a ParameterElem modositasa tortenik
						 }else{
								 
							 Torlendo_FieldParamElementOperation op = (Torlendo_FieldParamElementOperation)nodeForModify.getElementOperation();
					 
							 //akkor latnom kell a kivalasztott tartalmat
							 fieldFieldBaseElementSelector = new BaseElementTreeSelectorComponent( baseRootDataModel, op.getBaseElement() );
						 }
								
						 ParamElementEditor.this.add( labelFieldBaseElementSelector, fieldFieldBaseElementSelector );
						 
					 //GAIN TEXT
					 }else if( operation.equals( Torlendo_Operation.GAINTEXTPATTERN ) ){
								 
						 //Ha mar volt valtoztatas, vagy uj ParameterElem szerkesztes tortenik 
						 if( hasBeenHere || null == nodeForModify ){
								 
							 //Akkor uresen kell kapnom a mezot
							 fieldPattern = new TextFieldComponent( "" );
							 
						 //Ha viszont most van itt eloszor es a ParameterElem modositasa tortenik
						 }else{
								 
							 Torlendo_GainTextPatternOperation op = (Torlendo_GainTextPatternOperation)nodeForModify.getElementOperation();
					 
							 //akkor latnom kell a kivalasztott tartalmat
							 fieldPattern = new TextFieldComponent( op.getStringPattern() );
						 }
								
						 ParamElementEditor.this.add( labelPattern, fieldPattern );						 
						 
						 
						 
					 }else if( operation.equals( Torlendo_Operation.BUTTON ) ){
					
						 
						 
					 }else if( operation.equals( Torlendo_Operation.CHECKBOX ) ){
						
					 }else if( operation.equals( Torlendo_Operation.RADIOBUTTON ) ){
						
					 }else if( operation.equals( Torlendo_Operation.LINK ) ){
				 
					}else if( operation.equals( Torlendo_Operation.TAB ) ){

					 hasBeenHere = true;						
				}				
			}
		});
*/		
	}
	
	private void commonPost(BaseElementDataModel baseElement, BasePageDataModel basePage ){
		
		baseRootDataModel = (BaseRootDataModel)basePage.getRoot();
		
		fieldBaseElementSelector.getDocument().addDocumentListener( new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				change();				
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				change();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				change();
			}			
			private void change(){
				BaseElementDataModel baseElement = ParamElementEditor.this.fieldBaseElementSelector.getSelectedDataModel();
				changeOperation( baseElement );
			}
		});
		
		labelName = new JLabel( CommonOperations.getTranslation("editor.label.name") + ": ");
		labelBaseElementSelector = new JLabel( CommonOperations.getTranslation("editor.label.param.baseelement") + ": " );
		labelElementTypeSelector = new JLabel( "");
		
		this.add( labelName, fieldName );
		this.add( labelBaseElementSelector, fieldBaseElementSelector );
//		this.add( labelElementTypeSelector, fieldOperationComponent );
		
		
//!!!!Na itt a hiba		
		
		elementTypeComponent = new EmptyElementTypeComponent();
		changeOperation(baseElement);
		
	}
	
	/**
	 * 
	 * Az aktualis elemtipusnak megfelelo komponenst mutatja meg
	 *  
	 * @param baseElement
	 */
	private void changeOperation( BaseElementDataModel baseElement ){

		//Eltavolitja az ott levot
		ParamElementEditor.this.remove( labelElementTypeSelector, elementTypeComponent.getComponent() );
		
		ElementOperationInterface elementOperation;
		
		//Uj
		if( null == nodeForModify ){
			elementOperation = null;
		
		//Modositas
		}else{
			elementOperation = nodeForModify.getElementOperation();
		}		
		
		//Ha uj es elso alkalom
		if( null == baseElement ){		 
		
			elementTypeComponent = new EmptyElementTypeComponent();
			
		//FIELD
		}else if( baseElement.getElementType().name().equals( ElementTypeListEnum.FIELD.name() ) ){
			
			elementTypeComponent = new FieldElementTypeComponent<FieldElementTypeOperationsListEnum>( baseElement.getElementType(), elementOperation, baseRootDataModel, variableRootDataModel);  
			
		//TEXT
		}else if( baseElement.getElementType().name().equals(  ElementTypeListEnum.TEXT.name() ) ){

			elementTypeComponent = new TextElementTypeComponent<TextElementTypeOperationsListEnum>( baseElement.getElementType(), elementOperation );
			
		//LINK	
		}else if( baseElement.getElementType().name().equals(  ElementTypeListEnum.LINK.name() ) ){

			elementTypeComponent = new LinkElementTypeComponent<LinkElementTypeOperationsListEnum>( baseElement.getElementType(), elementOperation );
			
		//LIST
		}else if( baseElement.getElementType().name().equals(  ElementTypeListEnum.TEXT.name() ) ){
			
		//BUTTON
		}else if( baseElement.getElementType().name().equals(  ElementTypeListEnum.BUTTON.name() ) ){
			
			elementTypeComponent = new ButtonElementTypeComponent<ButtonElementTypeOperationsListEnum>( baseElement.getElementType(), elementOperation );
			
		//RADIOBUTTON
		}else if( baseElement.getElementType().name().equals(  ElementTypeListEnum.RADIOBUTTON.name() ) ){

			elementTypeComponent = new RadiobuttonElementTypeComponent<RadiobuttonElementTypeOperationsListEnum>( baseElement.getElementType(), elementOperation);
			
		//CHECKBOX
		}else if( baseElement.getElementType().name().equals(  ElementTypeListEnum.CHECKBOX.name() ) ){
			
			elementTypeComponent = new CheckboxElementTypeComponent<CheckboxElementTypeOperationsListEnum>( baseElement.getElementType(), elementOperation );
					
		}		
		
		//Elhelyezi az ujat		
		ParamElementEditor.this.add( labelElementTypeSelector, elementTypeComponent.getComponent() );
		ParamElementEditor.this.repaint();
		
	}
	
	
	@Override
	public void save() {
		
		//
		//Ertekek trimmelese
		//
		fieldName.setText( fieldName.getText().trim() );
				
		//
		//Hibak eseten a hibas mezok osszegyujtese
		//
		
		//fieldName
		LinkedHashMap<Component, String> errorList = new LinkedHashMap<Component, String>();
		
		//Nincs nev megadva
		if( fieldName.getText().length() == 0 ){
			errorList.put( 
					fieldName,
					MessageFormat.format(
							CommonOperations.getTranslation("editor.errormessage.emptyfield"), 
							"'"+labelName.getText()+"'"
					)
			);
		
		//Nincs BaseElement kivalasztva
		}else if( null == fieldBaseElementSelector.getSelectedDataModel()){
			errorList.put( 
					fieldBaseElementSelector,
					MessageFormat.format(
							CommonOperations.getTranslation("editor.errormessage.emptyfield"), 
							"'"+labelBaseElementSelector.getText()+"'"
					)
			);	
			
			
		}else{

			TreeNode nodeForSearch = null;
			
			if( null == mode ){
				
				nodeForSearch = nodeForCapture;
				
			}else if( mode.equals( EditMode.MODIFY )){
				
				nodeForSearch = nodeForModify.getParent();
				
			}

			//Megnezi, hogy van-e masik azonos nevu elem			
			int childrenCount = nodeForSearch.getChildCount();
			for( int i = 0; i < childrenCount; i++ ){
				TreeNode levelNode = nodeForSearch.getChildAt( i );
				
				//Ha Element-rol van szo 
				if( levelNode instanceof ParamElementDataModel ){
					
					//Ha azonos a nev azzal amit most mentenek
					if( ((ParamElementDataModel) levelNode).getName().equals( fieldName.getText() ) ){
					
						//Ha rogzites van, vagy ha modositas, de a vizsgalt node kulonbozik a modositott-tol
						if( null == mode || ( mode.equals( EditMode.MODIFY ) && !levelNode.equals(nodeForModify) ) ){
							
							errorList.put( 
								fieldName, 
								MessageFormat.format( 
										CommonOperations.getTranslation("editor.errormessage.duplicateelement"), 
										fieldName.getText(), 
										CommonOperations.getTranslation("tree.nodetype.param.element") 
								) 
							);
							break;
						}
					}
				}
			}
		}

		//Operation
		
		//Volt hiba
		if( errorList.size() != 0 ){
			
			//Hibajelzes
			this.errorAt( errorList );
		
		//Ha nem volt hiba akkor a valtozok veglegesitese
		}else{
			
			BaseElementDataModel baseElement = fieldBaseElementSelector.getSelectedDataModel();
			ElementOperationInterface elementOperation = elementTypeComponent.getElementOperation();

			//Uj rogzites eseten
			if( null == mode ){			
				
				ParamElementDataModel newParamElement = new ParamElementDataModel( fieldName.getText(), baseElement, elementOperation );			
				
				nodeForCapture.add( newParamElement );
				
			//Modositas eseten
			}else if( mode.equals(EditMode.MODIFY ) ){
		
				nodeForModify.setName( fieldName.getText() );
				nodeForModify.setBaseElement(baseElement);
				nodeForModify.setOperation( elementOperation );
			}
			
			//A fa-ban is modositja a nevet (ha az valtozott)
			tree.changed();
		}
		
	}
	
}
