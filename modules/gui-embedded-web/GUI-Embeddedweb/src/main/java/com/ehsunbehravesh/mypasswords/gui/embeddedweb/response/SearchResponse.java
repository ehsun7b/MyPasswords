package com.ehsunbehravesh.mypasswords.gui.embeddedweb.response;

import com.ehsunbehravesh.mypasswords.models.Entry;

/**
 *
 * @author ehsun7b
 */
public class SearchResponse extends Response {

  private Entry[] entries;

  public SearchResponse(boolean success) {
    super(success);
  }

}
