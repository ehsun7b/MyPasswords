package com.ehsunbehravesh.mypasswords.engine.log;

import org.apache.log4j.Logger;

/**
 *
 * @author ehsun.behravesh
 */
public class LogUtils {  
  public static Logger getLogger(String className) {
    return Logger.getLogger(className);
  }
}
