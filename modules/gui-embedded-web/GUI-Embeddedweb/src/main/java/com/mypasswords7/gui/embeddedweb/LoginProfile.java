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

  //static {
    //System.load("/home/ehsun7b/NetBeansProjects/MyPasswords/modules/dynamic-library/TokenGenerator/dist/Debug/GNU-Linux-x86/libTokenGenerator.so");
    //System.load("D:\\code\\MyPasswords\\modules\\dynamic-library\\TokenGenerator\\dist\\Debug\\MinGW-Windows\\libTokenGenerator.dll");    
    //System.load("/Users/ehsun7b/NetBeansProjects/MyPasswords/modules/gui-embedded-web/GUI-Embeddedweb/src/main/resources/lib/libTokenGenerator.dylib");    
      /*
    try {
      System.loadLibrary("libTokenGenerator.dll");
    } catch (UnsatisfiedLinkError e) {
      try {
        if (OSValidator.isWindows()) {
          NativeUtils.loadLibraryFromJar("/lib/libTokenGenerator64.dll");
        } else if (OSValidator.isUnix()) {
          NativeUtils.loadLibraryFromJar("/lib/libTokenGenerator.so");
        } else if (OSValidator.isMac()) {
            NativeUtils.loadLibraryFromJar("/lib/libTokenGenerator.dylib");
        }
      } catch (IOException e1) {
        throw new RuntimeException(e1);
      }
    }*/
  //}

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
      token = "123";//CipherUtils.SHA256(nativeGenToken(pass, timeStamp));
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

  //private native String nativeGenToken(String password, String timeStamp);

  //private native int nativeCheckToken(String token, String password, String timeStamp);

}
