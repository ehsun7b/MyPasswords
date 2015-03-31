package com.ehsunbehravesh.mypasswords.gui.embeddedweb.browser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

/**
 * Created by ehsun7b on 3/24/15.
 */
public class JxBrowserContainer extends JFrame {

  private final BrowserView browserView;
  private Browser browser;

  public JxBrowserContainer(String title, int width, int height, String url) throws HeadlessException {
    super(title);
    browser = new Browser();

    browserView = new BrowserView(browser);

    LoggerProvider.getBrowserLogger().setLevel(Level.SEVERE);
    LoggerProvider.getIPCLogger().setLevel(Level.SEVERE);
    LoggerProvider.getChromiumProcessLogger().setLevel(Level.SEVERE);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(browserView, BorderLayout.CENTER);
    frame.setSize(width, height);
    frame.setMinimumSize(new Dimension(width, height));
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    browser.loadURL(url);
  }
}
