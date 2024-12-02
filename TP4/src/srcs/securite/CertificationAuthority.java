package srcs.securite;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;

public class CertificationAuthority {
	private final KeyPair kp;
	private final String algorithm_sign;
	private final Map<String, Certif> certificats;
	
	public CertificationAuthority(String algorithm_as, int sizekey, String algorithm_sign) throws NoSuchAlgorithmException {
		kp = Util.generateNewKeyPair(algorithm_as, sizekey);
		this.algorithm_sign = algorithm_sign;
		certificats = new HashMap<String, Certif>();
	}
	
	public PublicKey getPublicKey() {
		return kp.getPublic();
	}
	
	public Certif getCertificate(String identifier) {
		return certificats.get(identifier);
	}
	
	public Certif declarePublicKey(String identifier, PublicKey pubk) throws GeneralSecurityException{
		if(certificats.containsKey(identifier)) {
			throw new GeneralSecurityException();
		}
		
		Signature s = Signature.getInstance(algorithm_sign);
		s.initSign(kp.getPrivate());
		s.update(identifier.getBytes());
		s.update(pubk.getEncoded());
		byte[] signature = s.sign();
		Certif c = new Certif(identifier, pubk, signature, algorithm_sign);
		certificats.put(identifier, c);
		return c;
	}
}
