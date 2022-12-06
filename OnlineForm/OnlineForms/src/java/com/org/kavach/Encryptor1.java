package com.org.kavach;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;

public class Encryptor1 {

    private static final byte initVector[] = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};

    public static String encrypt(String value, String key) {
        try {

            byte[] vv = value.getBytes();
            int l = vv.length + 16 - (vv.length % 16);
            byte[] vv2 = new byte[l];

            for (int i = 0; i < vv.length; i++) {
                vv2[i] = vv[i];
            }

            for (int i = vv.length; i < vv2.length; i++) {
                vv2[i] = 0x00;
            }

            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(vv2);
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        return null;
        return "";
    }

	public static String decrypt(String encrypted, String key) {
		if (encrypted != null && !encrypted.isEmpty()) {
			//System.out.println("Decrypting the string :: "+encrypted);
			try {
				IvParameterSpec iv = new IvParameterSpec(initVector);
				SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES/CBC/NOPADDING");
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
				byte[] vv = Base64.decodeBase64(encrypted);
				byte[] original = cipher.doFinal(vv);
				return new String(original).trim();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
//		return null;
		return "";
	}

}
