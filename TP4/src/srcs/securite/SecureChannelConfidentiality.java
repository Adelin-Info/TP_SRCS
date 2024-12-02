package srcs.securite;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecureChannelConfidentiality extends ChannelDecorator{
	private final SecretKey secretkey;
	
	public SecureChannelConfidentiality(Channel channel, Authentication authentication, String algorithm, int sizekey) throws IOException, GeneralSecurityException{
		super(channel);
		
		KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
		keyGen.init(sizekey);
		SecretKey localkey = keyGen.generateKey();
		
		channel.send(authentication.encrypted(localkey.getEncoded()));
		
		SecretKey distantkey = new SecretKeySpec(authentication.decrypted(channel.recv()), algorithm);
		
		if(authentication.getLocalCertif().getIdentifier().equals("server")) {
			this.secretkey = localkey;
		}
		else {
			this.secretkey = distantkey;
		}
	}
	
	public SecretKey getSecretKey() {
		return secretkey;
	}
	
	@Override
	public void send(byte[] bytesArray) throws IOException {
		try {
			Cipher cipher = Cipher.getInstance(secretkey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, secretkey);
			byte[] encryptedmessage = cipher.doFinal(bytesArray);
			super.send(encryptedmessage);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] recv() throws IOException{
		try {
			byte[] encryptedmessage = super.recv();
			Cipher cipher = Cipher.getInstance(secretkey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			byte[] decryptedmessage = cipher.doFinal(encryptedmessage);
			return decryptedmessage;
		} catch (GeneralSecurityException e){
			return null;
		}
	}
}
