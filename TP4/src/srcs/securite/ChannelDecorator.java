package srcs.securite;

import java.io.IOException;
import java.net.InetAddress;

public class ChannelDecorator implements Channel{
	private Channel c;
	
	public ChannelDecorator(Channel c) {
		this.c = c;
	}
	
	public void send(byte[] bytesArray) throws IOException{
		c.send(bytesArray);
	}
	
	public byte[] recv() throws IOException{
		return c.recv();
	}
	
	public InetAddress getRemoteHost(){
		return c.getRemoteHost();
	}
	
	public int getRemotePort() {
		return c.getRemotePort();
	}
	
	public InetAddress getLocalHost() {
		return c.getLocalHost();
	}
	
	public int getLocalPort() {
		return c.getLocalPort();
	}
}
