package info.geared.corki.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class LoginFrame extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;

	/* All JComponents fields here. */
	protected JTextField nameField;
	protected JTextField addressField;
	protected JTextField passwordField;
	
	protected JLabel nameErrorLabel;
	protected JLabel addressErrorLabel;
	protected JLabel passwordErrorLabel;
	
	protected JButton loginButton;
	
	protected Image icon;
	protected Image corkiChat;

	LoginFrame()
	{		
		buildUI();
		loginButton.requestFocusInWindow();
	}

	private void buildUI()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			icon = ImageIO.read(new File("icon.png"));
			corkiChat = ImageIO.read(new File("corkichat.png"));
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
		
		getContentPane().add(new ImageIcon(corkiChat));
		
		addressField = new HintTextField("Hostname eg: geared.info");
		addressField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		addressField.setSize(275, 23);
		addressField.setLocation(54, 104);
		getContentPane().add(addressField);
		
		nameField = new HintTextField("Username");
		nameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		nameField.setSize(new Dimension(275, 23));
		nameField.setLocation(54, 199);
		getContentPane().add(nameField);
		
		passwordField = new HintTextField("Password (optional)");
		passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		passwordField.setSize(275, 23);
		passwordField.setLocation(54, 294);
		getContentPane().add(passwordField);
		
		loginButton = new JButton("Login");
		loginButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
		loginButton.setSize(85, 23);
		loginButton.setLocation(149, 389);
		getContentPane().add(loginButton);
		
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
		// Get data from fields

		ChatSession s = new ChatSession("localhost", "david", "");
		if (s.getStatus() != ChatSession.Status.DISCONNECTED)
		{
			// Session failed
		}
		else
		{
			ClientFrame client = new ClientFrame(s);
			s.addChatSessionListener(client);
			dispose();
		}
	}

	public static void main(String[] args) throws InterruptedException
	{	
		LoginFrame lf = new LoginFrame();

		ChatSession s = new ChatSession("localhost:37195", "Vince", "");;
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
		}
	}

}
