package info.geared.corki.server;

import java.net.Socket;

public class Client
{
	public class ClientWorker implements Runnable
	{
			
		public void run()
		{
			
		}

	}

	
	protected Socket socket;
	protected String name;
	protected ClientWorker worker;
	protected boolean running;
	
	public Client(Socket socket)
	{
		running = false;
		this.socket = socket;
	}
	
	public void disconnect()
	{
		running = false;
	}
}
