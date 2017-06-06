package com.ehsunbehravesh.mypasswords.dropbox.key;

/**
 *
 * @author ehsun7b
 */
public class KeyProviderFactory {
  
  private KeyProvider developmentKeyProvider() {
    return new DevelopmentKeyProvider();
  }
  
  private KeyProvider nativeKeyProvider() {
    return new NativeKeyProvider();
  }
  
  public KeyProvider keyProvider() {
    
    // TODO: Must replace with native key provider before release
    return developmentKeyProvider();
  }
}
