package srcs.service.calculatrice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import srcs.service.MyProtocolException;
import srcs.service.SansEtat;
import srcs.service.Service;

@SansEtat
public class CalculatriceService implements Service, Calculatrice {
	
	public Integer add(Integer op1, Integer op2) {
		return (op1 + op2);
	}
	
	public Integer sous(Integer op1, Integer op2) {
		return (op1 - op2);
	}
	
	public Integer mult(Integer op1, Integer op2) {
		return (op1 * op2);
	}
	
	public ResDiv div(Integer op1, Integer op2) {
		if(op2 == 0) {
			throw new ArithmeticException();
		}
		ResDiv rd = new ResDiv(op1, op2);
		return rd;
	}
	
	public void execute(Socket s){
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			String operation = ois.readUTF();
			int operand1 = ois.readInt();
			int operand2 = ois.readInt();
			Object res;
			switch (operation) {
				case "add":
					res = add(operand1, operand2);
					oos.writeObject(res);
					oos.flush();
					break;
				
				case "sous":
					res = sous(operand1, operand2);
					oos.writeObject(res);
					oos.flush();
					break;
				
				case "mult":
					res = mult(operand1, operand2);
					oos.writeObject(res);
					oos.flush();
					break;
				
				case "div":
					res = div(operand1, operand2);
					oos.writeObject(res);
					oos.flush();
					break;
				
				default:
					oos.writeObject(new MyProtocolException("Méthode n'existe pas"));
			}
			ois.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
