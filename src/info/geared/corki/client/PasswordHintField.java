package info.geared.corki.client;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PasswordHintField extends JPasswordField implements FocusListener
{

	private static final long serialVersionUID = 1L;
	private final String hint;
	private boolean showingHint;

	public PasswordHintField(final String hint)
	{
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		this.setHorizontalAlignment(JTextField.LEADING);
		this.setForeground(new Color(100, 100, 100));
		super.addFocusListener(this);
	}

	public void focusGained(FocusEvent e)
	{
		this.selectAll();
		if (this.getPassword().length == 0)
		{
			super.setText("");
			showingHint = false;
			this.setHorizontalAlignment(JTextField.LEADING);
			this.setForeground(Color.BLACK);
		}
	}

	public void focusLost(FocusEvent e)
	{
		if (this.getPassword().length == 0)
		{
			super.setText(hint);
			showingHint = true;
			this.setHorizontalAlignment(JTextField.LEADING);
			this.setForeground(new Color(100, 100, 100));
		}
	}

	public char[] getPassword()
	{
		if (showingHint == true)
			return new char[0];
		else
		{
			return super.getPassword();
		}
	}

}
