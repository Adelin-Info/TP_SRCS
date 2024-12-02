package srcs.rmi.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HostImpl implements Host{
	private final Map<String, FunctionService<?,?>> services;
	
	public HostImpl() throws RemoteException{
		services = new HashMap<>();
	}
	
	@Override
	public <P,R> FunctionService<P,R> deployNewService(String nameService, Class<? extends FunctionService<P,R>> classService) throws RemoteException{
		if (services.containsKey(nameService)) {
			throw new RemoteException("Service already exist");
		}
		
		FunctionService<P, R> newService;
		try {
			newService = classService.getDeclaredConstructor(String.class).newInstance(nameService);
			UnicastRemoteObject.exportObject(newService,0);
			services.put(nameService, newService);
			return newService;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	@Override
	public <P,R> FunctionService<P,R> deployExistingService(FunctionService<P,R> service) throws RemoteException{
		if (services.containsKey(service.getName())) {
			throw new RemoteException("Service already exist");
		}
		UnicastRemoteObject.exportObject(service,0);
		services.put(service.getName(), service);
		return service;
	}
	
	@Override
	public boolean undeployService(String nameService) throws RemoteException{
		if (!services.containsKey(nameService)) {
			return false;
		}
		UnicastRemoteObject.unexportObject(services.get(nameService), true);
		services.remove(nameService);
		return true;
	}
	
	@Override
	public ArrayList<String> getServices() throws RemoteException{
		return new ArrayList<>(services.keySet());
	}

}
