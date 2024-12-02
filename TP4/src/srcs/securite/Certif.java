package srcs.securite;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;

@SuppressWarnings("serial")
public class Certif implements Serializable{
	private final String id;
	private final PublicKey pk;
	private final byte[] signature;
	private final String algorithm;
	
	Certif(String id, PublicKey pk, byte[] signature, String algorithm) {
		this.id = id;
		this.pk = pk;
		this.signature = signature;
		this.algorithm = algorithm;
	}
	
	public String getIdentifier() {
		return id;
	}
	
	public PublicKey getPublicKey() {
		return pk;
	}
	
	public byte[] getAuthoritySignature(){
		return signature;
	}
	
	public boolean verify(PublicKey publickeyauthority) throws GeneralSecurityException{
		Signature s = Signature.getInstance(algorithm);
		s.initVerify(publickeyauthority);
		s.update(id.getBytes());
		s.update(pk.getEncoded());
		return s.verify(signature);
	}
}
