package srcs.securite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PasswordStore {
	private final String algorithm_h;
	private final Map<String, byte[]> passwords;
	
	public PasswordStore(String algorithm_h) {
		this.algorithm_h = algorithm_h;
		passwords = new HashMap<String, byte[]>();
	}
	
	public void storePassword(String user, String passwd) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm_h);
		md.update(passwd.getBytes());
		byte[] digest = md.digest();
		passwords.put(user, digest);
	}
	
	public boolean checkPassword(String user, String passwd) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm_h);
		md.update(passwd.getBytes());
		byte[] digest = md.digest();
		return Arrays.equals(passwords.get(user), digest);
	}
}
