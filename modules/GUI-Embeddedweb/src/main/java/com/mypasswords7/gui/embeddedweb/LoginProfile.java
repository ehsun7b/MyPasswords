package com.mypasswords7.gui.embeddedweb;

import com.mkyong.core.OSValidator;
import com.mypasswords7.engine.Engine;
import com.mypasswords7.engine.cipher.CipherUtils;
import cz.adamh.utils.NativeUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 *
 * @author ehsun.behravesh
 */
public class LoginProfile {

  static {
    System.load("/home/ehsun7b/NetBeansProjects/MyPasswords/modules/dynamic-library/TokenGenerator/dist/Debug/GNU-Linux-x86/libTokenGenerator.so");
    /*
     try {
     System.loadLibrary("libGenerateToken");
     } catch (UnsatisfiedLinkError e) {
     try {
     if (OSValidator.isWindows()) {
     NativeUtils.loadLibraryFromJar("/lib/libGenerateToken.dll");
     } else if (OSValidator.isUnix()) {
     NativeUtils.loadLibraryFromJar("/lib/libGenerateToken.so");
     }
     } catch (IOException e1) {
     throw new RuntimeException(e1);
     }
     }*/
  }

  private String token;
  private Date date;
  private Engine engine;
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Engine getEngine() {
    return engine;
  }

  public void setEngine(Engine engine) {
    this.engine = engine;
  }

  public void genToken() throws Exception {
    if (password != null) {
      date = new Date();
      String timeStamp = CipherUtils.SHA256(date.getTime() + "");
      String pass = CipherUtils.SHA256(password);
      token = CipherUtils.SHA256(nativeGenToken(pass, timeStamp));
    } else {
      throw new Exception("Password / Date is null.");
    }
  }

  public boolean validateToken(String tok) throws NoSuchAlgorithmException, Exception {
    if (password != null && date != null) {
      //String timeStamp = CipherUtils.SHA256(date.getTime() + "");
      //String pass = CipherUtils.SHA256(password);
      //return nativeCheckToken(token, pass, timeStamp) == 1;

      return token.equalsIgnoreCase(tok);
    } else {
      throw new Exception("Password / Date is null.");
    }
  }

  private native String nativeGenToken(String password, String timeStamp);

  private native int nativeCheckToken(String token, String password, String timeStamp);

  public static void main(String[] args) throws NoSuchAlgorithmException {
    LoginProfile loginProfile = new LoginProfile();
    String password = "123";
    String timestamp = "4566";

    //System.out.println(pass.length() + " " + time.length());
    //System.out.println(pass);
    //System.out.println(time);
    for (int i = 0; i < 100; i++) {

      String pass = CipherUtils.SHA256(password);
      String time = CipherUtils.SHA256(timestamp);
      
      String token = loginProfile.nativeGenToken(pass, time);
           
      
      System.out.println(token);
    }
    //int check = loginProfile.nativeCheckToken("23", pass, time);
    //System.out.println(check);
  }
}
