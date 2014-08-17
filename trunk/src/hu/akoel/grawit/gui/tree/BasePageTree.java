package hu.akoel.grawit.gui.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import hu.akoel.grawit.ActionCommand;
import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.gui.GUIFrame;
import hu.akoel.grawit.gui.editor.DataEditor;
import hu.akoel.grawit.gui.editor.EmptyEditor;
import hu.akoel.grawit.gui.editor.BasePageElementEditor;
import hu.akoel.grawit.gui.editor.BasePageNodeEditor;
import hu.akoel.grawit.gui.editor.BasePagePageEditor;
import hu.akoel.grawit.gui.editor.DataEditor.EditMode;
import hu.akoel.grawit.gui.tree.datamodel.BasePageDataModelInterface;
import hu.akoel.grawit.gui.tree.datamodel.BasePageElementDataModel;
import hu.akoel.grawit.gui.tree.datamodel.BasePageNodeDataModel;
import hu.akoel.grawit.gui.tree.datamodel.BasePagePageDataModel;
import hu.akoel.grawit.gui.tree.datamodel.BasePageRootDataModel;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class BasePageTree extends JTree{

	private static final long serialVersionUID = -3929758449314068678L;
	
	private GUIFrame guiFrame;
	
	private DefaultMutableTreeNode selectedNode;

//	private DefaultTreeModel treeModel;
	/**
	 * 
	 * Ertesiti a tree-t, hogy valtozas tortent
	 * 
	 */
	public void changed(){

		TreePath path = new TreePath(selectedNode.getPath());
		boolean isExpanded = this.isExpanded( path );
		
		//Ujratolti a modellt
		((DefaultTreeModel)this.getModel()).reload();

		//this.setSelectionPath(pathToSelect);

		//Kivalasztja azt a csomopontot ami eleve ki volt valasztva
		this.setSelectionPath( path );
		
		//Es ha ki volt terjesztve
		if( isExpanded ){
			
			//Akkor kiterjeszti
			this.expandPath( path );
		}
		
	}
	
	public BasePageTree( GUIFrame guiFrame, BasePageRootDataModel basePageRootDataModel ){
	
		super( new DefaultTreeModel(basePageRootDataModel) );
		
		treeModel = (DefaultTreeModel)this.getModel();
		
		this.guiFrame = guiFrame;
		this.setShowsRootHandles(true);
		//this.setRootVisible(false);
		
		//Csak egy elem lehet kivalasztva
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		
		/**
		 * Ikonokat helyezek el az egyes csomopontok ele
		 */
		this.setCellRenderer(new DefaultTreeCellRenderer() {
		    //private Icon loadIcon = UIManager.getIcon("OptionPane.errorIcon");
		    //private Icon saveIcon = UIManager.getIcon("OptionPane.informationIcon");

			private static final long serialVersionUID = 1323618892737458100L;

			@Override
		    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused) {
		    	Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);
		    	
		    	ImageIcon pageIcon = CommonOperations.createImageIcon("tree/pagebase-page-icon.png");
		    	ImageIcon elementIcon = CommonOperations.createImageIcon("tree/pagebase-element-icon.png");
		    	ImageIcon nodeClosedIcon = CommonOperations.createImageIcon("tree/node-closed-icon.png");
		    	ImageIcon nodeOpenIcon = CommonOperations.createImageIcon("tree/node-open-icon.png");
		    	
		    	//Felirata a NODE-nak
		    	setText( ((BasePageDataModelInterface)value).getNameToString() );
		    	
		    	//Iconja a NODE-nak
		    	if( value instanceof BasePagePageDataModel){
		            setIcon(pageIcon);
		    	}else if( value instanceof BasePageElementDataModel ){
		            setIcon(elementIcon);
		    	}else if( value instanceof BasePageNodeDataModel){
		    		if( expanded ){
		    			setIcon(nodeOpenIcon);
		    		}else{
		    			setIcon(nodeClosedIcon);
		    		}
		        }
		 
		    	return c;
		    }
		});
		

		
		/**
		 * A eger benyomasara reagalok
		 */
		this.addMouseListener( new TreeMouseListener() );
		this.addTreeSelectionListener( new SelectionChangedListener() );
	
	}
	
	/**
	 * Azt figyeli, hogy barmi miatt megvaltozott-e a kivalasztott elem.
	 * Ez lehet eger vagy billentyu, vagy akar maga a program
	 * 
	 * @author afoldvarszky
	 *
	 */
	class SelectionChangedListener implements TreeSelectionListener{

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			
			//System.err.println("changed to: " + e.getNewLeadSelectionPath());
			//A kivalasztott NODE			
			
			//Nincs kivalasztva semmi
			if( null == e.getNewLeadSelectionPath() ){
				EmptyEditor emptyPanel = new EmptyEditor();								
				guiFrame.showEditorPanel( emptyPanel);
			}else{
			
				selectedNode = (DefaultMutableTreeNode)e.getNewLeadSelectionPath().getLastPathComponent();
				//selectedNode = (DefaultMutableTreeNode)BasePageTree.this.getLastSelectedPathComponent();
			
				//Ha egyaltalan valamilyen egergombot benyomtam
				if( selectedNode instanceof BasePageRootDataModel ){
					EmptyEditor emptyPanel = new EmptyEditor();								
					guiFrame.showEditorPanel( emptyPanel );
				
				}else if( selectedNode instanceof BasePageNodeDataModel ){
					BasePageNodeEditor pageBaseNodePanel = new BasePageNodeEditor(BasePageTree.this, (BasePageNodeDataModel)selectedNode, EditMode.VIEW);
					guiFrame.showEditorPanel( pageBaseNodePanel);								
				
				}else if( selectedNode instanceof BasePagePageDataModel ){
					BasePagePageEditor pageBasePagePanel = new BasePagePageEditor( BasePageTree.this, (BasePagePageDataModel)selectedNode, EditMode.VIEW );								
					guiFrame.showEditorPanel( pageBasePagePanel);				
								
				}else if( selectedNode instanceof BasePageElementDataModel ){
					BasePageElementEditor pageBaseElementPanel = new BasePageElementEditor( BasePageTree.this, (BasePageElementDataModel)selectedNode, EditMode.VIEW );								
					guiFrame.showEditorPanel( pageBaseElementPanel);		
										
				}
			}
		}		
	}
		
	
	/**
	 * A jobb-eger gomb benyomasara reagalo osztaly
	 * 
	 * @author akoel
	 *
	 */
	class TreeMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			//A kivalasztott NODE			
			selectedNode = (DefaultMutableTreeNode)BasePageTree.this.getLastSelectedPathComponent();
		
			//Ha jobb-eger gombot nyomtam - Akkor popup menu jelenik meg
			if (SwingUtilities.isRightMouseButton(e)) {

				//A kivalasztott elem sora - kell a sor kiszinezesehez es a PopUp menu poziciojahoz
				int row = BasePageTree.this.getClosestRowForLocation(e.getX(), e.getY());
				//int row = BasePageTree.this.getRowForLocation(e.getX(), e.getY());
				
				//Kiszinezi a sort
				BasePageTree.this.setSelectionRow(row);

				//Jelzi, hogy mostantol, hiaba nem bal-egerrel valasztottam ki a node-ot, megis kivalasztott lesz
				selectedNode = (DefaultMutableTreeNode)BasePageTree.this.getLastSelectedPathComponent();

				//Letrehozza a PopUpMenu-t
				//PopUpMenu popUpMenu = new PopUpMenu( row, node, path );
				PopUpMenu popUpMenu = new PopUpMenu( row );
				
				//Megjeleniti a popup menut
				popUpMenu.show( e.getComponent(), e.getX(), e.getY() );
				
			}		
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}	 
	}
	
	
	/**
	 * A jobb-eger gomb hatasara megjeleno menu
	 * 
	 * @author akoel
	 *
	 */
	class PopUpMenu extends JPopupMenu{
		
		private static final long serialVersionUID = -2476473336416059356L;

		private DefaultMutableTreeNode parentNode;
		private BasePageDataModelInterface selectedNode;
		private TreePath selectedPath;
		private DefaultTreeModel totalTreeModel;
		private int selectedIndexInTheNode;
		private int elementsInTheNode;
		
		//public PopUpMenu( final int selectedRow, final DefaultMutableTreeNode selectedNode, final TreePath selectedPath ){
		public PopUpMenu( final int selectedRow ){
			super();

			//A teljes fastruktura modell-je			
			totalTreeModel = (DefaultTreeModel)BasePageTree.this.getModel();

			//A kivalasztott NODE			
			selectedNode = (BasePageDataModelInterface)BasePageTree.this.getLastSelectedPathComponent();

			//A kivalasztott node-ig vezeto PATH
			selectedPath = BasePageTree.this.getSelectionPath();	

			//A kivalasztott node fastrukturaban elfoglalt melyseget adja vissza. 1 jelenti a gyokeret
			int pathLevel = selectedPath.getPathCount();

			//Jelzem, hogy nincs kivalasztva meg
			selectedIndexInTheNode = -1;

			//Ha NEM ROOT volt kivalasztva, akkor a FEL es LE elemeket helyezem el
			if( pathLevel > 1 ){

				//A kivalasztott node szulo node-ja
				parentNode = (DefaultMutableTreeNode)selectedPath.getPathComponent( pathLevel - 2 );

				//Hany elem van a szulo csomopontjaban
				elementsInTheNode = totalTreeModel.getChildCount( parentNode );
				
				//A kivalasztott elem sorszama a szulo node-jaban
				selectedIndexInTheNode = totalTreeModel.getIndexOfChild( parentNode, selectedNode );
				
				//
				// Felfele mozgat
				// Ha nem a legelso elem
				//
				if( selectedIndexInTheNode >= 1 ){
					
					//Akkor mozoghat felfele, letrehozhatom a fel menuelemet
					JMenuItem upMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.up") );
					upMenu.setActionCommand( ActionCommand.UP.name());
					upMenu.addActionListener( new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							if( selectedIndexInTheNode >= 1 ) {
								
								//Torolni kell a fastrukturabol az eredeti sort
								totalTreeModel.removeNodeFromParent(selectedNode);
								
								//Majd el kell helyezni eggyel feljebb
								totalTreeModel.insertNodeInto(selectedNode, (DefaultMutableTreeNode)parentNode, selectedIndexInTheNode - 1);    // move the node
								
								//Ujra ki kell szinezni az eredetileg kivalasztott sort
								BasePageTree.this.setSelectionRow(selectedRow - 1);
							}							
						}
					});
					this.add ( upMenu );

					
				}
				
				//
				// Lefele mozgat
				// Ha nem a legutolso elem
				//
				if( selectedIndexInTheNode < elementsInTheNode - 1 ){
					
					//Akkor mozoghat lefele, letrehozhatom a le nemuelement
					JMenuItem downMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.down")  );
					downMenu.setActionCommand( ActionCommand.DOWN.name() );
					downMenu.addActionListener( new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							if(selectedIndexInTheNode < elementsInTheNode - 1 ) {
								
								//Torolni kell a fastrukturabol az eredeti sort
								totalTreeModel.removeNodeFromParent(selectedNode);
								
								//Majd el kell helyezni eggyel lejebb
								totalTreeModel.insertNodeInto(selectedNode, (DefaultMutableTreeNode)parentNode, selectedIndexInTheNode + 1);    
								
								//Ujra ki kell szinezni az eredetileg kivalasztott sort
								BasePageTree.this.setSelectionRow(selectedRow + 1);
							}	
							
						}
					});
					this.add ( downMenu );
					
				}
				
				//
				//Szerkesztes
				//
				JMenuItem editMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.edit") );
				editMenu.setActionCommand( ActionCommand.EDIT.name());
				editMenu.addActionListener( new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
//						if( selectedIndexInTheNode >= 1 ) {
							
							if( selectedNode instanceof BasePageNodeDataModel ){
							
								BasePageNodeEditor pageBaseNodePanel = new BasePageNodeEditor( BasePageTree.this, (BasePageNodeDataModel)selectedNode, DataEditor.EditMode.MODIFY );								
								guiFrame.showEditorPanel( pageBaseNodePanel);								
								
							}else if( selectedNode instanceof BasePagePageDataModel ){
								
								BasePagePageEditor pageBasePagePanel = new BasePagePageEditor( BasePageTree.this, (BasePagePageDataModel)selectedNode, DataEditor.EditMode.MODIFY );								
								guiFrame.showEditorPanel( pageBasePagePanel);		
								
							}else if( selectedNode instanceof BasePageElementDataModel ){

								BasePageElementEditor pageBaseElementPanel = new BasePageElementEditor( BasePageTree.this, (BasePageElementDataModel)selectedNode, DataEditor.EditMode.MODIFY );								
								guiFrame.showEditorPanel( pageBaseElementPanel);		
								
							}
					}
				});
				this.add ( editMenu );
				
				//
				// Csomopont eseten
				//
				if( selectedNode instanceof BasePageNodeDataModel ){

					//Insert Node
					JMenuItem insertNodeMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.insert.node") );
					insertNodeMenu.setActionCommand( ActionCommand.CAPTURE.name());
					insertNodeMenu.addActionListener( new ActionListener() {
					
						@Override
						public void actionPerformed(ActionEvent e) {
							
							BasePageNodeEditor pageBaseNodePanel = new BasePageNodeEditor( BasePageTree.this, (BasePageNodeDataModel)selectedNode );								
							guiFrame.showEditorPanel( pageBaseNodePanel);								
						
						}
					});
					this.add ( insertNodeMenu );

					//Insert Page
					JMenuItem insertPageMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.insert.page") );
					insertPageMenu.setActionCommand( ActionCommand.CAPTURE.name());
					insertPageMenu.addActionListener( new ActionListener() {
					
						@Override
						public void actionPerformed(ActionEvent e) {
							
							BasePagePageEditor pageBaseNodePanel = new BasePagePageEditor( BasePageTree.this, (BasePageNodeDataModel)selectedNode );								
							guiFrame.showEditorPanel( pageBaseNodePanel);								
						
						}
					});
					this.add ( insertPageMenu );
					
				}		
				
				//
				// Page eseten
				//
				if( selectedNode instanceof BasePagePageDataModel ){

					//Insert Element
					JMenuItem insertElementMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.insert.element") );
					insertElementMenu.setActionCommand( ActionCommand.CAPTURE.name());
					insertElementMenu.addActionListener( new ActionListener() {
					
						@Override
						public void actionPerformed(ActionEvent e) {
							
							BasePageElementEditor pageBaseNodePanel = new BasePageElementEditor( BasePageTree.this, (BasePagePageDataModel)selectedNode );								
							guiFrame.showEditorPanel( pageBaseNodePanel);								
						
						}
					});
					this.add ( insertElementMenu );
				
				}
				
				//
				// Torles
				// Ha nincs alatta ujabb elem
				//
				if( selectedNode.getChildCount() == 0 ){
					
				
					JMenuItem deleteMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.delete") );
					deleteMenu.setActionCommand( ActionCommand.UP.name());
					deleteMenu.addActionListener( new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {

							//Megerosito kerdes
							Object[] options = {
									CommonOperations.getTranslation("button.no"),
									CommonOperations.getTranslation("button.yes")								
							};
							
							int n = JOptionPane.showOptionDialog(guiFrame,
									"Valóban torolni kívánod a(z) " + selectedNode.getNameToString() + " nevü " + selectedNode.getTypeToString() + "-t ?",
									CommonOperations.getTranslation("editor.windowtitle.confirmation.delete"),
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									null,
									options,
									options[0]);

							if( n == 1 ){
								totalTreeModel.removeNodeFromParent( selectedNode);
								BasePageTree.this.setSelectionRow(selectedRow - 1);
							}							
						}
					});
					this.add ( deleteMenu );
					
				}
				
			//ROOT volt kivalasztva
			}else{
				
				//Insert Node
				JMenuItem insertNodeMenu = new JMenuItem( CommonOperations.getTranslation( "popupmenu.insert.node") );
				insertNodeMenu.setActionCommand( ActionCommand.CAPTURE.name());
				insertNodeMenu.addActionListener( new ActionListener() {
				
					@Override
					public void actionPerformed(ActionEvent e) {
						
						BasePageNodeEditor pageBaseNodePanel = new BasePageNodeEditor( BasePageTree.this, (BasePageNodeDataModel)selectedNode );								
						guiFrame.showEditorPanel( pageBaseNodePanel);								
					
					}
				});
				this.add ( insertNodeMenu );
				
			}

		}
	
	}
}