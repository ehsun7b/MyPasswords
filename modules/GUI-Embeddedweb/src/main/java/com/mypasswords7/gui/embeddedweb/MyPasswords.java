package com.mypasswords7.gui.embeddedweb;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 *
 * @author ehsun.behravesh
 */
public class MyPasswords {

  private static final int PORT = 6265;
  private static final String CONTEXT_PATH = "/";

  public static void main(String[] args) {
    try {
      InetSocketAddress addrress = new InetSocketAddress(PORT);
      HttpServer server = HttpServer.create(addrress, 0);

      server.createContext(CONTEXT_PATH, new WebHandler());
      server.setExecutor(Executors.newCachedThreadPool());
      server.start();
      System.out.println("MyPasswords is running on http://localhost:" + PORT + CONTEXT_PATH);
    } catch (Exception ex) {
      System.out.println("Error: " + ex.getMessage());
    }
  }
}
