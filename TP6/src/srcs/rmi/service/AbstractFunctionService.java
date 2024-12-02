package srcs.rmi.service;

import java.rmi.RemoteException;

@SuppressWarnings("serial")
public abstract class AbstractFunctionService<P,R> implements FunctionService<P,R>, Cloneable{
	private final String name;
	private boolean isMigrated;
	private FunctionService<P,R> copy;
	
	public AbstractFunctionService(String name){
		this.name = name;
		isMigrated = false;
	}

	@Override
	public String getName() throws RemoteException{
		if (isMigrated) {
			return copy.getName();
		}
		return name;
	}
	
	@Override
	public R invoke(P parameter) throws RemoteException{
		if (isMigrated) {
			return copy.invoke(parameter);
		}
		return perform(parameter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FunctionService<P,R> migrateTo(Host host) throws RemoteException{
		if (isMigrated) {
			throw new RemoteException();
		}
		try {
			copy = (FunctionService<P, R>) this.clone();
			copy = host.deployExistingService(copy);
			isMigrated = true;
			return copy;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
			
	}
	
	protected abstract R perform(P parameter);
}
