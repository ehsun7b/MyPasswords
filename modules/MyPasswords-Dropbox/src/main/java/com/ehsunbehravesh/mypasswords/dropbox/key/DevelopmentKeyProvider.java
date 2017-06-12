package com.ehsunbehravesh.mypasswords.dropbox.key;

/**
 *
 * @author ehsun7b
 */
public class DevelopmentKeyProvider implements KeyProvider {

  @Override
  public char[] appKey() {
    return "xhyqa5qhwzyb2me".toCharArray();
  }

  @Override
  public char[] appSecret() {
    return "jyv8gqc2pazs4yg".toCharArray();
  }
  
}
