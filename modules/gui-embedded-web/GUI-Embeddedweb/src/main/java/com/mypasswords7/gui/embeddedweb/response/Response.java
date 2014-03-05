package com.mypasswords7.gui.embeddedweb.response;

import java.io.Serializable;

/**
 *
 * @author ehsun.behravesh
 */
public class Response implements Serializable {

  private String errorMessage;
  private String successMessage;
  private boolean success;

  public Response(boolean success) {
    this.success = success;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getSuccessMessage() {
    return successMessage;
  }

  public void setSuccessMessage(String successMessage) {
    this.successMessage = successMessage;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

}
