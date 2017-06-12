package com.ehsunbehravesh.mypasswords.dropbox.oauth;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxWebAuth;

/**
 *
 * @author ehsun7b
 */
public class AuthIntermediate {
  private final DbxWebAuth.Request webAuthRequest;
  private final DbxWebAuth webAuth;
  private final DbxAppInfo appInfo;
  private String code;

  public AuthIntermediate(DbxWebAuth.Request webAuthRequest, DbxWebAuth webAuth, DbxAppInfo appInfo) {
    this.webAuthRequest = webAuthRequest;
    this.webAuth = webAuth;
    this.appInfo = appInfo;
  }

  public DbxWebAuth.Request getWebAuthRequest() {
    return webAuthRequest;
  }

  public DbxWebAuth getWebAuth() {
    return webAuth;
  }

  public DbxAppInfo getAppInfo() {
    return appInfo;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

}
