package srcs.service.annuaire;

import srcs.service.ClientProxy;
import srcs.service.MyProtocolException;

public class AnnuaireProxy extends ClientProxy implements Annuaire{
		
	public AnnuaireProxy(String host, int port) {
		super(host, port);
	}
	
	public String lookup(String name) {
		Object[] args = {name};
		try {
			return (String) invokeService("lookup", args);
		} catch(MyProtocolException e) {
			return "";
		}
	}
	
	public void bind(String name, String value) {
		Object[] args = {name, value};
		try {
			invokeService("bind", args);
		} catch (MyProtocolException e) {
			e.printStackTrace();
		}
	}
	
	public void unbind(String name) {
		Object[] args = {name};
		try {
			invokeService("unbind", args);
		} catch (MyProtocolException e) {
			e.printStackTrace();
		}
	}
}
