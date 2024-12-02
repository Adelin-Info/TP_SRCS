package srcs.rmi.concurrent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SharedVariable<T> extends Remote{
	public T obtenir() throws RemoteException;
	public void relacher(T value) throws RemoteException;
}
