package srcs.securite;

import java.io.IOException;
import java.net.InetAddress;

public interface Channel {

	void send(byte[] bytesArray) throws IOException;

	byte[] recv() throws IOException;
	
	InetAddress getRemoteHost();
	int getRemotePort();
	
	InetAddress getLocalHost();
	int getLocalPort();

}
