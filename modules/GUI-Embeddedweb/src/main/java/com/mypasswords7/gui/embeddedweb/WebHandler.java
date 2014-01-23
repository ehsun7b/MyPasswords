package com.mypasswords7.gui.embeddedweb;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 *
 * @author ehsun.behravesh
 */
public class WebHandler implements HttpHandler {

  private static final int BUFFER_SIZE = 2048;
  
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String requestMethod = exchange.getRequestMethod();
    URI requestURI = exchange.getRequestURI();

    if (requestURI.getPath().equalsIgnoreCase("/")) {
      responseHome(exchange);
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

  private void responseHome(HttpExchange exchange) throws IOException {
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/html");
      exchange.sendResponseHeaders(200, 0);

      try (OutputStream os = exchange.getResponseBody()) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("home.html")) {
          byte[] buffer = new byte[BUFFER_SIZE];
          int len;
          
          while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
          }
        }
      }
      
      exchange.getResponseBody().close();

    } else {
      Headers responseHeaders = exchange.getResponseHeaders();
      exchange.sendResponseHeaders(405, 0);
      exchange.getResponseBody().close();
    }
  }

}
