package srcs.rmi.concurrent;

import java.rmi.RemoteException;

public class SharedVariableClassical<T> implements SharedVariable<T>{
	private T object;
	private int cpt_in = 0;
	private int cpt_out = 0;
	
	public SharedVariableClassical(T object) throws RemoteException{
		this.object = object;
	}
	
	public synchronized T obtenir() {
		int ticket = cpt_in++;
		while (ticket != cpt_out) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return object;
	}
	
	public synchronized void relacher(T value) {
		object = value;
		cpt_out++;
		notifyAll();
	}
}	
