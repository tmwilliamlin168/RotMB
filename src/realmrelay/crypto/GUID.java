package realmrelay.crypto;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class GUID {

	private static int counter = 0;
	private static final String serverPublicKey = 
			"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCKFctVrhfF3m2Kes0FBL/JFeO"
			+ "cmNg9eJz8k/hQy1kadD+XFUpluRqa//Uxp2s9W2qE0EoUCu59ugcf/p7lGuL99Uo"
			+ "SGmQEynkBvZct+/M40L0E0rZ4BVgzLOJmIbXMp0J4PnPcb6VLZvxazGcmSfjauC7"
			+ "F3yWYqUbZd/HCBtawwIDAQAB"; //base64 encoded DEM i.e. PEM
	private static PublicKey key;
	

	public static String createGuestGUID() {
		long timestamp = System.currentTimeMillis();
		double random = Math.random() * Double.MAX_VALUE;
		String caps = caps();
		return sha1string(timestamp + "" + random + caps + counter++).toUpperCase();
	}
	
	public static String encrypt(String string) {
		byte[] buf = stringToBytes(string);
		
		if (key == null) {
			
			try {
				X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(serverPublicKey));
				KeyFactory kf = KeyFactory.getInstance("RSA");
				key = kf.generatePublic(spec);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			buf = cipher.doFinal(buf);
			return Base64.encodeBytes(buf);
		} catch (Exception e) {
			key = null;
			e.printStackTrace();
			return null;
		}
	}
	
	private static String sha1string(String string) {
		return step8(string);
	}
	
	private static String step8(String string) {
		return hexString(sha1(stringToBytes(string)));
	}
	
	private static String hexString(byte[] buf) {
		char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	    char[] hexChars = new char[buf.length * 2];
	    int v;
	    for ( int j = 0; j < buf.length; j++ ) {
	        v = buf[j] & 0xFF;
	        hexChars[j*2] = hexArray[v >>> 4];
	        hexChars[j*2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	private static byte[] sha1(byte[] buf) {
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
			sha1.digest(buf);
			return buf;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static byte[] stringToBytes(String string) {
		try {
			return string.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String caps() {
		return "";
	}
}