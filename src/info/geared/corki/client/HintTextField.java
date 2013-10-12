package info.geared.corki.client;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

class HintTextField extends JTextField implements FocusListener
{

	private static final long serialVersionUID = 1L;
	private final String hint;
	private boolean showingHint;

	public HintTextField(final String hint)
	{
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		this.setHorizontalAlignment(JTextField.CENTER);
		this.setForeground(new Color(100, 100, 100));
		super.addFocusListener(this);
	}

	public void focusGained(FocusEvent e)
	{
		if (this.getText().isEmpty())
		{
			super.setText("");
			showingHint = false;
			this.setHorizontalAlignment(JTextField.LEADING);
			this.setForeground(Color.BLACK);
		}
	}

	public void focusLost(FocusEvent e)
	{
		if (this.getText().isEmpty())
		{
			super.setText(hint);
			showingHint = true;
			this.setHorizontalAlignment(JTextField.CENTER);
			this.setForeground(new Color(100, 100, 100));
		}
	}

	public String getText()
	{
		if (showingHint == true)
			return "";
		else
		{
			return super.getText();
		}
	}
}