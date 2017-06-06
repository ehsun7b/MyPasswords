package com.ehsunbehravesh.mypasswords.dropbox.key;

/**
 *
 * @author ehsun7b
 */
public class DevelopmentKeyProvider implements KeyProvider {

  @Override
  public char[] appKey() {
    return "ogvecfiqba2qvrn".toCharArray();
  }

  @Override
  public char[] appSecret() {
    return "888jp6f6ofufiyg".toCharArray();
  }
  
}
