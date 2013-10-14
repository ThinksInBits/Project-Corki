package info.geared.corki.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class ClientFrame extends JFrame implements ChatSessionListener, KeyListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	protected ChatSession session;
	protected JPanel userListPanel;
	protected JTextArea messageHistory;
	protected JTextField messageTextField;
	protected JButton sendButton;
	protected JPanel messagePanel;
	
	/* All JComponents fields here. */
	
	

	ClientFrame(ChatSession session)
	{
		this.session = session;
		session.addChatSessionListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Corki Chat - " + session.hostname);	
		buildUI();
		messageTextField.requestFocusInWindow();
	}

	/** This method is called in the constructor and will piece together
	 * all of the components to correctly form the layout.
	 */
	private void buildUI()
	{
		setSize(475, 600);
		getContentPane().setLayout(new BorderLayout(2, 2));
		
		messageHistory = new JTextArea();
		getContentPane().add(messageHistory, BorderLayout.CENTER);
		
		userListPanel = new JPanel();
		getContentPane().add(userListPanel, BorderLayout.EAST);
		userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
		
		JLabel label = new JLabel("New label");
		label.setBorder(new EmptyBorder(3,3,3,5));
		userListPanel.add(label);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBorder(new EmptyBorder(3,3,3,5));
		userListPanel.add(lblNewLabel);
		
		messagePanel = new JPanel();
		getContentPane().add(messagePanel, BorderLayout.SOUTH);
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.X_AXIS));
		
		messageTextField = new JTextField();
		messageTextField.addKeyListener(this);
		messageTextField.setFont(new Font("SansSerif", Font.PLAIN, 13));
		messagePanel.add(messageTextField);
		
		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
		messagePanel.add(sendButton);
		
		setVisible(true);
	}
	
	protected void sendMessage()
	{
		if ( ! messageTextField.getText().isEmpty())
			session.send(messageTextField.getText());
		
		messageTextField.setText("");
	}

	/** This method is called by the session when there is a new chat
	 * message has been received. It will in turn get the new message(s)
	 * from the session and put them into the history TextArea.
	 */
	public void update(String msg)
	{
		messageHistory.append(msg + "\r\n");
	}

	/** This method listens for the user to click the send button.
	 * It will send the message in the input area (if it is not empty)
	 * through the session, and also print it locally to the history
	 * TextArea
	 */
	public void actionPerformed(ActionEvent e)
	{
		sendMessage();
	}

	/** This method listens for the user to press "Enter" to send
	 * a message. It will send the message through the session, and
	 * also print it locally to the history TextArea.
	 */
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			sendMessage();
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
