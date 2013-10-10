package info.geared.corki.server;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class ChatServer implements ClientListener, Runnable
{
	protected ArrayList<Client> clients;
	protected ExecutorService executor;
	
	private Thread connectionThread;

	public void run()
	{
		// TODO Auto-generated method stub

	}

	public void receiveMessage(String message, String name)
	{
		// TODO Auto-generated method stub

	}
	public static void main(String args[])
	{
		System.out.println("argo fuck yourself");
	}
}

