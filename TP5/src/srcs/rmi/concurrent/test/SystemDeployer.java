package srcs.rmi.concurrent.test;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.After;
import org.junit.Before;

import srcs.rmi.concurrent.SharedVariable;
import srcs.rmi.concurrent.SharedVariableClassical;
import srcs.rmi.concurrent.SharedVariableReliable;

public class SystemDeployer {
	
	private Registry registry;
	private SharedVariable<Integer> svc;
	private SharedVariable<Integer> svr;
	
	@Before
	public void deploy() {
		try {
			svc = new SharedVariableClassical<Integer>(0);
			UnicastRemoteObject.exportObject(svc, 0);
			
			svr = new SharedVariableReliable<Integer>(0);
			UnicastRemoteObject.exportObject(svr, 0);
			
			registry = LocateRegistry.createRegistry(1099);
			registry.bind("variableIntegerClassical", svc);
			registry.bind("variableIntegerReliable", svr);
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void undeploy() {
		try {
			registry.unbind("variableIntegerClassical");
			UnicastRemoteObject.unexportObject(svc, true);
			
			registry.unbind("variableIntegerReliable");
			UnicastRemoteObject.unexportObject(svr, true);
			
			UnicastRemoteObject.unexportObject(registry, true);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
	}
}
