package info.geared.corki;

import java.io.OutputStream;
import java.io.PrintStream;

public class Sender implements Runnable
{
	protected PrintStream out;
	protected String message;
	protected Thread thread;
	
	Sender(OutputStream out)
	{
		thread = new Thread(this);
		this.out = new PrintStream(out);
		message = "";
	}
	
	public void run()
	{
		out.println(message);
		message = "";
	}
	
	public boolean send(String message)
	{
		this.message = message;
		if (message.isEmpty())
			return false;
		
		if (thread.isAlive())
			return false;
		
		thread.start();
		return true;
	}
}
