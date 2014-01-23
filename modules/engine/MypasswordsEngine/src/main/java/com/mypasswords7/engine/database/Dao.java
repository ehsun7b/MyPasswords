package com.mypasswords7.engine.database;

import com.mypasswords7.models.Entry;
import com.mypasswords7.models.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author ehsun.behravesh
 */
public class Dao {

  public int countOfTags(Connection conn) throws SQLException, ClassNotFoundException {
    int result = -1;

    String sql = "SELECT COUNT(*) FROM TAG";
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(sql);

    if (resultSet.next()) {
      result = resultSet.getInt(1);
    }

    return result;
  }

  public int countOfEntries(Connection conn) throws SQLException, ClassNotFoundException {
    int result = -1;

    String sql = "SELECT COUNT(*) FROM ENTRY";
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery(sql);

    if (resultSet.next()) {
      result = resultSet.getInt(1);
    }

    return result;
  }

  public Tag insert(Connection conn, Tag tag) throws SQLException, ClassNotFoundException {

    String sql = "INSERT INTO TAG (TITLE) VALUES (?)";

    PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    statement.setString(1, tag.getTitle());
    int rows = statement.executeUpdate();

    if (rows == 1) {
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        tag.setId(generatedKeys.getInt(1));
      }
    } else {
      throw new SQLException("Insert failed! effected rows: " + rows);
    }

    return tag;
  }

  public Entry insert(Connection conn, Entry entry, Tag[] tags) throws SQLException, ClassNotFoundException {

    String sql = "INSERT INTO ENTRY (TITLE, USERNAME, URL, PASSWORD, DESCRIPTION, IP, NOTE) VALUES (?, ?, ?, ?, ?, ?, ?)";

    PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    statement.setString(1, entry.getTitle());
    statement.setString(2, entry.getUsername());
    statement.setString(3, entry.getUrl());
    statement.setString(4, entry.getPassword());
    statement.setString(5, entry.getDescription());
    statement.setString(6, entry.getIp());
    statement.setString(7, entry.getNote());
    int rows = statement.executeUpdate();

    if (rows == 1) {
      ResultSet generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        entry.setId(generatedKeys.getInt(1));
      }

      sql = "INSERT INTO ENTRY_TAG (ENTRY_ID, TAG_ID) VALUES (?, ?)";
      PreparedStatement statement1 = conn.prepareStatement(sql);
      statement1.setInt(1, entry.getId());

      for (Tag tag : tags) {
        Tag existingTag = load(conn, tag);
        if (existingTag == null) {
          tag = insert(conn, tag);
        } else {
          tag = existingTag;
        }

        statement1.setInt(2, tag.getId());
        statement1.executeUpdate();
      }
    } else {
      throw new SQLException("Insert failed! effected rows: " + rows);
    }

    return entry;
  }

  public Entry delete(Connection conn, Entry entry) throws SQLException {
    String sql = "DELETE FROM ENTRY WHERE ID = ?";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setInt(1, entry.getId());
    int rows = statement.executeUpdate();

    if (rows == 1) {
      sql = "DELETE FROM ENTRY_TAG WHERE ENTRY_ID = ?";
      PreparedStatement statement1 = conn.prepareStatement(sql);
      statement1.setInt(1, entry.getId());
      statement1.executeUpdate();      
    } else {
      throw new SQLException("DELETE failed. rows: " + rows);
    }

    return entry;
  }

  private Tag load(Connection conn, Tag tag) throws SQLException {
    String sql = "SELECT * FROM TAG WHERE TITLE = ?";
    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setString(1, tag.getTitle());
    ResultSet resultSet = statement.executeQuery();
    
    if (resultSet.next()) {
      Tag tag1 = new Tag();
      tag1.setId(resultSet.getInt("ID"));
      tag1.setTitle(resultSet.getString("TITLE"));
      
      return tag1;
    } else {
      return null;
    }
  }
}
