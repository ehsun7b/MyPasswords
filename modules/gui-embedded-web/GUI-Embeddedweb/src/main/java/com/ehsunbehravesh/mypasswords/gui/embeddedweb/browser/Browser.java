package com.ehsunbehravesh.mypasswords.gui.embeddedweb.browser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ehsun7b
 */
public class Browser extends JFrame {
    
    private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
    private WebView webView;
    
    private final Dimension preferedSize;
 
    private final JPanel panel = new JPanel(new BorderLayout());

    public Browser(String title, final Dimension size) {
        super(title);
        this.preferedSize = size;
        initComponents();
    }

    private void createScene() {
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
                webView = new WebView();
                engine = webView.getEngine();
                jfxPanel.setScene(new Scene(webView));
            }
        });
    }

    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override 
            public void run() {
                engine.load(url);
            }
        });
    }
    

    private void initComponents() {
        createScene();
 
        JPanel topBar = new JPanel(new BorderLayout(5, 0));
        topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
                
        panel.add(jfxPanel, BorderLayout.CENTER);
        
        getContentPane().add(panel);
        
        setPreferredSize(preferedSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }
    
    public static void main(String[] args) {
        Browser browser = new Browser("MyPasswords 3.0 Beta", new Dimension(1024, 600));
        browser.loadURL("http://google.com");
        browser.setVisible(true);
    }
}
