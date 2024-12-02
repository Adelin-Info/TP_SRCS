package srcs.interpretor;

import java.io.PrintStream;
import java.io.IOException;

public interface Command {

	public void execute(PrintStream out) throws IOException;
}