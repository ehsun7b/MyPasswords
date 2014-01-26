package com.mypasswords7.engine;

import com.mypasswords7.engine.cipher.CipherUtils;
import com.mypasswords7.engine.database.Dao;
import com.mypasswords7.engine.database.Database;
import com.mypasswords7.engine.log.LogUtils;
import com.mypasswords7.models.Entry;
import com.mypasswords7.models.Tag;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.log4j.Logger;

/**
 *
 * @author ehsun.behravesh
 */
public class Engine {

  private static Logger logger = LogUtils.getLogger(Engine.class.getSimpleName());
  private static final String DATABASE_FILE_NAME = "database.sqlite";
  private File homeDir, database;
  private String password;
  private String masterKey;

  /**
   *
   * @param home String
   * @param password
   * @throws IOException
   */
  public Engine(String home, String password) throws IOException {
    this(new File(home), password);
  }

  /**
   *
   * @param homeDir File
   * @param password
   * @throws IOException
   */
  public Engine(File homeDir, String password) throws IOException {
    this.homeDir = homeDir;
    if (!homeDir.exists() || !homeDir.isDirectory()) {
      throw new IOException("Home directory does not exist: " + homeDir.getAbsolutePath());
    }/* else if (homeDir.canWrite()) {
     throw new IOException("Home directory is not writable: " + homeDir.getAbsolutePath());
     }*/

    this.password = password;
  }

  public void init() throws SQLException, ClassNotFoundException {
    logger.info("Initializing engine ...");

    if (!databaseExists()) {
      logger.info("Database file does not exist.");
      try {
        createDatabase();
      } catch (SQLException | ClassNotFoundException ex) {
        logger.info("Error in creating tables.");
        logger.error(ex);
        throw ex;
      }
    }
  }

  private void createDatabase() throws SQLException, ClassNotFoundException {
    try (Connection connection = getConnection()) {
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

      sql = "CREATE TABLE IF NOT EXISTS SETTING (";
      sql = sql.concat("KEY TEXT NOY NULL UNIQUE,");
      sql = sql.concat("VALUE TEXT,");
      sql = sql.concat("PRIMARY KEY(KEY)");
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
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      tag = dao.insert(conn, tag);
      conn.commit();
    }

    return tag;
  }

  public Entry insert(Entry entry, Tag[] tags) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException {
    entry = encrypt(entry);

    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      entry = dao.insert(conn, entry, tags);
      conn.commit();
    }

    return entry;
  }

  public int countOfTags() throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      return dao.countOfTags(conn);
    }
  }

  public int countOfEntries() throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      return dao.countOfEntries(conn);
    }
  }

  public Entry delete(Entry entry) throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      Entry entry1 = dao.delete(conn, entry);
      conn.commit();
      return entry1;
    }
  }

  public void delete(int id) throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      dao.delete(conn, id);
      conn.commit();
    }
  }

  public Entry load(int id) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      Entry entry = dao.load(conn, id);
      if (entry != null) {
        return decrypt(entry);
      }
      return null;
    }
  }

  private Entry encrypt(Entry entry) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException {
    if (masterKey == null) {
      masterKey = CipherUtils.getKey(password);
    }

    if (entry.getUsername() != null) {
      entry.setUsername(CipherUtils.encrypt(masterKey, entry.getUsername()));
    }
    if (entry.getPassword() != null) {
      entry.setPassword(CipherUtils.encrypt(masterKey, entry.getPassword()));
    }
    if (entry.getIp() != null) {
      entry.setIp(CipherUtils.encrypt(masterKey, entry.getIp()));
    }
    if (entry.getUrl() != null) {
      entry.setUrl(CipherUtils.encrypt(masterKey, entry.getUrl()));
    }

    return entry;
  }

  private Entry decrypt(Entry entry) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    if (masterKey == null) {
      masterKey = CipherUtils.getKey(password);
    }

    if (entry.getUsername() != null) {
      entry.setUsername(CipherUtils.decrypt(masterKey, entry.getUsername()));
    }
    if (entry.getPassword() != null) {
      entry.setPassword(CipherUtils.decrypt(masterKey, entry.getPassword()));
    }
    if (entry.getIp() != null) {
      entry.setIp(CipherUtils.decrypt(masterKey, entry.getIp()));
    }
    if (entry.getUrl() != null) {
      entry.setUrl(CipherUtils.decrypt(masterKey, entry.getUrl()));
    }

    return entry;
  }

  public Tag[] loadTagsByEntryId(int id) throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      return dao.loadTagsByEntryId(conn, id);
    }
  }

  public Entry update(Entry entry, Tag[] tags) throws NoSuchAlgorithmException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, SQLException, ClassNotFoundException {
    entry = encrypt(entry);

    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      conn.setAutoCommit(false);
      Dao dao = new Dao();
      entry = dao.update(conn, entry, tags);
      conn.commit();
    }

    return entry;
  }

  public List<Entry> entries() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      List<Entry> result = dao.entries(conn);

      for (Entry entry : result) {
        if (entry != null) {
          entry = decrypt(entry);
        }
      }

      return result;
    }
  }

  public List<Tag> tags() throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      List<Tag> result = dao.tags(conn);

      return result;
    }
  }

  public List<Entry> search(String where, String order) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      List<Entry> result = dao.search(conn, where, order);

      for (Entry entry : result) {
        if (entry != null) {
          entry = decrypt(entry);
        }
      }

      return result;
    }
  }

  public void setSetting(String key, String value) throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      dao.setSetting(conn, key, value);
    }
  }
  
  public void setSetting(String key, Object value) throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      dao.setSetting(conn, key, value.toString());
    }
  }

  public String getSetting(String key) throws SQLException, ClassNotFoundException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      return dao.getSetting(conn, key);
    }
  }

  public void setSettingEncrypted(String key, String value) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();

      if (masterKey == null) {
        masterKey = CipherUtils.getKey(password);
      }

      String encryptedValue = CipherUtils.encrypt(masterKey, value);

      dao.setSetting(conn, key, encryptedValue);
    }
  }

  public String getSettingDecrypted(String key) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();
      String value = dao.getSetting(conn, key);

      if (value != null) {

        if (masterKey == null) {
          masterKey = CipherUtils.getKey(password);
        }

        return CipherUtils.decrypt(masterKey, value);
      }

      return null;
    }
  }

  public void setSettingSHA256(String key, String value) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    try (Connection conn = Database.INSTANCE.getConnection(database)) {
      Dao dao = new Dao();

      String hashedValue = CipherUtils.SHA256(value);

      dao.setSetting(conn, key, hashedValue);
    }
  }

  public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, BadPaddingException {
    Engine engine = new Engine(".", "");
    engine.init();
    System.out.println("Count of tags: " + engine.countOfTags());
    System.out.println("Count of entries: " + engine.countOfEntries());

    Entry entry = engine.load(1);
    System.out.println(entry.getPassword());

  }
}
