package com.ehsunbehravesh.mypasswords.gui.embeddedweb.response;

/**
 *
 * @author ehsun.behravesh
 */
public class CountResponse extends Response {

  private int count;

  public CountResponse(boolean success) {
    super(success);
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
