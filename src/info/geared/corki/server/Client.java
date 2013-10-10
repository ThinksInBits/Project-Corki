package info.geared.corki.server;

import info.geared.corki.net.Sender;

import java.io.PrintStream;
import java.net.Socket;

public class Client
{
	protected String name;
	protected Socket socket;
	protected Sender sender;
	protected PrintStream out;
	
	public Client()
	{
		
	}
}
