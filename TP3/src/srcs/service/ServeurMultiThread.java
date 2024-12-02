package srcs.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMultiThread{
	private final int port;
	private final Class<? extends Service> cl_service;
	private Service global;
	
	public ServeurMultiThread(int port, Class<? extends Service> cl_service) {
		this.port = port;
		this.cl_service = cl_service;
		global = null;
	}
	
	public void listen(){
		ServerSocket ss;
		try {
			ss =  new ServerSocket(port);
			while(true) {
				Socket s = ss.accept();
				Service service = getService();
				Thread t = new Thread( () -> service.execute(s)); 
				t.start();
			}
		} catch(IOException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private Service getService() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(cl_service.getAnnotation(SansEtat.class) != null) {
			return cl_service.getConstructor().newInstance();
		}
		else if (cl_service.getAnnotation(EtatGlobal.class) != null) {
			if(global == null) {
				global = cl_service.getConstructor().newInstance();
			}
			return global;
		}
		else {
			throw new IllegalStateException();
		}
	}
}