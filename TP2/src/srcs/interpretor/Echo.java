package srcs.interpretor;

import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;

public class Echo implements Command {
	private StringTokenizer args;
	
	public Echo(String s) {
		args = new StringTokenizer(s);
	}
	
	public void execute(PrintStream out) throws IOException{
		args.nextToken();
		String res = args.nextToken();
		while(args.hasMoreTokens()) {
			res += " " + args.nextToken();
		}
		out.println(res);
	}
}
