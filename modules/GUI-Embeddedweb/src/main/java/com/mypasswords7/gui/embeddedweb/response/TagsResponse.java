package com.mypasswords7.gui.embeddedweb.response;

import com.mypasswords7.models.Tag;

/**
 *
 * @author ehsun.behravesh
 */
public class TagsResponse extends Response {

  private Tag[] tags;

  public Tag[] getTags() {
    return tags;
  }

  public void setTags(Tag[] tags) {
    this.tags = tags;
  }
  
  public TagsResponse(boolean success) {
    super(success);
  }
  
}
