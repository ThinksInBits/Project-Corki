package info.geared.corki.server;

public interface ClientListener
{
	void receiveMessage(String message, String name);
}