package com.mypasswords7.gui.embeddedweb.response;

/**
 *
 * @author ehsun7b
 */
public class EnginesStatusResponse extends Response {

  private EngineInfo[] engines;

  public EngineInfo[] getEngines() {
    return engines;
  }

  public void setEngines(EngineInfo[] engines) {
    this.engines = engines;
  }

  public EnginesStatusResponse(boolean success) {
    super(success);
  }

}
