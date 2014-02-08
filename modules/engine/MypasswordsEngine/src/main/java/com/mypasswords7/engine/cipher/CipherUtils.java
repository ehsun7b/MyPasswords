/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mypasswords7.engine.cipher;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ehsun.behravesh
 */
public class CipherUtils {

  public static String encrypt(String key, String text) throws UnsupportedEncodingException,
          NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] byteKey = key.getBytes("UTF8");
    byte[] byteText = text.getBytes("UTF8");
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(byteKey, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    byte[] byteEncrypted = c.doFinal(byteText);
    String encrypted = new String(Base64Coder.encode(byteEncrypted));
    return encrypted;
  }

  public static byte[] encrypt(String key, byte[] byteText) throws UnsupportedEncodingException,
          NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    byte[] byteKey = key.getBytes("UTF8");
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(byteKey, "AES");
    c.init(Cipher.ENCRYPT_MODE, k);
    byte[] byteEncrypted = c.doFinal(byteText);
    return byteEncrypted;
  }

  public static String decrypt(String key, String encrypted) throws UnsupportedEncodingException,
          NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
          BadPaddingException, IOException {
    byte[] byteKey = key.getBytes("UTF8");
    byte[] byteEncrypted = Base64Coder.decode(encrypted);
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(byteKey, "AES");
    c.init(Cipher.DECRYPT_MODE, k);
    byte[] byteText = c.doFinal(byteEncrypted);
    String text = new String(byteText);
    return text;
  }

  public static byte[] decrypt(String key, byte[] byteEncrypted) throws UnsupportedEncodingException,
          NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
          BadPaddingException, IOException {
    byte[] byteKey = key.getBytes("UTF8");
    Cipher c = Cipher.getInstance("AES");
    SecretKeySpec k = new SecretKeySpec(byteKey, "AES");
    c.init(Cipher.DECRYPT_MODE, k);
    byte[] byteText = c.doFinal(byteEncrypted);
    return byteText;
  }

  private static String MD5(String message) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    final byte[] data = message.getBytes(Charset.forName("UTF8"));
    final byte[] digest = messageDigest.digest(data);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < digest.length; i++) {
      byte b = digest[i];
      result.append(Integer.toHexString(0xFF & b));
    }
    return result.toString();
  }

  public static String SHA256(String message) throws NoSuchAlgorithmException {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    final byte[] data = message.getBytes(Charset.forName("UTF8"));
    final byte[] digest = messageDigest.digest(data);
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < digest.length; i++) {
      byte b = digest[i];
      result.append(Integer.toHexString(0xFF & b));
    }
    return result.toString();
  }
  
  public static String getKey(String password) throws NoSuchAlgorithmException {    
    for (int i = 0; i < 1000; ++i) {
      password = SHA256(password);
    }
    
    return password.substring(0, 16);
  }
}
