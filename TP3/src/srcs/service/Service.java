package srcs.service;

import java.net.Socket;

public interface Service{
	public abstract void execute(Socket connexion);
}