package srcs.persistance;

import java.io.IOException;
import java.io.OutputStream;

public interface Sauvegardable{
	
	public abstract void save(OutputStream os) throws IOException;
}