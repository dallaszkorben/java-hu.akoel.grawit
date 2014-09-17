package hu.akoel.grawit.gui.editor.variable;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.variable.VariableElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.variable.VariableNodeDataModel;
import hu.akoel.grawit.enums.list.ListEnumParameterType;
import hu.akoel.grawit.gui.editor.DataEditor;
import hu.akoel.grawit.gui.editors.component.ComboBoxComponent;
import hu.akoel.grawit.gui.editors.component.TextFieldComponent;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersIntegerComponent;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersComponentInterface;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersRandomDateComponent;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersRandomDoubleComponent;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersRandomIntegerComponent;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersRandomStringComponent;
import hu.akoel.grawit.gui.editors.component.variableparameter.VariableParametersStringComponent;
import hu.akoel.grawit.gui.tree.Tree;

import javax.swing.JLabel;
import javax.swing.tree.TreeNode;

public class VariableElementEditor extends DataEditor{
	
	private static final long serialVersionUID = -7285419881714492620L;
	
	private Tree tree;
	private VariableElementDataModel nodeForModify;
	private VariableNodeDataModel nodeForCapture;
	private EditMode mode;
	
	private JLabel labelName;
	private TextFieldComponent fieldName;
	private JLabel labelVariableType;
	private ComboBoxComponent<ListEnumParameterType> fieldVariableType;
	private JLabel labelVariableParameters;
	private VariableParametersComponentInterface fieldVariableParameters;
	
	private ListEnumParameterType type;
	
	/**
	 *  Uj VariableElement rogzitese - Insert
	 *  
	 * @param tree
	 * @param selectedNode
	 */
	public VariableElementEditor( Tree tree, VariableNodeDataModel selectedNode ){

		super( VariableElementDataModel.getModelNameToShowStatic());

		this.tree = tree;
		this.nodeForCapture = selectedNode;
		this.mode = null;

		commonPre();
		
		//Name
		fieldName.setText( "" );
		
		//Type - String
		type = ListEnumParameterType.STRING_PARAMETER;		
				
		//Parameters
		//fieldVariableParameters = new VariableParametersStringComponent(type);				
				
		commonPost( );
				
	}
		
	/**
	 * 
	 * Mar letezo VariableElement modositasa vagy megtekintese
	 * 
	 * @param tree
	 * @param selectedElement
	 * @param mode
	 */
	public VariableElementEditor( Tree tree, VariableElementDataModel selectedElement, EditMode mode ){		

		super( mode, selectedElement.getNodeTypeToShow() );

		this.tree = tree;
		this.nodeForModify = selectedElement;
		this.mode = mode;
	
		commonPre();
		
		//Name
		fieldName.setText( selectedElement.getName() );

		//Variable Type
		type = selectedElement.getType();
		
		//Parameters
		//fieldVariableParameters = new VariableParametersStringComponent(type, selectedElement.getParameters() );
		
		commonPost( );		
		
	}

