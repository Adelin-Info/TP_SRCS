package srcs.rmi.service.test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.After;
import org.junit.Before;

import srcs.rmi.service.Host;
import srcs.rmi.service.HostImpl;

public class SystemDeployer {
	
	private Registry registry;
	private Host h1;
	private Host h2;
	
	@Before
	public void deploy() {
		try {
			h1 = new HostImpl();
			UnicastRemoteObject.exportObject(h1, 0);
			
			h2 = new HostImpl();
			UnicastRemoteObject.exportObject(h2, 0);
			
			registry = LocateRegistry.createRegistry(1099);
			registry.rebind("host1", h1);
			registry.rebind("host2", h2);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void undeploy() {
		try {
			registry.unbind("host1");
			UnicastRemoteObject.unexportObject(h1,true);
			
			registry.unbind("host2");
			UnicastRemoteObject.unexportObject(h2, true);
			
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}

