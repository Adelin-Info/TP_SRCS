package srcs.securite;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ChannelBasic implements Channel{
	private Socket s;
	
	public ChannelBasic(Socket s) {
		this.s = s;
	}
	
	public void send(byte[] bytesArray) throws IOException{
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(bytesArray);
		oos.flush();
	}
	
	public byte[] recv() throws IOException{
		ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		try {
			return (byte[]) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public InetAddress getRemoteHost() {
		return s.getInetAddress();
	}
	
	public int getRemotePort() {
		return s.getPort();
	}
	
	 public InetAddress getLocalHost() {
		 return s.getLocalAddress();
	 }
	public int getLocalPort() {
		return s.getLocalPort();
	}
}
