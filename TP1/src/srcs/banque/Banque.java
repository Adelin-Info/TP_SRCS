package srcs.banque;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import srcs.persistance.Sauvegardable;

public class Banque implements Sauvegardable{

	private final Set<Client> clients;
	
	public Banque() {
		clients=new HashSet<>();
	}
	
	public Banque(InputStream is) throws IOException {
		clients = new HashSet<>();
		DataInputStream dis = new DataInputStream(is);
		int size = dis.readInt();
		for(int i=0; i<size; i++) {
			addNewClient(new Client(dis));
		}
		dis.close();
	}
		
	public int nbClients() {
		return clients.size();
	}
	
	public int nbComptes() {
		Set<Compte> comptes = new HashSet<>();
		for(Client c : clients) {
			comptes.add(c.getCompte());
		}
		return comptes.size();
	}
	
	public Client getClient(String nom) {
		for(Client c : clients) {
			if(c.getNom().equals(nom)) return c;
		}
		return null;
	}
	
	public boolean addNewClient(Client c) {
		return clients.add(c);
	}

	public void save(OutputStream os) throws IOException {
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeInt(clients.size());
		for(Client c : clients) {
			c.save(os);
		}
		dos.close();
	}
	

}
