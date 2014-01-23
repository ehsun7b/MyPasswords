package com.mypasswords7.gui.embeddedweb;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ehsun.behravesh
 */
public class WebHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String requestMethod = exchange.getRequestMethod();
    URI requestURI = exchange.getRequestURI();
    
    if (requestURI.getPath().equalsIgnoreCase("/")) {
      responseHome();
    }

    /*
    if (requestMethod.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/plain");
      exchange.sendResponseHeaders(200, 0);
      try (OutputStream responseBody = exchange.getResponseBody()) {
        Headers requestHeaders = exchange.getRequestHeaders();
        Set<String> keySet = requestHeaders.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
          String key = iter.next();
          List values = requestHeaders.get(key);
          String s = key + " = " + values.toString() + "\n";
          responseBody.write(s.getBytes());
        }
        responseBody.write("requestURI: ".concat(requestURI.getPath()).getBytes("UTF-8"));
      }
    }*/
  }

  private void responseHome() {
    
  }

}
