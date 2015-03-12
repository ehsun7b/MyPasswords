package com.ehsunbehravesh.mypasswords.gui.embeddedweb.response;

import com.ehsunbehravesh.mypasswords.models.Tag;

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
