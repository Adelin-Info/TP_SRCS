package srcs.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Host extends Remote{
	public <P,R> FunctionService<P,R> deployNewService(String nameService, Class<? extends FunctionService<P,R>> classService) throws RemoteException;
	public <P,R> FunctionService<P,R> deployExistingService(FunctionService<P,R> service) throws RemoteException;
	public  boolean undeployService(String nameService) throws RemoteException;
	public ArrayList<String> getServices() throws RemoteException;
}
