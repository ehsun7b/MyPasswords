package com.mypasswords7.engine.database;

import com.mypasswords7.engine.Engine;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ehsun.behravesh
 */
public enum Database {

  INSTANCE;

  public Connection getConnection(File file) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
  }
}
