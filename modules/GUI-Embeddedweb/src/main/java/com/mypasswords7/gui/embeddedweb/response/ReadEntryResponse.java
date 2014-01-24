package com.mypasswords7.gui.embeddedweb.response;

import com.mypasswords7.models.Entry;
import com.mypasswords7.models.Tag;

/**
 *
 * @author ehsun.behravesh
 */
public class ReadEntryResponse extends Response {

  private Entry entry;
  private Tag[] tags;

  public Entry getEntry() {
    return entry;
  }

  public void setEntry(Entry entry) {
    this.entry = entry;
  }

  public Tag[] getTags() {
    return tags;
  }

  public void setTags(Tag[] tags) {
    this.tags = tags;
  }

  public ReadEntryResponse(boolean success) {
    super(success);
  }

}
