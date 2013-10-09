package info.geared.corki.client;

import info.geared.corki.net.Sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ChatSession implements Runnable
{
	public enum Status
	{
		DISCONNECTED, CONNECTED, UNKNOWN_HOST, IO_EXCEPTION, NO_MD5, NO_UTF8, NOT_INI, NAME_TAKEN
	}

	protected static final int DEFAULT_PORT = 73195;
	protected static final int TIMEOUT = 3000;

	protected String hostname;
	protected String username;
	protected int port;
	protected byte[] hashedPassword;

	protected ArrayList<ChatSessionListener> listeners;
	protected Sender sender;
	protected Socket socket;
	protected Thread thread;

	protected Status status;
	protected boolean isClosed;

	public ChatSession(String host, String username, String password)
	{
		status = Status.NOT_INI;
		isClosed = true;
		thread = new Thread(this);
		
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
			socket.setSoTimeout(TIMEOUT);
			sender = new Sender(socket.getOutputStream());
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
	
	public boolean send(String message)
	{
		return sender.send(message);
	}
	
	public boolean open()
	{
		/* Check if the session is already open. */
		if (isClosed == false)
			return false;
		
		/* If the session status is not DISCONNECTED, then the
		 * session is not ready to be opened. */
		if (status != Status.DISCONNECTED)
			return false;
		
		/* Check if the main receiving thread is already running. */
		if (thread.isAlive())
			return false;
		else
			thread.start();
		
		/* Try to send connect command to server. */
		if (!sender.send("CONNECT:"+username))
			return false;
		
		isClosed = false;
		status = Status.CONNECTED;
		return true;
	}
	
	public void close()
	{
		sender.send("DISCONNECT");
		isClosed = true;
		status = Status.DISCONNECTED;
	}
	
	public void addChatSessionListener(ChatSessionListener csl)
	{
		listeners.add(csl);
	}
	
	public Status getStatus()
	{
		return status;
	}

	public void run()
	{
		BufferedReader in;
		try
		{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (isClosed == false)
			{
				/* Try to read a line from the socket. */
				String line = in.readLine();
				
				/* If the read timedout then line will be empty or null. */
				if (line == null || line.isEmpty())
					continue;
				
				/* When a line is read update the listeners. */
				for(ChatSessionListener listener : listeners)
				{
					listener.update(line);
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	protected synchronized boolean isNameTaken()
	{
		return false;
	}

	protected synchronized boolean connect()
	{
		return false;
	}

	protected synchronized void disconnect()
	{
	}
}
