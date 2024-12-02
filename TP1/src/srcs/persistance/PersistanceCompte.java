package srcs.persistance;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import srcs.banque.Compte;

public class PersistanceCompte{
	
	public static void saveCompte(String f, Compte e) throws IOException {
		OutputStream os = new FileOutputStream(f);
		e.saveCompte(os);
	}
	
	public static Compte loadCompte(String f) throws IOException{
		InputStream is = new FileInputStream(f);
		Compte e = new Compte(is);
		is.close();
		return e;
	}
}