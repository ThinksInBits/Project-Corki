package info.geared.corki.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;
import javax.swing.JScrollPane;

public class ClientFrame extends JFrame implements ChatSessionListener, KeyListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	protected ChatSession session;
	protected JPanel userListPanel;
	protected ArrayList<UserLabel> userLabels;
	protected JTextArea messageHistory;
	protected JTextField messageTextField;
	protected JButton sendButton;
	protected JPanel messagePanel;
	protected JLabel userLabel;
	private JScrollPane scrollPane;
	
	/* All JComponents fields here. */
	
	

	ClientFrame(ChatSession session)
	{
		userLabels = new ArrayList<UserLabel>();
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
		messageHistory.setEditable(false);
		scrollPane = new JScrollPane(messageHistory);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		userListPanel = new JPanel();
		userListPanel.setPreferredSize(new Dimension(90, 10));
		getContentPane().add(userListPanel, BorderLayout.EAST);
		userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));
		
		userLabel = new JLabel("Users");
		userLabel.setPreferredSize(new Dimension(90, 20));
		userLabel.setVerticalTextPosition(SwingConstants.TOP);
		userLabel.setMaximumSize(new Dimension(90, 20));
		userLabel.setMinimumSize(new Dimension(90, 20));
		userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
		userLabel.setVerticalAlignment(SwingConstants.TOP);
		userLabel.setHorizontalAlignment(SwingConstants.CENTER);
		userLabel.setBorder(new EmptyBorder(3,3,3,5));
		userListPanel.add(userLabel);
		JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
		separator.setMaximumSize(new Dimension(32767, 2));
		separator.setAlignmentY(Component.TOP_ALIGNMENT);
		userListPanel.add(separator);
		
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
	
	protected void console(String s)
	{
		messageHistory.append(s + "\r\n");
		messageHistory.setCaretPosition(messageHistory.getText().length());
	}

	/** This method is called by the session when there is a new chat
	 * message has been received. It will in turn get the new message(s)
	 * from the session and put them into the history TextArea.
	 */
	public void update(String msg)
	{
		if (msg.startsWith("CUL:"))
		{
			String[] users = msg.substring(4).split("\\|");
			
			userListPanel.removeAll();
			
			userListPanel.add(userLabel);
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
			separator.setMaximumSize(new Dimension(32767, 2));
			separator.setAlignmentY(Component.TOP_ALIGNMENT);
			userListPanel.add(separator);
			userLabels.clear();
			
			for (int i = 0; i < users.length; i++)
			{
				UserLabel l = new UserLabel(users[i]);
				userLabels.add(l);
				l.setBorder(new EmptyBorder(3,3,3,5));
				l.setAlignmentY(Component.TOP_ALIGNMENT);
				userListPanel.add(l);
			}
			userListPanel.repaint();
			validate();
		}
		else if (msg.startsWith("CON:"))
		{
			UserLabel l = new UserLabel(msg.substring(4));
			if (userLabels.contains(l))
			{
				l = null;
				return;
			}
			else
			{
				l.setBorder(new EmptyBorder(3,3,3,5));
				l.setAlignmentY(Component.TOP_ALIGNMENT);
				userLabels.add(l);
				userListPanel.add(l);
				userListPanel.repaint();
				validate();
				console(l.getText() + " has connected.");
			}
		}
		else if (msg.startsWith("DIS:"))
		{
			String name = msg.substring(4);
			for (UserLabel l : userLabels)
			{
				if (l.getText().equals(name))
				{
					userListPanel.remove(l);
					userLabels.remove(l);
					userListPanel.repaint();
					validate();
					console(name + " has disconnected.");
					break;
				}
			}
		}
		else if (msg.startsWith("MSG:"))
		{
			console(msg.substring(4));
		}
		else
		{
			console(msg);
		}
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
