package info.geared.corki.net;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender
{
	
	protected class SenderWorker implements Runnable
	{
		private String message;
		
		public SenderWorker(String message)
		{
			this.message = message;
		}
		
		protected synchronized void send()
		{
			out.println(message);
		}

		public void run()
		{
			send();
		}
		
	}
	
	protected PrintStream out;
	protected ExecutorService executor;
	
	public Sender(PrintStream out)
	{
		this.out = out;
	}
	
	public void start()
	{
		executor = Executors.newFixedThreadPool(3);
	}
	
	public void stop()
	{
		executor.shutdown();
	}
	
	public boolean send(String message)
	{
		if (message.isEmpty())
			return false;
		
		Runnable worker = new SenderWorker(message);
		executor.execute(worker);
		return true;
	}
}
