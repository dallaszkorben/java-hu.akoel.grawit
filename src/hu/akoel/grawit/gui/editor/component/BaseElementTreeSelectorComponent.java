package hu.akoel.grawit.gui.editor.component;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.datamodel.BaseDataModelInterface;
import hu.akoel.grawit.core.datamodel.DataModelInterface;
import hu.akoel.grawit.core.datamodel.elements.BaseElementDataModel;
import hu.akoel.grawit.core.datamodel.nodes.BaseNodeDataModel;
import hu.akoel.grawit.core.datamodel.pages.BasePageDataModel;

import javax.swing.ImageIcon;
import javax.swing.tree.TreePath;

public class BaseElementTreeSelectorComponent extends TreeSelectorComponent<BaseElementDataModel>{

	private static final long serialVersionUID = -5178610032767904794L;

	public BaseElementTreeSelectorComponent( BaseDataModelInterface rootDataModel ) {
		super(BaseElementDataModel.class, rootDataModel);
	}

	public BaseElementTreeSelectorComponent( BaseDataModelInterface rootDataModel, BaseElementDataModel selectedBaseElementDataModel ) {
		super(BaseElementDataModel.class, rootDataModel, selectedBaseElementDataModel);
	}
	
	@Override
	public String getSelectedDataModelToString( BaseElementDataModel selectedDataModel) {
		StringBuffer out = new StringBuffer();
		out.append( selectedDataModel.getName() );
		return out.toString();
	}

	@Override
	public boolean needToExpand(TreePath path, boolean state) {
		return true;
	}
	
	@Override
	public ImageIcon getIcon(DataModelInterface actualNode, boolean expanded ) {
		ImageIcon pageIcon = CommonOperations.createImageIcon("tree/pagebase-page-icon.png");
		ImageIcon elementIcon = CommonOperations.createImageIcon("tree/pagebase-element-icon.png");
		ImageIcon nodeClosedIcon = CommonOperations.createImageIcon("tree/node-closed-icon.png");
		ImageIcon nodeOpenIcon = CommonOperations.createImageIcon("tree/node-open-icon.png");

		//Iconja a NODE-nak
		if( actualNode instanceof BasePageDataModel){
			return (pageIcon);
		}else if( actualNode instanceof BaseElementDataModel ){
			return (elementIcon);
		}else if( actualNode instanceof BaseNodeDataModel){
			if( expanded ){
				return (nodeOpenIcon);
			}else{
				return (nodeClosedIcon);
			}
		}
		return null;
	}
	
}
