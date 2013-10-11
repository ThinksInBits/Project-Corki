package info.geared.corki.net;

import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender
{

	protected class SenderWorker implements Runnable
	{
		private String message;
		private PrintStream out;

		public SenderWorker(String message, PrintStream out)
		{
			this.message = message;
			this.out = out;
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

	protected ExecutorService executor;
	protected boolean running;
	protected static final int POOL_SIZE = 3;

	public Sender()
	{
		running = false;
	}

	public void start()
	{
		if (running == true)
			return;
		executor = Executors.newFixedThreadPool(3);
		running = true;
	}

	public void stop()
	{
		if (!executor.isShutdown())
		executor.shutdown();
		running = false;
	}

	public boolean send(String message, PrintStream out)
	{
		if (running == false)
			return false;
		
		if (message.isEmpty())
			return false;
		
		if (out.checkError())
			return false;
		
		executor.execute(new SenderWorker(message, out));
		return true;
	}
}
