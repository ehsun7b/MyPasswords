package com.ehsunbehravesh.mypasswords.dropbox.oauth;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxHost;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.files.WriteMode;
import com.ehsunbehravesh.mypasswords.dropbox.key.KeyProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 *
 * @author ehsun7b
 */
public class Authorization {

  protected final KeyProvider keyProvider;
  protected DbxRequestConfig requestConfig;
  protected DbxAppInfo appInfo;

  public Authorization(KeyProvider keyProvider) {
    this.keyProvider = keyProvider;
  }

  private DbxAppInfo appInfo() {
    if (appInfo == null) {
      appInfo = new DbxAppInfo(String.valueOf(keyProvider.appKey()), String.valueOf(keyProvider.appSecret()));
    }

    return appInfo;
  }

  public DbxRequestConfig requestConfig() {
    if (requestConfig == null) {
      requestConfig = new DbxRequestConfig("MyPasswords7");
    }

    return requestConfig;
  }

  public AuthIntermediate startAuthentication() {
    DbxAppInfo appInfo = appInfo();
    DbxWebAuth webAuth = new DbxWebAuth(requestConfig(), appInfo);
    DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
            .withNoRedirect()
            .build();

    return new AuthIntermediate(webAuthRequest, webAuth, appInfo);
  }

  public String authUrl(AuthIntermediate intermediate) {
    DbxWebAuth webAuth = intermediate.getWebAuth();
    DbxWebAuth.Request webAuthRequest = intermediate.getWebAuthRequest();
    return webAuth.authorize(webAuthRequest);
  }

  public DbxAuthFinish finishAuthentication(AuthIntermediate intermediate) throws DbxException {
    DbxWebAuth webAuth = intermediate.getWebAuth();
    return webAuth.finishFromCode(intermediate.getCode());
  }

  private DbxAuthInfo createAuthInfo(String accessToken, DbxHost host) {
    return new DbxAuthInfo(accessToken, host);
  }

  public DbxClientV2 createClient(String accessToken, DbxHost host) {
    DbxAuthInfo authInfo = createAuthInfo(accessToken, host);
    return new DbxClientV2(requestConfig(), authInfo.getAccessToken(), authInfo.getHost());
  }  
}
