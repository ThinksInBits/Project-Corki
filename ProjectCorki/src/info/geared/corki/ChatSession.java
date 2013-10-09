package info.geared.corki;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class ChatSession implements Runnable
{
	public enum Status {DISCONNECTED, CONNECTED, UNKNOWN_HOST, IO_EXCEPTION, NO_MD5, NO_UTF8, NOT_INI, NAME_TAKEN}
	protected static final int DEFAULT_PORT = 73195;
	
	protected String hostname;
	protected String username;
	protected int port;
	protected byte[] hashedPassword;

	protected ArrayList<ChatSessionListener> listeners;
	protected Socket socket;

	protected Status status;

	ChatSession(String host, String username, String password)
	{
		status = Status.NOT_INI;
		if (host.contains(":"))
		{
			hostname = host.split(":")[0];
			port = Integer.parseInt(host.split(":")[1]);
		}
		else
		{
			hostname = host;
			port = DEFAULT_PORT;
		}

		this.username = username;

		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			hashedPassword = md.digest(password.getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			status = Status.NO_MD5;
			return; // Return before connected is set to true
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			status = Status.NO_UTF8;
			return; // Return before connected is set to true
		}

		try
		{
			socket = new Socket(hostname, port);
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			status = Status.UNKNOWN_HOST;
			return;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			status = Status.IO_EXCEPTION;
			return;
		}
		
		if (isNameTaken())
		{
			status = Status.NAME_TAKEN; 
			return;
		}
		
		status = Status.DISCONNECTED;
	}
	
	protected boolean isNameTaken()
	{
		return false;
	}

	public void run()
	{
		
	}
}
