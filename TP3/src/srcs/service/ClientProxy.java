package srcs.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public abstract class ClientProxy {
	private final String host;
	private final int port;
	
	public ClientProxy(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public Object invokeService(String name, Object[] params) throws MyProtocolException{
		try{
			Socket s = new Socket(host, port);
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(name);
			oos.writeObject(params);
			oos.flush();
			
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			Object res = ois.readObject();
			s.close();
			if (res instanceof MyProtocolException) {
				throw (MyProtocolException) res;
			}
			return res;
		} catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
