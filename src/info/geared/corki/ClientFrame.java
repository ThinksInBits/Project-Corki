package info.geared.corki;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class ClientFrame extends JFrame implements ChatSessionListener, KeyListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	private ChatSession session;
	
	/* All JComponents fields here. */

	ClientFrame(ChatSession session)
	{
		buildUI();
	}

	/** This method is called in the constructor and will piece together
	 * all of the components to correctly form the layout.
	 */
	private void buildUI()
	{
		
	}

	/** This method is called by the session when there is a new chat
	 * message has been received. It will in turn get the new message(s)
	 * from the session and put them into the history TextArea.
	 */
	public void update(String msg)
	{
		
	}

	/** This method listens for the user to click the send button.
	 * It will send the message in the input area (if it is not empty)
	 * through the session, and also print it locally to the history
	 * TextArea
	 */
	public void actionPerformed(ActionEvent e)
	{
		
	}

	/** This method listens for the user to press "Enter" to send
	 * a message. It will send the message through the session, and
	 * also print it locally to the history TextArea.
	 */
	public void keyPressed(KeyEvent e)
	{
		
	}

	/* Do nothing. */
	public void keyReleased(KeyEvent e)
	{
	}

	/* Do nothing. */
	public void keyTyped(KeyEvent e)
	{
	}

}
