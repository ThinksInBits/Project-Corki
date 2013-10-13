package info.geared.corki.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class LoginFrame extends JFrame implements ActionListener, KeyListener, ChatSessionListener
{
	private static final long serialVersionUID = 1L;

	/* All JComponents fields here. */
	protected JTextField addressField;
	protected JTextField nameField;
	protected JPasswordField passwordField;
	
	protected JLabel addressErrorLabel;
	protected JLabel nameErrorLabel;
	protected JLabel passwordErrorLabel;
	
	protected JButton loginButton;
	
	protected Image icon;
	protected Image corkiChat;
	protected ImageIcon loadingIcon;
	protected JLabel loadingLabel;
	protected JLabel corkiChatLabel;
	
	ChatSession s;

	LoginFrame()
	{
		setTitle("Corki Chat - Login");	
		buildUI();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		loginButton.requestFocusInWindow();
	}

	private void buildUI()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			icon = ImageIO.read(new File("icon.png"));
			corkiChat = ImageIO.read(new File("corkichat.png"));
			//loadingIcon = ImageIO.read(new File("loading.gif"));
			loadingIcon = new ImageIcon(getToolkit().createImage("loading.gif"));
			setIconImage(icon);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("The icon file is missing.");
		}
		catch(Exception e)
		{	
			e.printStackTrace();
			System.out.println("The look and feel could not be set to the native UI");
		}
		
		getContentPane().setLayout(null);
		this.setSize(400, 494);
		setResizable(false);
		getContentPane().setBackground(SystemColor.controlHighlight);

		corkiChatLabel = new JLabel(new ImageIcon(corkiChat));
		corkiChatLabel.setSize(350, 167);
		corkiChatLabel.setLocation(20, 34);
		getContentPane().add(corkiChatLabel);

		addressField = new HintTextField("Server address (geared.info)");
		addressField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		addressField.setSize(275, 23);
		addressField.setLocation(54, 235);
		addressField.addKeyListener(this);
		getContentPane().add(addressField);
		
		addressErrorLabel = new JLabel("Server Error");
		addressErrorLabel.setForeground(new Color(128, 0, 0));
		addressErrorLabel.setVisible(false);
		addressErrorLabel.setSize(265, 20);
		addressErrorLabel.setLocation(64, 259);
		getContentPane().add(addressErrorLabel);
		
		nameField = new HintTextField("Username");
		nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		nameField.setSize(new Dimension(275, 23));
		nameField.setLocation(54, 292);
		nameField.addKeyListener(this);
		getContentPane().add(nameField);
		
		nameErrorLabel = new JLabel("Username Error");
		nameErrorLabel.setForeground(new Color(128, 0, 0));
		nameErrorLabel.setVisible(false);
		nameErrorLabel.setSize(265, 20);
		nameErrorLabel.setLocation(64, 316);
		getContentPane().add(nameErrorLabel);
		
		passwordField = new PasswordHintField("Password");
		passwordField.setFont(new Font("SansSerif", Font.PLAIN, 10));
		passwordField.setSize(275, 23);
		passwordField.setLocation(54, 349);
		passwordField.addKeyListener(this);
		getContentPane().add(passwordField);
		
		passwordErrorLabel = new JLabel("Password Error");
		passwordErrorLabel.setForeground(new Color(128, 0, 0));
		passwordErrorLabel.setVisible(false);
		passwordErrorLabel.setSize(265, 20);
		passwordErrorLabel.setLocation(64, 373);
		getContentPane().add(passwordErrorLabel);
		
		loginButton = new JButton("Login");
		loginButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		loginButton.setSize(85, 23);
		loginButton.setLocation(149, 406);
		loginButton.addActionListener(this);
		getContentPane().add(loginButton);	
		
		JLabel lblCopyRightGearedinfo = new JLabel("\u00A9 Geared Software 2013. All rights reserved.");
		lblCopyRightGearedinfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblCopyRightGearedinfo.setForeground(SystemColor.controlShadow);
		lblCopyRightGearedinfo.setBounds(20, 440, 350, 14);
		getContentPane().add(lblCopyRightGearedinfo);
		
		loadingLabel = new JLabel(loadingIcon);
		loadingLabel.setSize(124, 128);
		loadingLabel.setLocation(134, 337);
		loadingLabel.setVisible(false);
		getContentPane().add(loadingLabel);	
		
		setVisible(true);
	}

	/**
	 * This handler will be called when the user clicks the Login button. The
	 * method will verify the user entered valid information and alert them if
	 * there is a problem. It will attempt to construct a ChatSession, and if it
	 * is successful, then it will construct a ClientFrame and dispose.
	 */
	public void actionPerformed(ActionEvent e)
	{
		attemptConnection();
	}
	
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			attemptConnection();
	}
	
	protected void attemptConnection()
	{
		if (loadingLabel.isVisible())
			return; // If the loading label is visible, we are already attempting a connection.
		
		loadingLabel.setVisible(true);
		
		/* Reset error messages to hidden. */
		addressErrorLabel.setVisible(false);;
		nameErrorLabel.setVisible(false);
		passwordErrorLabel.setVisible(false);;
		
		String host = addressField.getText();
		String username = nameField.getText();
		String password = new String(passwordField.getPassword());
		
		boolean error = false;
		
		if (host.isEmpty())
		{
			addressErrorLabel.setText("Please provide a server address.");
			addressErrorLabel.setVisible(true);
			error = true;
		}
		else if (host.contains(" "))
		{
			addressErrorLabel.setText("Server address may not contain spaces.");
			addressErrorLabel.setVisible(true);
			error = true;
		}
		
		if (username.isEmpty())
		{
			nameErrorLabel.setText("Please provide a username.");
			nameErrorLabel.setVisible(true);
			error = true;
		}
		else if (username.contains(" "))
		{
			nameErrorLabel.setText("Usernames may not contain spaces.");
			nameErrorLabel.setVisible(true);
			error = true;
		}
		
		if (error)
		{
			loadingLabel.setVisible(false);
			return;
		}
		
		System.out.println("There were no errors!");
		System.out.println("host: "+host);
		System.out.println("username: "+username);
		System.out.println("password: "+password);
		
		s = new ChatSession(host, username, password, this);
	}
	
	/* This will be called once the session has finished constructing. */
	public void update(String msg)
	{
		System.out.println("Update called successfully.");
		try
		{
			if (!s.open()) // This block!!!!!
			{
				loadingLabel.setVisible(false);
				System.out.println("The Chat session could not be opened!");
			}
			else
			{
				loadingLabel.setVisible(false);
				System.out.println("Session opened.");
				Scanner keyboard = new Scanner(System.in);
				String message = keyboard.nextLine();
				while (! message.equals("q") && s.isOpen())
				{
					s.send(message);
					message = keyboard.nextLine();
				}
				keyboard.close();
			}
		}
		finally
		{
			s.close();
		}
	}

	public static void main(String[] args) throws InterruptedException
	{	
		LoginFrame lf = new LoginFrame();

		/*ChatSession s = new ChatSession("localhost:37195", "Vince", "");;
		try
		{
			if (!s.open())
			{
				System.out.println("The Chat session could not be opened!");
			}
			else
			{
				System.out.println("Session opened.");
				Scanner keyboard = new Scanner(System.in);
				String message = keyboard.nextLine();
				while (! message.equals("q") && s.isOpen())
				{
					s.send(message);
					message = keyboard.nextLine();
				}
				keyboard.close();
			}
		}
		finally
		{
			s.close();
		}*/
	}

	public void keyReleased(KeyEvent arg0)
	{	
	}

	public void keyTyped(KeyEvent arg0)
	{
	}
}
