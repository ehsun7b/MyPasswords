package com.mypasswords7.gui.embeddedweb;

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
    //System.load("<PROJECTS_ROOT>\\HelloWorldNative\\dist\\HelloWorldNative.dll");    
    try {
      System.loadLibrary("libGenerateToken.dll");
    } catch (UnsatisfiedLinkError e) {
      try {
        NativeUtils.loadLibraryFromJar("/lib/libGenerateToken.dll"); // during runtime. .DLL within .JAR
      } catch (IOException e1) {
        throw new RuntimeException(e1);
      }
    }
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
    if (password != null && date != null) {
      String timeStamp = CipherUtils.SHA256(date.getTime() + "");
      String pass = CipherUtils.SHA256(password);
      token = nativeGenToken(pass, timeStamp);
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
    String pass = CipherUtils.SHA256(password);
    String time = CipherUtils.SHA256(timestamp);
    //System.out.println(pass.length() + " " + time.length());
    //System.out.println(pass);
    //System.out.println(time);
    String token = loginProfile.nativeGenToken(pass, time);

    System.out.println(token);
    int check = loginProfile.nativeCheckToken("23", pass, time);
    System.out.println(check);
  }
}
