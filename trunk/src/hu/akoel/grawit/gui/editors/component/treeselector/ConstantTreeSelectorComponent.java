package hu.akoel.grawit.gui.editors.component.treeselector;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.DataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.ConstantDataModelAdapter;
import hu.akoel.grawit.core.treenodedatamodel.constant.ConstantElementDataModel;
import hu.akoel.grawit.core.treenodedatamodel.constant.ConstantFolderNodeDataModel;
import hu.akoel.grawit.core.treenodedatamodel.constant.ConstantRootDataModel;

import javax.swing.ImageIcon;
import javax.swing.tree.TreePath;

public class ConstantTreeSelectorComponent extends TreeSelectorComponent<ConstantElementDataModel>{

	private static final long serialVersionUID = 5692189257383238770L;

	public ConstantTreeSelectorComponent( ConstantDataModelAdapter rootDataModel ) {
		super(CommonOperations.getTranslation("window.title.selector.constant"), ConstantElementDataModel.class, rootDataModel, null, false, false );
	}

	public ConstantTreeSelectorComponent( ConstantDataModelAdapter rootDataModel, ConstantElementDataModel selectedVariableElementDataModel ) {
		super(CommonOperations.getTranslation("window.title.selector.constant"), ConstantElementDataModel.class, rootDataModel, selectedVariableElementDataModel, true, false );
	}
	
	@Override
	public String getSelectedDataModelToString( ConstantElementDataModel selectedDataModel) {
		StringBuffer out = new StringBuffer();
		out.append( selectedDataModel.getName() );
		return out.toString();
	}

	@Override
	public boolean needToExpand(TreePath path, boolean state) {
		return true;
		//return !( path.getLastPathComponent() instanceof VariableElementDataModel );
	}
	
	@Override
	public ImageIcon getIcon(DataModelAdapter actualNode, boolean expanded ) {
		//ImageIcon pageIcon = CommonOperations.createImageIcon("tree/variable-page-icon.png");
		ImageIcon elementIcon = CommonOperations.createImageIcon("tree/variable-element-icon.png");
		ImageIcon nodeClosedIcon = CommonOperations.createImageIcon("tree/variable-node-closed-icon.png");
		ImageIcon nodeOpenIcon = CommonOperations.createImageIcon("tree/variable-node-open-icon.png");
		ImageIcon rootIcon = CommonOperations.createImageIcon("tree/root-icon.png");

		//Iconja a NODE-nak
		if( actualNode instanceof ConstantRootDataModel){
            return rootIcon;
		}else if( actualNode instanceof ConstantElementDataModel ){
			return (elementIcon);
		}else if( actualNode instanceof ConstantFolderNodeDataModel){
			if( expanded ){
				return (nodeOpenIcon);
			}else{
				return (nodeClosedIcon);
			}
		}
		return null;
	}
	
}
