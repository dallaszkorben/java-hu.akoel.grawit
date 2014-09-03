package hu.akoel.grawit.gui.editors.component.selector;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.core.treenodedatamodel.DataModelInterface;
import hu.akoel.grawit.core.treenodedatamodel.SpecialDataModelInterface;
import hu.akoel.grawit.core.treenodedatamodel.special.SpecialCloseDataModel;
import hu.akoel.grawit.core.treenodedatamodel.special.SpecialNodeDataModel;
import hu.akoel.grawit.core.treenodedatamodel.special.SpecialOpenDataModel;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class SpecialCloseTreeSelectorComponent extends TreeSelectorComponent<SpecialCloseDataModel>{

	private static final long serialVersionUID = -3698310168899684818L;

	public SpecialCloseTreeSelectorComponent( SpecialDataModelInterface rootDataModel ) {
		super(SpecialCloseDataModel.class, rootDataModel);
	}

	public SpecialCloseTreeSelectorComponent( SpecialDataModelInterface rootDataModel, SpecialCloseDataModel selectedCloseDataModel ) {
		super(SpecialCloseDataModel.class, rootDataModel, selectedCloseDataModel);
	}
	
	@Override
	public String getSelectedDataModelToString( SpecialCloseDataModel selectedDataModel ) {
		StringBuffer out = new StringBuffer();
		boolean hasHyphen = false;
		for( TreeNode node: selectedDataModel.getPath() ){
			
			DataModelInterface dataModel = (DataModelInterface)node;

			if( !dataModel.isRoot() ){
				if( !hasHyphen ){
					hasHyphen = true;
				}else{
					out.append("-");
				}
				out.append( dataModel.getName() );
			}			
		}
		return out.toString();
	}

	@Override
	public boolean needToExpand(TreePath path, boolean state) {
		return true;
		//return !( path.getLastPathComponent() instanceof SpecialDataModelInterface );
	}
	
	@Override
	public ImageIcon getIcon(DataModelInterface actualNode, boolean expanded ) {
	
		ImageIcon closeIcon = CommonOperations.createImageIcon("tree/special-close-icon.png");
		ImageIcon openIcon = CommonOperations.createImageIcon("tree/special-open-icon.png");
    	ImageIcon nodeClosedIcon = CommonOperations.createImageIcon("tree/special-node-closed-icon.png");
    	ImageIcon nodeOpenIcon = CommonOperations.createImageIcon("tree/special-node-open-icon.png");
       	
       	//Iconja a NODE-nak
    	if( actualNode instanceof SpecialOpenDataModel ){
    			return openIcon;
   		}else if( actualNode instanceof SpecialCloseDataModel ){
    			return closeIcon;
    	}else if( actualNode instanceof SpecialOpenDataModel ){
            return openIcon;
    	}else if( actualNode instanceof SpecialNodeDataModel){
    		if( expanded ){
    			return nodeOpenIcon;
    		}else{
    			return nodeClosedIcon;
    		}
        }
    	
		return null;
	}




	
}
