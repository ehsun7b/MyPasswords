package com.mypasswords7.gui.embeddedweb;

import com.mypasswords7.engine.Engine;
import java.util.Date;

/**
 *
 * @author ehsun.behravesh
 */
public class LoginProfile {

  static {
    //System.load("<PROJECTS_ROOT>\\HelloWorldNative\\dist\\HelloWorldNative.dll");
    System.load("D:\\code\\MyPasswords\\modules\\dynamic-library\\win32\\GenerateToken\\dist\\Debug\\MinGW-Windows\\libGenerateToken.dll");
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
      String timeStamp = date.getTime() + "";
      //token = nativeGenToken(password, timeStamp);
    } else {
      throw new Exception("Password / Date is null.");
    }
  }

  private native String nativeGenToken(String password, String timeStamp);  
  
  public static void main(String[] args) {
    LoginProfile loginProfile = new LoginProfile();
    System.out.println(loginProfile.nativeGenToken("123", "456"));
    System.out.println("hello");
  }
}