	private void commonPre(){
		
		//Name
		fieldName = new TextFieldComponent();
	
		//VariableTypeSelector		
		fieldVariableType = new ComboBoxComponent<>();
		for( int i = 0; i < ListEnumParameterType.getSize(); i++ ){
			fieldVariableType.addItem( ListEnumParameterType.getVariableParameterTypeByIndex(i) );
		}
		fieldVariableType.addItemListener( new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {

				int index = fieldVariableType.getSelectedIndex();					

				//Ha megvaltoztattam a tipust
				if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ){ 
					
					type = ListEnumParameterType.getVariableParameterTypeByIndex(index);
					
					//STRING_PARAMETER
					if( ListEnumParameterType.getVariableParameterTypeByIndex(index).equals(ListEnumParameterType.STRING_PARAMETER ) ){

						//Nem ez az elso valtoztatas
						if( null != fieldVariableParameters ){
							VariableElementEditor.this.remove(labelVariableParameters, fieldVariableParameters.getComponent());
							fieldVariableParameters = new VariableParametersStringComponent(type);
						}else{
							//Modositas volt
							if( null != nodeForModify ){
								fieldVariableParameters = new VariableParametersStringComponent(type, nodeForModify.getParameters() );
							}else{
								fieldVariableParameters = new VariableParametersStringComponent(type);
							}
						}
						
						
					}else if( ListEnumParameterType.getVariableParameterTypeByIndex(index).equals(ListEnumParameterType.INTEGER_PARAMETER ) ){

						//Nem ez az elso valtoztatas
						if( null != fieldVariableParameters ){
							VariableElementEditor.this.remove(labelVariableParameters, fieldVariableParameters.getComponent());
							fieldVariableParameters = new VariableParametersIntegerComponent(type);
						}else{
							//Modositas volt
							if( null != nodeForModify ){
								fieldVariableParameters = new VariableParametersIntegerComponent(type, nodeForModify.getParameters() );
							}else{
								fieldVariableParameters = new VariableParametersIntegerComponent(type);
							}
						}
						
					//RANDOM_STRING_PARAMETER	
					}else if( ListEnumParameterType.getVariableParameterTypeByIndex(index).equals(ListEnumParameterType.RANDOM_STRING_PARAMETER ) ){
						
						//Nem ez az elso valtoztatas
						if( null != fieldVariableParameters ){
							VariableElementEditor.this.remove(labelVariableParameters, fieldVariableParameters.getComponent());
							fieldVariableParameters = new VariableParametersRandomStringComponent(type);
						}else{
							//Modositas volt
							if( null != nodeForModify ){
								fieldVariableParameters = new VariableParametersRandomStringComponent(type, nodeForModify.getParameters() );
							}else{
								fieldVariableParameters = new VariableParametersRandomStringComponent(type);
							}
						}
						
						
					
					//RANDOM_INTEGER_PARAMETER
					}else if( ListEnumParameterType.getVariableParameterTypeByIndex(index).equals(ListEnumParameterType.RANDOM_INTEGER_PARAMETER ) ){

						//Nem ez az elso valtoztatas
						if( null != fieldVariableParameters ){
							VariableElementEditor.this.remove(labelVariableParameters, fieldVariableParameters.getComponent());
							fieldVariableParameters = new VariableParametersRandomIntegerComponent(type);
						}else{
							//Modositas volt
							if( null != nodeForModify ){
								fieldVariableParameters = new VariableParametersRandomIntegerComponent(type, nodeForModify.getParameters() );
							}else{
								fieldVariableParameters = new VariableParametersRandomIntegerComponent(type);
							}
						}
						
						
						
					//RANDOM_DECIMAL_PARAMETER	
					}else if( ListEnumParameterType.getVariableParameterTypeByIndex(index).equals(ListEnumParameterType.RANDOM_DOUBLE_PARAMETER ) ){
						
						//Nem ez az elso valtoztatas
						if( null != fieldVariableParameters ){
							VariableElementEditor.this.remove(labelVariableParameters, fieldVariableParameters.getComponent());
							fieldVariableParameters = new VariableParametersRandomDoubleComponent(type);
						}else{
							//Modositas volt
							if( null != nodeForModify ){
								fieldVariableParameters = new VariableParametersRandomDoubleComponent(type, nodeForModify.getParameters() );
							}else{
								fieldVariableParameters = new VariableParametersRandomDoubleComponent(type);
							}
						}
										
					//RANDOM_DATE_PARAMETER
					}else if( ListEnumParameterType.getVariableParameterTypeByIndex(index).equals(ListEnumParameterType.RANDOM_DATE_PARAMETER ) ){
						
						//Nem ez az elso valtoztatas
						if( null != fieldVariableParameters ){
							VariableElementEditor.this.remove(labelVariableParameters, fieldVariableParameters.getComponent());
							fieldVariableParameters = new VariableParametersRandomDateComponent(type);
						}else{
							//Modositas volt
							if( null != nodeForModify ){
								fieldVariableParameters = new VariableParametersRandomDateComponent(type, nodeForModify.getParameters() );
							}else{
								fieldVariableParameters = new VariableParametersRandomDateComponent(type);
							}
						}										
						
					}
					
					VariableElementEditor.this.add( labelVariableParameters, fieldVariableParameters );
					VariableElementEditor.this.revalidate();
				}
			}
		});		
	}
	
	private void commonPost(){
		
		labelName = new JLabel( CommonOperations.getTranslation("editor.label.name") + ": ");
		labelVariableType = new JLabel( CommonOperations.getTranslation("editor.label.variable.parametertype") + ": ");
		labelVariableParameters = new JLabel("");
		
		//this.add( labelName, fieldName );
		//jx
		this.add( labelName, fieldName );
		this.add( labelVariableType, fieldVariableType );
//		this.add( labelVariableParameters, fieldVariableParameters );

		//Arra kenyszeritem, hogy az elso igazi valasztas is kivaltson egy esemenyt
		fieldVariableType.setSelectedIndex(-1);		
		fieldVariableType.setSelectedIndex( type.getIndex() );	
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
		if( fieldName.getText().length() == 0 ){
			errorList.put( 
					fieldName,
					MessageFormat.format(
							CommonOperations.getTranslation("editor.errormessage.emptyfield"), 
							"'"+labelName.getText()+"'"
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
				if( levelNode instanceof VariableElementDataModel ){
					
					//Ha azonos a nev
					if( ((VariableElementDataModel) levelNode).getName().equals( fieldName.getText() ) ){
					
						//Ha rogzites van, vagy ha modositas, de a vizsgalt node kulonbozik a modositott-tol
						if( null == mode || ( mode.equals( EditMode.MODIFY ) && !levelNode.equals(nodeForModify) ) ){
							
							errorList.put( 
								fieldName, 
								MessageFormat.format( 
										CommonOperations.getTranslation("editor.errormessage.duplicateelement"), 
										fieldName.getText(), 
										CommonOperations.getTranslation("tree.nodetype.variable.element") 
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
		
			//Uj rogzites eseten
			if( null == mode ){			

				VariableElementDataModel newVariableElement = new VariableElementDataModel( fieldName.getText(), type, fieldVariableParameters.getParameters());
				nodeForCapture.add( newVariableElement );
				
			//Modositas eseten
			}else if( mode.equals(EditMode.MODIFY ) ){
		
				nodeForModify.setName( fieldName.getText() );
				nodeForModify.setType( type );
				nodeForModify.setParameters( fieldVariableParameters.getParameters() );
				
			}
			
			//A fa-ban is modositja a nevet (ha az valtozott)
			tree.changed();

		}
		
	}
	
}
