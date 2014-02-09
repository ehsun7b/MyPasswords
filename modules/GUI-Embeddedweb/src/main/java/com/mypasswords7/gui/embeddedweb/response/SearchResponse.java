package com.mypasswords7.gui.embeddedweb.response;

import com.mypasswords7.models.Entry;

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
