package srcs.service.calculatrice;

import srcs.service.AppelDistant;
import srcs.service.SansEtat;

@SansEtat
public class CalculatriceAppelDistant implements AppelDistant, Calculatrice{
	
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
		return new ResDiv(op1, op2);
	}
}
