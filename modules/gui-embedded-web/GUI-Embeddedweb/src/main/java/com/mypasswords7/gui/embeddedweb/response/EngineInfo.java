package com.mypasswords7.gui.embeddedweb.response;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ehsun7b
 */
public class EngineInfo implements Serializable {
 
  private String engineName;
  private Date firstLogin;
  private Date lastLogin;

  public EngineInfo(String name) {
    engineName = name;
  }

  public String getEngineName() {
    return engineName;
  }

  public void setEngineName(String engineName) {
    this.engineName = engineName;
  }

  public Date getFirstLogin() {
    return firstLogin;
  }

  public void setFirstLogin(Date firstLogin) {
    this.firstLogin = firstLogin;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }
  
}
