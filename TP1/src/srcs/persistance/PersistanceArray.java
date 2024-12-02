package srcs.persistance;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class PersistanceArray{
	
	public static void saveArrayInt(String f, int[] tab) throws IOException{
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
		dos.writeInt(tab.length);
		for(int i : tab) {
			dos.writeInt(i);
		}
		dos.flush();
		dos.close();
	}
	
	public static int[] loadArrayInt(String fichier) throws IOException{
		DataInputStream dis = new DataInputStream(new FileInputStream(fichier));
		int[] tab = new int[dis.readInt()];
		for(int i = 0; i < tab.length; i++) {
			tab[i] = dis.readInt();
		}
		dis.close();
		return tab;
	}
}