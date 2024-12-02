package srcs.service.annuaire;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import srcs.service.EtatGlobal;
import srcs.service.MyProtocolException;
import srcs.service.Service;
import srcs.service.VoidResponse;

@EtatGlobal
public class AnnuaireService implements Service, Annuaire {
	
	private Map<String, String> annuaires = new HashMap<String, String>();
	
	public String lookup(String name) {
		if(!annuaires.containsKey(name)) {
			return "";
		}
		return annuaires.get(name);
	}
	
	public void bind(String name, String value) {
		annuaires.put(name, value);
	}
	
	public void unbind(String name) {
		annuaires.remove(name);
	}
	
	public void execute(Socket s) {
		try {
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			String operation = ois.readUTF();
			String name;
			String value;
			switch (operation) {
				case "lookup":
					name = ois.readUTF();
					oos.writeObject(lookup(name));
					oos.flush();
					break;
				
				case "bind":
					name = ois.readUTF();
					value = ois.readUTF();
					bind(name, value);
					oos.writeObject(new VoidResponse());
					oos.flush();
					break;
				
				case "unbind":
					name = ois.readUTF();
					unbind(name);
					oos.writeObject(new VoidResponse());
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
