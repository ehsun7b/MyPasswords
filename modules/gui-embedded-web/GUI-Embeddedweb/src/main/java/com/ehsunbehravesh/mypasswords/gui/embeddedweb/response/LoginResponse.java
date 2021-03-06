package com.ehsunbehravesh.mypasswords.gui.embeddedweb.response;

/**
 *
 * @author ehsun.behravesh
 */
public class LoginResponse extends Response {
  
  private boolean loginSuccess;
  private String loginMessage;
  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getLoginMessage() {
    return loginMessage;
  }

  public void setLoginMessage(String loginMessage) {
    this.loginMessage = loginMessage;
  }

  public boolean isLoginSuccess() {
    return loginSuccess;
  }

  public void setLoginSuccess(boolean loginSuccess) {
    this.loginSuccess = loginSuccess;
  }

  public LoginResponse(boolean success) {
    super(success);
  }

}
