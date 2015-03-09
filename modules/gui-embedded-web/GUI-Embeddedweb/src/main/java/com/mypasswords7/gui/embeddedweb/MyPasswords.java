package com.mypasswords7.gui.embeddedweb;

import com.mypasswords7.gui.embeddedweb.browser.Browser;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.awt.Dimension;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;

/**
 *
 * @author ehsun.behravesh
 */
public class MyPasswords {

    private static final String versionName = "3.0 Beta";
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

            //try {
                //Browser browser = new Browser(versionName, new Dimension(1024, 600));
                //browser.loadURL("http://localhost:" + PORT);
                //browser.setVisible(true);
            //} catch (Exception e) {
                //System.out.println("Warning: " + e.getMessage());
                try {
                    openWebpage(new URI("http://localhost:6265"));
                } catch (Exception ex) {
                    System.out.println("Warning: Can not open default browser. " /*+ ex.getMessage()*/);
                }
            //}
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public static void openWebpage(URI uri) throws Exception {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        } else {
            throw new Exception("Can not get default desktop.");
        }
    }
}
