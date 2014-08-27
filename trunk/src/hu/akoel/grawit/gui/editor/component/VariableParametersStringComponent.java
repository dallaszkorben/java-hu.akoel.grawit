package hu.akoel.grawit.gui.editor.component;

import hu.akoel.grawit.enums.VariableType;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VariableParametersStringComponent extends JPanel implements VariableParametersComponentInterface{
	
	private static final long serialVersionUID = -5111211582850994473L;
	
	private static final String DEFALULT_SAMPLE = "";
	
	private JTextField fieldString;
	private VariableType type;
	
	private ArrayList<Object> parameterList;

	/**
	 * Uj lista
	 * 
	 * @param type
	 */
	public VariableParametersStringComponent( VariableType type ){
		super();

		//parameter lista letrehozasa es feltoltese default ertekekkel
		this.parameterList = new ArrayList<>();
		this.parameterList.add( DEFALULT_SAMPLE );
		
		common( type );
		
		
	}
	
	/**
	 * Letezo lista
	 * 
	 * @param type
	 * @param parameterList
	 */
	public VariableParametersStringComponent( VariableType type, ArrayList<Object> parameterList ){
		super();
		
		//Parameter lista feltoltese a letezo ertekekkel
		this.parameterList = parameterList;
		
		common( type );
		
		
	}
	
	private void common( VariableType type ){
		this.type = type;
		
		this.setLayout( new GridBagLayout() );
		
//		JLabel labelString = new JLabel( CommonOperations.getTranslation("editor.title.variabletype.string.string") );
		
		//Mezo feltoltese
		fieldString = new JTextField( parameterList.get(0).toString());
		
		fieldString.setInputVerifier(new InputVerifier() {
			String goodValue = "";
			
			@Override
			public boolean verify(JComponent input) {
				JTextField text = (JTextField)input;
				String possibleValue = text.getText();

				try {
					//Kiprobalja, hogy konvertalhato-e
					Object value = VariableParametersStringComponent.this.type.getParameterClass(0).getConstructor(String.class).newInstance(possibleValue);
					parameterList.set( 0, value );
					goodValue = possibleValue;
					
				} catch (Exception e) {
					text.setText( goodValue );
					return false;
				}				
				return true;
			}
		});
		
		int gridY = 0;
		GridBagConstraints c = new GridBagConstraints();		
		c.insets = new Insets(0,0,0,0);
/*		
		c.gridy = gridY;
		c.gridx = 0;
		c.gridwidth = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		c.anchor = GridBagConstraints.WEST;
		this.add( labelString, c );
*/		
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
