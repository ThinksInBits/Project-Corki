package info.geared.corki.client;

import javax.swing.JLabel;

public class UserLabel extends JLabel
{
	public UserLabel(String name)
	{
		super(name);
	}
	
	public boolean equals(JLabel rhs)
	{
		if (this.getText().equals(rhs.getText()))
			return true;
		else
			return false;
	}
	
	public boolean equals(String name)
	{
		if (this.getText().equals(name))
			return true;
		else
			return false;
	}
}
