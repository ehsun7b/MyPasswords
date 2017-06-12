/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ehsunbehravesh.mypasswords.dropbox.oauth;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.ehsunbehravesh.mypasswords.dropbox.key.KeyProviderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.junit.Test;

/**
 *
 * @author ehsun7b
 */
public class AuthorizationTest {

  public AuthorizationTest() {
  }

  //@Test
  public void testSomeMethod() throws IOException, DbxException {

    Authorization a = new Authorization(new KeyProviderFactory().keyProvider());
    AuthIntermediate intermediate = a.startAuthentication();
    System.out.println(a.authUrl(intermediate));

    System.out.println("Code:");
    String code = new BufferedReader(new InputStreamReader(System.in)).readLine();
    if (code == null) {
      System.exit(1);
      return;
    }
    code = code.trim();

    intermediate.setCode(code);

    DbxAuthFinish finish = a.finishAuthentication(intermediate);
    System.out.println(finish.getAccessToken());
    System.out.println(finish.getUserId());
    System.out.println(finish.getUrlState());
    
  }

}
