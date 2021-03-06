package hu.akoel.grawit.gui.editor.driver;

import java.awt.Component;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.driver.DriverExplorerDataModel;
import hu.akoel.grawit.core.treenodedatamodel.driver.DriverFolderDataModel;
import hu.akoel.grawit.gui.editor.DataEditor;
import hu.akoel.grawit.gui.editors.component.TextAreaComponent;
import hu.akoel.grawit.gui.editors.component.TextFieldComponent;
import hu.akoel.grawit.gui.editors.component.fileselector.FileSelectorComponent;
import hu.akoel.grawit.gui.tree.Tree;

import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.TreeNode;

public class DriverExplorerEditor extends DataEditor{

	private static final long serialVersionUID = 5823748025740682822L;
	
	private static final FileNameExtensionFilter fileExtention = null;//new FileNameExtensionFilter("exe", "exe");
	
	private Tree tree; 
	private DriverExplorerDataModel nodeForModify;
	private DriverFolderDataModel nodeForCapture;
	private EditMode mode;
	
	private JLabel labelName;
	private TextFieldComponent fieldName;
	private JLabel labelDetails;
	private TextAreaComponent fieldDetails;
	private JLabel labelWebDriverPath;
	private FileSelectorComponent fieldWebDriverPath;
	
	//Itt biztos beszuras van
	public DriverExplorerEditor( Tree tree, DriverFolderDataModel selectedNode ){

		super( DriverExplorerDataModel.getModelNameToShowStatic());
		
		this.tree = tree;
		this.nodeForCapture = selectedNode;
		this.mode = null;
		
		//Name
		fieldName = new TextFieldComponent( "" );
		
		//Details
		fieldDetails = new TextAreaComponent( "", NOTE_ROWS, 15);
		
		//Web Driver Path
		fieldWebDriverPath = new FileSelectorComponent( fileExtention );

		common();
		
	}
	
	//Itt lehet hogy modositas vagy megtekintes van
	public DriverExplorerEditor( Tree tree, DriverExplorerDataModel selectedNode, EditMode mode ){

		super( mode, selectedNode.getNodeTypeToShow());

		this.tree = tree;
		this.nodeForModify = selectedNode;
		this.mode = mode;
		
		//Name		
		fieldName = new TextFieldComponent( selectedNode.getName());
			
		//Details
		fieldDetails = new TextAreaComponent( selectedNode.getDetails(), NOTE_ROWS, 15);
		
		//Web Driver Path
		fieldWebDriverPath = new FileSelectorComponent( fileExtention, selectedNode.getWebDriverPath() );
		
		common();
		
	}
	
	private void common(){
		labelName = new JLabel( CommonOperations.getTranslation("editor.label.name") + ": ");
		labelDetails = new JLabel( CommonOperations.getTranslation("editor.label.details") + ": ");
		labelWebDriverPath = new JLabel( CommonOperations.getTranslation("editor.label.webdrivepath") + ": ");
		
		this.add( labelName, fieldName );
		this.add( labelDetails, fieldDetails );
		this.add( labelWebDriverPath, fieldWebDriverPath );
	}
	
	
	@Override
	public void save() {
		
		//Ertekek trimmelese
		fieldName.setText( fieldName.getText().trim() );
		fieldDetails.setText( fieldDetails.getText().trim() );
		
		//
		//Hibak eseten a hibas mezok osszegyujtese
		//
		LinkedHashMap<Component, String> errorList = new LinkedHashMap<Component, String>();		
		if( fieldName.getText().length() == 0 ){
			errorList.put( 
					fieldName,
					MessageFormat.format(
							CommonOperations.getTranslation("editor.errormessage.emptyfield"), 
							"'"+labelName.getText()+"'"
					)
			);
		
		}else if( null == fieldWebDriverPath.getSelectedFile() ){	
			errorList.put( 
					fieldWebDriverPath,
					MessageFormat.format(
							CommonOperations.getTranslation("editor.errormessage.emptyfield"), 
							"'"+labelWebDriverPath.getText()+"'"
					)
			);			
			
		}else{

			TreeNode nodeForSearch = null;

			//CAPTURE
			if( null == mode){
				
				nodeForSearch = nodeForCapture;
				
			//MODIFY
			}else if( mode.equals( EditMode.MODIFY )){
				
				nodeForSearch = nodeForModify.getParent();
				
			}
			
			//Megnezi, hogy van-e masik azonos nevu elem
			int childrenCount = nodeForSearch.getChildCount();
			for( int i = 0; i < childrenCount; i++ ){
				TreeNode levelNode = nodeForSearch.getChildAt( i );
				
				//Ha Page-rol van szo (Lehetne meg NODE is)
				if( levelNode instanceof DriverExplorerDataModel ){
					
					//Ha azonos a nev
					if( ((DriverExplorerDataModel) levelNode).getName().equals( fieldName.getText() ) ){
					
						//Ha rogzites van, vagy ha modositas, de a vizsgalt node kulonbozik a modositott-tol
						if( null == mode || ( mode.equals( EditMode.MODIFY ) && !levelNode.equals(nodeForModify) ) ){
										
							errorList.put( 
								fieldName, 
								MessageFormat.format( 
										CommonOperations.getTranslation("editor.errormessage.duplicateelement"), 
										fieldName.getText(), 
										CommonOperations.getTranslation("tree.nodetype.driver.firefox") 
								) 
							);
							break;
						}
					}
				}
			}
		}
		
		//Volt hiba
		if( errorList.size() != 0 ){
			
			//Hibajelzes
			this.errorAt( errorList );
		
		//Ha nem volt hiba akkor a valtozok veglegesitese
		}else{

			//Uj rogzites eseten
			if( null == mode ){
			
				DriverExplorerDataModel newExplorerDataModel = new DriverExplorerDataModel( fieldName.getText(), fieldDetails.getText(), fieldWebDriverPath.getSelectedFile() );				
				nodeForCapture.add( newExplorerDataModel );
				
				//A fa-ban modositja a strukturat
				//tree.refreshTreeAfterStructureChanged( nodeForCapture, nodeForCapture );
				tree.refreshTreeAfterStructureChanged( nodeForCapture );
				
			//Modositas eseten
			}else if( mode.equals(EditMode.MODIFY ) ){
				
				nodeForModify.setName( fieldName.getText() );
				nodeForModify.setDetails( fieldDetails.getText() );
				nodeForModify.setWebDriverFile( fieldWebDriverPath.getSelectedFile() );
			
				//A fa-ban modositja a nevet (ha az valtozott)
				//tree.refreshTreeAfterChanged( nodeForModify );
				tree.refreshTreeAfterChanged();
			}
		}
	}
}
