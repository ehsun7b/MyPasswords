package com.mypasswords7.engine;

import com.mypasswords7.engine.log.LogUtils;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author ehsun.behravesh
 */
public class Engine {  
  
  private static Logger logger = LogUtils.getLogger(Engine.class.getSimpleName());
  private File homeDir;
  
  /**
   * 
   * @param home String
   * @throws IOException 
   */
  public Engine(String home) throws IOException {    
    this(new File(home));
  }

  /**
   * 
   * @param homeDir File
   * @throws IOException 
   */
  public Engine(File homeDir) throws IOException {
    this.homeDir = homeDir;
    if (!homeDir.exists() || !homeDir.isDirectory()) {
      throw new IOException("Home directory does not exist: " + homeDir.getAbsolutePath());
    }/* else if (homeDir.canWrite()) {
      throw new IOException("Home directory is not writable: " + homeDir.getAbsolutePath());
    }*/
  }
  
  public void init() {
    logger.info("Initializing engine ...");    
  }
  
  public static void main(String[] args) throws IOException {
    Engine engine = new Engine("d:\\");
    engine.init();
  }
}
