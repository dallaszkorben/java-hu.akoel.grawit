package hu.akoel.grawit.gui.editors.component.constantparameter;

import hu.akoel.grawit.CommonOperations;
import hu.akoel.grawit.enums.list.ConstantTypeListEnum;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConstantStringComponent extends JPanel implements ConstantComponentInterface{
	
	private static final long serialVersionUID = -5111211582850994473L;
	
	private static final String DEFAULT_VALUE = "";
	
	private static final int PARAMETERORDER_VALUE = 0;
	
	private JTextField fieldString;
	
	private ArrayList<Object> parameterList;

	/**
	 * Uj lista
	 * 
	 * @param type
	 */
	public ConstantStringComponent( ConstantTypeListEnum type ){
		super();

		//parameter lista letrehozasa es feltoltese default ertekekkel
		this.parameterList = new ArrayList<>();
		this.parameterList.add( DEFAULT_VALUE );
		
		common( type );
		
		
	}
	
	/**
	 * Letezo lista
	 * 
	 * @param type
	 * @param parameterList
	 */
	public ConstantStringComponent( ConstantTypeListEnum type, ArrayList<Object> parameterList ){
		super();
		
		//Parameter lista feltoltese a letezo ertekekkel
		this.parameterList = parameterList;
		
		common( type );
		
		
	}
	
	private void common( ConstantTypeListEnum type ){
		
		this.setLayout( new GridBagLayout() );
		
		//Mezo feltoltese
		fieldString = new JTextField( parameterList.get(PARAMETERORDER_VALUE).toString());
		fieldString.setInputVerifier( new CommonOperations.ValueVerifier(parameterList, type, DEFAULT_VALUE, PARAMETERORDER_VALUE) );
		
		int gridY = 0;
		GridBagConstraints c = new GridBagConstraints();		
		c.insets = new Insets(0,0,0,0);
	
		gridY++;
		c.gridy = gridY;
		c.gridx = 0;
		c.gridwidth = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.anchor = GridBagConstraints.WEST;
		this.add( fieldString, c );
	}	
	
	@Override
	public void setEnableModify(boolean enable) {
		fieldString.setEditable( enable );		
	}

	@Override
	public Component getComponent() {
		return this;
	}

	@Override
	public ArrayList<Object> getParameters() {
		return parameterList;
	}

}
