package srcs.service.calculatrice;

import java.io.Serializable;

public interface Calculatrice {
	public abstract Integer add(Integer op1, Integer op2);
	public abstract Integer sous(Integer op1, Integer op2);
	public abstract Integer mult(Integer op1, Integer op2);
	public abstract ResDiv div(Integer op1, Integer op2);
	
	@SuppressWarnings("serial")
	public static class ResDiv implements Serializable{
		private int quotient;
		private int reste;
		
		public ResDiv(Integer op1, Integer op2) {
			quotient = op1/op2;
			reste = op1%op2;
		}
		
		public int getQuotient() {
			return quotient;
		}
		
		public int getReste() {
			return reste;
		}
	}
}
