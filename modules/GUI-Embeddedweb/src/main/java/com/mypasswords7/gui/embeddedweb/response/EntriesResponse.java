package com.mypasswords7.gui.embeddedweb.response;

import com.mypasswords7.models.Entry;

/**
 *
 * @author ehsun.behravesh
 */
public class EntriesResponse extends Response {

  private Entry[] entries;

  public EntriesResponse(boolean success) {
    super(success);
  }

  public Entry[] getEntries() {
    return entries;
  }

  public void setEntries(Entry[] entries) {
    this.entries = entries;
  }

}
