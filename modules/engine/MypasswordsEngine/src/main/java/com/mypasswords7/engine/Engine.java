package com.mypasswords7.engine;

import com.mypasswords7.engine.database.Dao;
import com.mypasswords7.engine.database.Database;
import com.mypasswords7.engine.log.LogUtils;
import com.mypasswords7.models.Entry;
import com.mypasswords7.models.Tag;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author ehsun.behravesh
 */
public class Engine {  
    
  private static Logger logger = LogUtils.getLogger(Engine.class.getSimpleName());
  private static final  String DATABASE_FILE_NAME = "database.sqlite";
  private File homeDir, database;
  
  /**
   * 
   * @param home String
   * @throws IOException 
   */
  private Engine(String home) throws IOException {    
    this(new File(home));
  }

  /**
   * 
   * @param homeDir File
   * @throws IOException 
   */
  private Engine(File homeDir) throws IOException {
    this.homeDir = homeDir;
    if (!homeDir.exists() || !homeDir.isDirectory()) {
      throw new IOException("Home directory does not exist: " + homeDir.getAbsolutePath());
    }/* else if (homeDir.canWrite()) {
      throw new IOException("Home directory is not writable: " + homeDir.getAbsolutePath());
    }*/
  }
  
  public void init() {
    logger.info("Initializing engine ...");  
    
    if (!databaseExists()) {
      logger.info("Database file does not exist.");
      try {
        createDatabase();
      } catch (SQLException | ClassNotFoundException ex) {
        logger.info("Error in creating tables.");
        logger.error(ex);        
      }
    }
  }    

  private void createDatabase() throws SQLException, ClassNotFoundException {
    try(Connection connection = getConnection()) {      
      connection.setAutoCommit(false);

      String sql = "CREATE TABLE IF NOT EXISTS TAG (";
      sql = sql.concat("ID INTEGER PRIMARY KEY AUTOINCREMENT,");
      sql = sql.concat("TITLE TEXT NOT NULL UNIQUE");
      sql = sql.concat(")");

      Statement stmnt = connection.createStatement();

      stmnt.executeUpdate(sql);

      sql = "CREATE TABLE IF NOT EXISTS ENTRY (";
      sql = sql.concat("ID INTEGER PRIMARY KEY AUTOINCREMENT,");      
      sql = sql.concat("TITLE TEXT NOT NULL, ");
      sql = sql.concat("USERNAME TEXT, ");
      sql = sql.concat("URL TEXT, ");
      sql = sql.concat("DESCRIPTION TEXT, ");
      sql = sql.concat("PASSWORD TEXT, ");
      sql = sql.concat("IP TEXT, ");
      sql = sql.concat("NOTE TEXT");
      sql = sql.concat(")");

      stmnt.executeUpdate(sql);
      
      sql = "CREATE TABLE IF NOT EXISTS ENTRY_TAG (";
      sql = sql.concat("ENTRY_ID INTEGER NOT NULL,");
      sql = sql.concat("TAG_ID INTEGER NOT NULL, ");
      sql = sql.concat("PRIMARY KEY(ENTRY_ID, TAG_ID) ");
      sql = sql.concat(")");

      stmnt.executeUpdate(sql);
      
      connection.commit();
      
      logger.info("Database file was created successfully.");
    } catch (SQLException ex) {
      if (!database.delete()) {
        logger.info("Please delete the file " + database.getAbsolutePath() + " manually.");
      }
      throw ex;
    }
  }

  private boolean databaseExists() {
    database = new File(homeDir, DATABASE_FILE_NAME);
    return database.exists();
  }

  private Connection getConnection() throws SQLException, ClassNotFoundException {
    return Database.INSTANCE.getConnection(database);
  }
  
  public Tag insert(Tag tag) throws SQLException, ClassNotFoundException {
    try(Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      tag = dao.insert(conn, tag);
      conn.commit();
    }
    
    return tag;
  }
  
  public Entry insert(Entry entry, Tag[] tags) throws SQLException, ClassNotFoundException {
    try(Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      entry = dao.insert(conn, entry, tags);
      conn.commit();
    }
    
    return entry;
  }
  
  public int countOfTags() throws SQLException, ClassNotFoundException {
    try(Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      return dao.countOfTags(conn);
    }        
  }
  
  public int countOfEntries() throws SQLException, ClassNotFoundException {
    try(Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      return dao.countOfEntries(conn);
    }        
  }
  
  public Entry delete(Entry entry) throws SQLException, ClassNotFoundException {
    try(Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      Entry entry1 = dao.delete(conn, entry);
      conn.commit();
      return entry1;
    }        
  }
  
  public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
    Engine engine = new Engine(".");
    engine.init();
    System.out.println("Count of tags: " + engine.countOfTags());
    System.out.println("Count of entries: " + engine.countOfEntries());
    Tag bank = new Tag();
    bank.setTitle("BANK");
    
    Tag email = new Tag();
    email.setTitle("EMAIL");
    
    //engine.insert(bank);
    
    System.out.println("Count of tags: " + engine.countOfTags());
    
    Entry entry = new Entry();
    entry.setTitle("Gamil account");
    entry.setUrl("http://gmail.com");
    entry.setUsername("ehsun7b@gmail.com");
    
    engine.insert(entry, new Tag[] {bank, email});
  }
}
