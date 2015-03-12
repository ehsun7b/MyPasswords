package com.ehsunbehravesh.mypasswords.gui.embeddedweb.response;

import com.ehsunbehravesh.mypasswords.models.Entry;
import com.ehsunbehravesh.mypasswords.models.Tag;

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
