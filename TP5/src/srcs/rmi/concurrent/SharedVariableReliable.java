package srcs.rmi.concurrent;

import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class SharedVariableReliable<T> implements SharedVariable<T>{
	private T object;
	private int cpt_in;
	private int cpt_out;
	private Timer timer;
	private boolean lock;
	
	public SharedVariableReliable(T object) throws RemoteException{
		this.object = object;
		this.timer = new Timer();
		this.lock = false;
		this.cpt_in = 0;
		this.cpt_out = 0;
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
		lock = true;
		timer.schedule(new RelacherTask(), 1000);
		return object;
	}
	
	public synchronized void relacher(T value) {
		timer.cancel();
		timer = new Timer();

		object = value;
		lock = false;
		cpt_out++;
		notifyAll();
	}
	
	private class RelacherTask extends TimerTask{
		@Override
		public void run() {
			synchronized(SharedVariableReliable.this) {
				if (lock) {
					lock = false;
					cpt_out++;
					SharedVariableReliable.this.notifyAll();
				}
			}
		}
	}
}
