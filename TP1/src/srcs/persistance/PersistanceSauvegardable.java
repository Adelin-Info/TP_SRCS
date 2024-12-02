package srcs.persistance;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public class PersistanceSauvegardable{
	
	public static void save(String f, Sauvegardable s) throws IOException {
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
		dos.writeUTF(s.getClass().getCanonicalName());
		s.save(dos);
		dos.close();
	}
	
	public static Sauvegardable load(String fichier) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, ClassNotFoundException {
		DataInputStream dis = new DataInputStream(new FileInputStream(fichier));
		String nameClass = dis.readUTF();
		Sauvegardable s = Class.forName(nameClass).asSubclass(Sauvegardable.class).getConstructor(InputStream.class).newInstance(dis);
		dis.close();
		return s;
	}
}