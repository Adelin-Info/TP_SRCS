package srcs.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public interface AppelDistant extends Service{
	default void execute(Socket s){
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			String name = (String) ois.readObject();
			Object[] params = (Object[]) ois.readObject();
			
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			try {
				Method m = this.getClass().getMethod(name, getParameterTypes(params));
				if(m.getReturnType().getName().equals("void")) {
					m.invoke(this, params);
					oos.writeObject(new VoidResponse());
					oos.flush();
				}
				else {
					Object res = m.invoke(this, params);
					oos.writeObject(res);
					oos.flush();
				}
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
				oos.writeObject(new MyProtocolException("Méthode n'existe pas"));
				oos.flush();
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private Class<?>[] getParameterTypes(Object[] args) {
        if (args == null) {
            return new Class<?>[0];
        }
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return parameterTypes;
    }
}
