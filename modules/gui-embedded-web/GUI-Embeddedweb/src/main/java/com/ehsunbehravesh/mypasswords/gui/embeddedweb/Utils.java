package com.ehsunbehravesh.mypasswords.gui.embeddedweb;

import java.io.File;
import java.util.Properties;

/**
 *
 * @author ehsun.behravesh
 */
public class Utils {

  public static String getPathOfJar(boolean convertSpaces) {
//return "/home/ehsun7b/code/mypasswords-home/sf/new/mypasswords7-svn/source/";
    String jarFilePath = new File(Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath();
    jarFilePath = jarFilePath.substring(0, jarFilePath.lastIndexOf(getFileSeparator()) + 1);
    return convertSpaces ? jarFilePath.replaceAll("%20", "\\ ") : jarFilePath;
  }

  public static String getFileSeparator() {
    Properties sysProperties = System.getProperties();
    String fileSeparator = sysProperties.getProperty("file.separator");
    return fileSeparator;
  }
}
