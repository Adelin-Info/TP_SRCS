package srcs.securite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;

public class Authentication {

	private Certif certiflocal;
	private Certif certifdistant;
	private KeyPair kplocal;
	private final String nonce;
	
	// Server
	public Authentication(Channel channel, Certif certiflocal, KeyPair kplocal, PasswordStore pws, PublicKey pkauthority) throws IOException, GeneralSecurityException{
		this.certiflocal = certiflocal;
		this.kplocal = kplocal;
		
		// Envoi certificat et vérification certificat client
		channel.send(serialize(this.certiflocal));
		Certif certifdistant = deserialize(channel.recv());
		if(!certifdistant.verify(pkauthority)) {
			throw new CertificateCorruptedException("Certificate client signature verification failed");
		}
		this.certifdistant = certifdistant;
		
		// Generation d'un nonce
		SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG"); 
		this.nonce = String.valueOf(securerandom.nextInt());
		
		// Envoi du nonceserveur non chiffré au client
		channel.send(nonce.getBytes());
		
		// Envoi du nonceclient chiffré avec clé privée serveur
		channel.send(encryptedNonce(channel.recv()));
		
		// Récupération du nonce serveur déchiffré avec clé publique du client et vérification
		String response = new String(decryptedNonce(channel.recv()));
		if(!response.equals(nonce)) {
			throw new AuthenticationFailedException("Authentication client failed");
		}
		
		// Récuperation du login et du mot de passe et vérification
		String login = new String(channel.recv());
		String password = new String(decrypted(channel.recv()));
		if(!pws.checkPassword(login, password)) {
			throw new AuthenticationFailedException("Identification client failed");
		}		
	}
	
	// Client
	public Authentication(Channel channel, Certif certiflocal, KeyPair kplocal, String login, String password, PublicKey pkauthority) throws IOException, GeneralSecurityException{
		this.certiflocal = certiflocal;
		this.kplocal = kplocal;
		
		// Envoi certificat et vérification certificat serveur
		channel.send(serialize(this.certiflocal));
		Certif certifdistant = deserialize(channel.recv());
		if(!certifdistant.verify(pkauthority)) {
			throw new CertificateCorruptedException("Certificate server signature verification failed");
		}
		this.certifdistant = certifdistant;
		
		// Generation d'un nonce
		SecureRandom securerandom = SecureRandom.getInstance("SHA1PRNG"); 
		this.nonce = String.valueOf(securerandom.nextInt());
		
		// Envoi du nonceclient non chiffré au serveur
		channel.send(nonce.getBytes());
		
		// Envoi du nonceserveur chiffré avec clé privée client
		channel.send(encryptedNonce(channel.recv()));
		
		// Récupération du nonce client déchiffré avec clé publique du serveur et vérification
		String response = new String(decryptedNonce(channel.recv()));
		if(!response.equals(nonce)) {
			throw new AuthenticationFailedException("Authentication server failed");
		}
		
		// Envoi du login et du mot de passe chiffré avec la clé publique du serveur
		channel.send(login.getBytes());
		channel.send(encrypted(password.getBytes()));
	}
	
	public Certif getLocalCertif() {
		return certiflocal;
	}
	
	public Certif getRemoteCertif() {
		return certifdistant;
	}
	
	public KeyPair getLocalKeys() {
		return kplocal;
	}
	
	private Certif deserialize(byte[] bytesArray) throws IOException{
		ByteArrayInputStream bais = new ByteArrayInputStream(bytesArray);
		ObjectInputStream ois = new ObjectInputStream(bais);
		try {
			return (Certif) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			ois.close();
			bais.close();
		}
	}
	
	private byte[] serialize(Certif c) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(c);
		oos.flush();
		try {
			return baos.toByteArray();
		} finally {
			oos.close();
			baos.close();
		}
	}
	
	private byte[] decryptedNonce(byte[] bytesArray) throws GeneralSecurityException, IOException{
		Cipher cipher;
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, certifdistant.getPublicKey());
		byte[] decrypted = cipher.doFinal(bytesArray);
		return decrypted;
	}
	
	private byte[] encryptedNonce(byte[] bytesArray) throws IOException, GeneralSecurityException{
		Cipher cipher;
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, kplocal.getPrivate());
		byte[] encrypted = cipher.doFinal(bytesArray);
		return encrypted;
	}
	
	public byte[] encrypted(byte[] bytesArray) throws IOException, GeneralSecurityException{
		Cipher cipher;
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, certifdistant.getPublicKey());
		byte[] encrypted = cipher.doFinal(bytesArray);
		return encrypted;
	}
	
	public byte[] decrypted(byte[] bytesArray) throws IOException, GeneralSecurityException{
		Cipher cipher;
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, kplocal.getPrivate());
		byte[] decrypted = cipher.doFinal(bytesArray);
		return decrypted;
	}
}

