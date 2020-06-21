package com.fastcode.demopet.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fastcode.demopet.commons.logging.LoggingHelper;

/*
 * this is a decrypter method of aes 
 * for temporary purpose key is written here 
 * it uses javax crypto package
 * and uses AES/CBC/PKCS5PADDING for encryption  
 * 
 * 
 */


@Component
public class Decrypter {

//	@Value("${encryption.key}")
//	public static String key ;
	
	public static String key="qwertyuiopqwerty" ;
	public static String initVector="asdfghjklasdfghj";
	
//	@Value("${encryption.intvector}")
//	public static String initVector;


	@Autowired
	private LoggingHelper logHelper;
	
	/*
	 * it takes encoded string as a parameter 
	 * a secret key for decoding 
	 *
	 */

	public String decrypt(String encodedString) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec secret = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secret, iv);
			byte[] originalString = cipher.doFinal(Base64.decodeBase64(encodedString));
			return new String(originalString);

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException e) {
			logHelper.getLogger().error("error"+e);
			return " exception >";
		}
	}
}