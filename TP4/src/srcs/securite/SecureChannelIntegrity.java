package srcs.securite;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Signature;

public class SecureChannelIntegrity extends ChannelDecorator{
	private final Authentication authentication;
	private final String algorithm;
	
	public SecureChannelIntegrity(Channel channel, Authentication authentication, String algorithm) {
		super(channel);
		this.authentication = authentication;
		this.algorithm = algorithm;
	}
	
	@Override
	public void send(byte[] bytesArray) throws IOException{
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(authentication.getLocalKeys().getPrivate());
			signature.update(bytesArray);
			byte[] sign = signature.sign();
			super.send(bytesArray);
			super.send(sign);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public byte[] recv() throws IOException{
		byte[] bytesArray = super.recv(); 
		byte[] sign = super.recv();
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initVerify(authentication.getRemoteCertif().getPublicKey());
			signature.update(bytesArray);
			boolean res = signature.verify(sign);
			if (res == true) {
				return bytesArray;
			}
			else {
				throw new CorruptedMessageException();
			}
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}	
	}
}
