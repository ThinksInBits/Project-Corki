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
	protected static final int POOL_SIZE = 3;
	
	public Sender(OutputStream out)
	{
		this.out = new PrintStream(out);
	}
	
	public void start()
	{
		executor = Executors.newFixedThreadPool(POOL_SIZE);
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
