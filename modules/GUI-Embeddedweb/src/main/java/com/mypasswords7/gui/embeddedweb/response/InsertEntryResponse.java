package com.mypasswords7.gui.embeddedweb.response;

/**
 *
 * @author ehsun.behravesh
 */
public class InsertEntryResponse extends Response {

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  private Integer id;

  public InsertEntryResponse(boolean success) {
    super(success);
  }

}
