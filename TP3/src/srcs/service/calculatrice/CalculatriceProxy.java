package srcs.service.calculatrice;

import srcs.service.ClientProxy;
import srcs.service.MyProtocolException;

public class CalculatriceProxy extends ClientProxy implements Calculatrice{
	
	public CalculatriceProxy(String host, int port) {
		super(host, port);
	}
	
	public Integer add(Integer op1, Integer op2){
		Object[] args = {op1, op2};
		try {
			return (Integer) invokeService("add", args);
		} catch (MyProtocolException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public Integer sous(Integer op1, Integer op2) {
		Object[] args = {op1, op2};
		try {
			return (Integer) invokeService("sous", args);
		} catch (MyProtocolException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public Integer mult(Integer op1, Integer op2) {
		Object[] args = {op1, op2};
		try {
			return (Integer) invokeService("mult", args);
		} catch (MyProtocolException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public ResDiv div(Integer op1, Integer op2) {
		if(op2 == 0) {
			throw new ArithmeticException();
		}
		Object[] args = {op1, op2};
		try {
			return (ResDiv) invokeService("div", args);
		} catch (MyProtocolException e) {
			e.printStackTrace();
			return null;
		}
	}
}
