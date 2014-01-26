package com.mypasswords7.gui.embeddedweb;

import com.google.gson.Gson;
import com.mypasswords7.engine.Engine;
import com.mypasswords7.gui.embeddedweb.response.CountResponse;
import com.mypasswords7.gui.embeddedweb.response.DeleteEntryResponse;
import com.mypasswords7.gui.embeddedweb.response.EngineInfo;
import com.mypasswords7.gui.embeddedweb.response.EnginesStatusResponse;
import com.mypasswords7.gui.embeddedweb.response.EntriesResponse;
import com.mypasswords7.gui.embeddedweb.response.InsertEntryResponse;
import com.mypasswords7.gui.embeddedweb.response.ReadEntryResponse;
import com.mypasswords7.gui.embeddedweb.response.Response;
import com.mypasswords7.gui.embeddedweb.response.TagsResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import com.mypasswords7.models.Entry;
import com.mypasswords7.models.Tag;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author ehsun.behravesh
 */
public class WebHandler implements HttpHandler {

  private static final int BUFFER_SIZE = 2048;

  private Engine engine;
  private static final String DATABASES_DIR = "databases";
  private static final String DEFAULT_PASSWORD = "123";

  public WebHandler() {
    /*
     File home = new File(".");
     try {
     engine = new Engine(home, "123");
     engine.init();
     } catch (IOException ex) {
     System.out.println("Error in initializing the ENGINE." + ex.getMessage());
     }*/
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath().toLowerCase();

    if (requestURI.getPath().equalsIgnoreCase("/")) {
      responseHome(exchange);
    } else if (requestURI.getPath().endsWith(".js")) {
      loadJS(exchange);
    } else if (requestURI.getPath().endsWith(".css")) {
      loadCSS(exchange);
    } else if (requestURI.getPath().startsWith("/entry")) {
      entryRequest(exchange);
    } else if (requestURI.getPath().startsWith("/count")) {
      countRequest(exchange);
    } else if (requestURI.getPath().equalsIgnoreCase("/entries")) {
      entriesRequest(exchange);
    } else if (requestURI.getPath().equalsIgnoreCase("/tags")) {
      tagsRequest(exchange);
    } else if (path.endsWith(".png") || path.endsWith(".gif") || path.endsWith(".jpg") || path.endsWith(".jpeg")) {
      loadImage(exchange);
    } else if (requestURI.getPath().equalsIgnoreCase("/database")) {
      databasesStatusRequest(exchange);
    } else {
      fourOFour(exchange);
    }

    /*
     if (requestMethod.equalsIgnoreCase("GET")) {
     Headers responseHeaders = exchange.getResponseHeaders();
     responseHeaders.set("Content-Type", "text/plain");
     exchange.sendResponseHeaders(200, 0);
     try (OutputStream responseBody = exchange.getResponseBody()) {
     Headers requestHeaders = exchange.getRequestHeaders();
     Set<String> keySet = requestHeaders.keySet();
     Iterator<String> iter = keySet.iterator();
     while (iter.hasNext()) {
     String key = iter.next();
     List values = requestHeaders.get(key);
     String s = key + " = " + values.toString() + "\n";
     responseBody.write(s.getBytes());
     }
     responseBody.write("requestURI: ".concat(requestURI.getPath()).getBytes("UTF-8"));
     }
     }*/
  }

  private void responseHome(HttpExchange exchange) throws IOException {
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/html");
      exchange.sendResponseHeaders(200, 0);

      try (OutputStream os = exchange.getResponseBody()) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("page/home.html")) {
          byte[] buffer = new byte[BUFFER_SIZE];
          int len;

          while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
          }
        }
      }

      exchange.getResponseBody().close();

    } else {
      exchange.sendResponseHeaders(405, 0);
      exchange.getResponseBody().close();
    }
  }

  private void loadJS(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
      String path = requestURI.getPath().toLowerCase();
      String script = "js/" + path.substring(path.lastIndexOf("/") + 1);

      if (script != null) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/javascript");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
          try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(script)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;

            while ((len = is.read(buffer)) > 0) {
              os.write(buffer, 0, len);
            }
          }
        }

        exchange.getResponseBody().close();
      }
    } else {
      exchange.sendResponseHeaders(405, 0);
      exchange.getResponseBody().close();
    }
  }

  private void loadCSS(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
      String path = requestURI.getPath().toLowerCase();
      String css = "css/" + path.substring(path.lastIndexOf("/") + 1);

      if (requestURI.getRawPath().endsWith("main.css")) {
        css = "css/main.css";
      }

      if (css != null) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/css");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
          try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(css)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;

            while ((len = is.read(buffer)) > 0) {
              os.write(buffer, 0, len);
            }
          }
        }

        exchange.getResponseBody().close();
      }
    } else {
      exchange.sendResponseHeaders(405, 0);
      exchange.getResponseBody().close();
    }
  }

  private void entryRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    Headers responseHeaders = exchange.getResponseHeaders();
    responseHeaders.set("Content-Type", "application/json");
    try {
      exchange.sendResponseHeaders(200, 0);
    } catch (IOException ex) {
      System.out.println("IOException during sending headers. " + ex.getMessage());
    }

    Gson gson = new Gson();
    Response response = null;

    if (method.equalsIgnoreCase("POST")) {
      response = new InsertEntryResponse(true);

      try (InputStream is = exchange.getRequestBody()) {
        StringBuilder builder = new StringBuilder();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;

        while ((len = is.read(buffer)) > 0) {
          buffer = Arrays.copyOf(buffer, len);
          builder.append(new String(buffer, "UTF-8"));
        }

        String json = builder.toString();
        Entry entry = gson.fromJson(json, Entry.class);

        json = json.substring(json.indexOf("["), json.indexOf("]") + 1);
        Tag[] tags = gson.fromJson(json, Tag[].class);

        entry = engine.insert(entry, tags);
        if (entry.getId() > 0) {
          response.setSuccessMessage(MessageFormat.format("The entry {0} saved successfully. ID: {1}", entry.getTitle(), entry.getId()));
        } else {
          response.setSuccess(false);
          response.setErrorMessage(MessageFormat.format("The entry {0} was NOT saved.", entry.getTitle()));
        }
      } catch (IOException ex) {
        response.setSuccess(false);
        response.setErrorMessage("IO Exception in reading request content. " + ex.getMessage());
      } catch (Exception ex) {
        response.setSuccess(false);
        response.setErrorMessage("Exception in parsing request content into JSON. " + ex.getMessage());
      }
    } else if (method.equalsIgnoreCase("GET")) {
      response = new ReadEntryResponse(true);

      String path = uri.getPath();
      int lastIndexOfSlash = path.lastIndexOf("/");
      if (lastIndexOfSlash > 0 && lastIndexOfSlash < path.length() - 1) {
        String strId = path.substring(lastIndexOfSlash + 1);
        System.out.println("READ ENTRY ID: " + strId);

        int id = 0;
        try {
          id = Integer.parseInt(strId);
          Entry entry = engine.load(id);
          Tag[] tags = engine.loadTagsByEntryId(id);

          ReadEntryResponse r = (ReadEntryResponse) response;

          r.setEntry(entry);
          r.setTags(tags);
        } catch (NumberFormatException ex) {
          response.setSuccess(false);
          response.setErrorMessage("Invalid ID " + strId);
        } catch (SQLException | ClassNotFoundException ex) {
          response.setSuccess(false);
          response.setErrorMessage("Engine Exception " + ex.getMessage());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
          response.setSuccess(false);
          response.setErrorMessage("Engine Exception " + ex.getMessage());
        }
      }
    } else if (method.equalsIgnoreCase("PUT")) {
      response = new InsertEntryResponse(true);

      String path = uri.getPath();
      int lastIndexOfSlash = path.lastIndexOf("/");
      if (lastIndexOfSlash > 0 && lastIndexOfSlash < path.length() - 1) {
        String strId = path.substring(lastIndexOfSlash + 1);
        System.out.println("UPDATE ENTRY ID: " + strId);

        int id = 0;

        id = Integer.parseInt(strId);

        try (InputStream is = exchange.getRequestBody()) {
          StringBuilder builder = new StringBuilder();
          byte[] buffer = new byte[BUFFER_SIZE];
          int len;

          while ((len = is.read(buffer)) > 0) {
            buffer = Arrays.copyOf(buffer, len);
            builder.append(new String(buffer, "UTF-8"));
          }

          String json = builder.toString();
          Entry entry = gson.fromJson(json, Entry.class);
          entry.setId(id);

          json = json.substring(json.indexOf("["), json.indexOf("]") + 1);
          Tag[] tags = gson.fromJson(json, Tag[].class);

          entry = engine.update(entry, tags);
          if (entry.getId() > 0) {
            response.setSuccessMessage(MessageFormat.format("The entry {0} saved successfully. ID: {1}", entry.getTitle(), entry.getId()));
          } else {
            response.setSuccess(false);
            response.setErrorMessage(MessageFormat.format("The entry {0} was NOT saved.", entry.getTitle()));
          }
        } catch (NumberFormatException ex) {
          response.setSuccess(false);
          response.setErrorMessage("Invalid ID " + strId);
        } catch (IOException ex) {
          response.setSuccess(false);
          response.setErrorMessage("IO Exception in reading request content. " + ex.getMessage());
        } catch (Exception ex) {
          response.setSuccess(false);
          response.setErrorMessage("Exception in parsing request content into JSON. " + ex.getMessage());
        }
      }
    } else if (method.equalsIgnoreCase("DELETE")) {
      response = new DeleteEntryResponse(true);

      String path = uri.getPath();
      int lastIndexOfSlash = path.lastIndexOf("/");
      if (lastIndexOfSlash > 0 && lastIndexOfSlash < path.length() - 1) {
        String strId = path.substring(lastIndexOfSlash + 1);
        System.out.println("DELETE ENTRY ID: " + strId);

        int id = 0;
        try {
          id = Integer.parseInt(strId);
          engine.delete(id);
          response.setSuccessMessage("Entry with ID " + id + " deleted successfully.");
        } catch (NumberFormatException ex) {
          response.setSuccess(false);
          response.setErrorMessage("Invalid ID " + strId);
        } catch (SQLException | ClassNotFoundException ex) {
          response.setSuccess(false);
          response.setErrorMessage("Engine Exception " + ex.getMessage());
        }
      }
    }

    String json = gson.toJson(response);
    try {
      exchange.getResponseBody().write(json.getBytes("UTF-8"));
    } catch (IOException ex) {
      System.out.println("IO Exception! " + ex.getMessage());
    }

    try {
      exchange.getResponseBody().close();
    } catch (IOException ex) {
      System.out.println("error " + ex.getMessage());
    }
  }

  private void countRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "application/json");

      try {
        exchange.sendResponseHeaders(200, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }

      Gson gson = new Gson();
      CountResponse response = new CountResponse(true);

      String path = uri.getPath();

      if (path.endsWith("entry")) {
        try {
          response.setCount(engine.countOfEntries());
        } catch (SQLException | ClassNotFoundException ex) {
          response.setSuccess(false);
          response.setErrorMessage("SQL Exception. " + ex.getMessage());
        }
      } else if (path.endsWith("tag")) {
        try {
          response.setCount(engine.countOfTags());
        } catch (SQLException | ClassNotFoundException ex) {
          response.setSuccess(false);
          response.setErrorMessage("SQL Exception. " + ex.getMessage());
        }
      }

      String json = gson.toJson(response);
      try {
        exchange.getResponseBody().write(json.getBytes("UTF-8"));
      } catch (IOException ex) {
        System.out.println("IO Exception! " + ex.getMessage());
      }
    } else {
      try {
        exchange.sendResponseHeaders(405, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }
    }

    try {
      exchange.getResponseBody().close();
    } catch (IOException ex) {
      System.out.println("error " + ex.getMessage());
    }
  }

  private void entriesRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "application/json");

      try {
        exchange.sendResponseHeaders(200, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }

      Gson gson = new Gson();
      EntriesResponse response = new EntriesResponse(true);
      try {
        List<Entry> entries = engine.entries();
        Entry[] arr = new Entry[entries.size()];
        arr = entries.toArray(arr);
        response.setEntries(arr);
      } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
        response.setSuccess(false);
        response.setErrorMessage("Exception during read all entries. " + ex.getMessage());
      }

      String json = gson.toJson(response);
      try {
        exchange.getResponseBody().write(json.getBytes("UTF-8"));
      } catch (IOException ex) {
        System.out.println("IO Exception! " + ex.getMessage());
      }
    } else {
      try {
        exchange.sendResponseHeaders(405, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }
    }

    try {
      exchange.getResponseBody().close();
    } catch (IOException ex) {
      System.out.println("error " + ex.getMessage());
    }
  }

  private void tagsRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "application/json");

      try {
        exchange.sendResponseHeaders(200, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }

      Gson gson = new Gson();
      TagsResponse response = new TagsResponse(true);
      try {
        List<Tag> tags = engine.tags();
        Tag[] arr = new Tag[tags.size()];
        arr = tags.toArray(arr);
        response.setTags(arr);
      } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
        response.setSuccess(false);
        response.setErrorMessage("Exception during read all entries. " + ex.getMessage());
      }

      String json = gson.toJson(response);
      try {
        exchange.getResponseBody().write(json.getBytes("UTF-8"));
      } catch (IOException ex) {
        System.out.println("IO Exception! " + ex.getMessage());
      }
    } else {
      try {
        exchange.sendResponseHeaders(405, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }
    }

    try {
      exchange.getResponseBody().close();
    } catch (IOException ex) {
      System.out.println("error " + ex.getMessage());
    }
  }

  private void fourOFour(HttpExchange exchange) {
    try {
      exchange.sendResponseHeaders(404, 0);
      exchange.getResponseBody().close();
    } catch (IOException ex) {
      System.out.println("Error " + ex.getMessage());
    }
  }

  private void loadImage(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

      String path = requestURI.getPath().toLowerCase();
      String image = "img/" + path.substring(path.lastIndexOf("/") + 1);

      String mimeType = "image/png";
      if (path.endsWith("gif")) {
        mimeType = "image/gif";
      } else if (path.endsWith("jpg") || path.endsWith("jpeg")) {
        mimeType = "image/jpeg";
      }

      if (image != null) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", mimeType);
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
          try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(image)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;

            while ((len = is.read(buffer)) > 0) {
              os.write(buffer, 0, len);
            }
          }
        }

        exchange.getResponseBody().close();
      }
    } else {
      exchange.sendResponseHeaders(405, 0);
      exchange.getResponseBody().close();
    }
  }

  private void databasesStatusRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "application/json");

      try {
        exchange.sendResponseHeaders(200, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }

      Gson gson = new Gson();
      EnginesStatusResponse response = new EnginesStatusResponse(true);

      try {
        File home = new File("./" + DATABASES_DIR);

        if (home.exists() && home.isDirectory()) {
          File[] files = home.listFiles();

          ArrayList<File> dbDirectories = new ArrayList<>();

          for (File file : files) {
            if (file.isDirectory()) {
              File dbFile = new File(file, "database.sqlite");
              if (dbFile.exists()) {
                dbDirectories.add(file);
              }
            }
          }

          if (dbDirectories.size() > 0) {
            List<EngineInfo> engineInfos = new ArrayList<>();
            for (File dbDirectory : dbDirectories) {
              EngineInfo engineInfo = new EngineInfo();
              engineInfo.setEngineName(dbDirectory.getName());
              engineInfos.add(engineInfo);
            }

            EngineInfo[] engines = new EngineInfo[engineInfos.size()];
            engines = engineInfos.toArray(engines);
            response.setEngines(engines);
          } else {
            createDefaultDatabase();
            response.setSuccessMessage("Default database has been created. Password: " + DEFAULT_PASSWORD);
          }
        } else {
          createDefaultDatabase();
          response.setSuccessMessage("Default database has been created. Password: " + DEFAULT_PASSWORD);
        }
      } catch (IOException | SQLException | ClassNotFoundException ex) {
        response.setSuccess(false);
        response.setErrorMessage("Exception during checking databases. " + ex.getMessage());
      }

      String json = gson.toJson(response);
      try {
        exchange.getResponseBody().write(json.getBytes("UTF-8"));
      } catch (IOException ex) {
        System.out.println("IO Exception! " + ex.getMessage());
      }
    } else {
      try {
        exchange.sendResponseHeaders(405, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
      }
    }

    try {
      exchange.getResponseBody().close();
    } catch (IOException ex) {
      System.out.println("error " + ex.getMessage());
    }
  }

  private void createDefaultDatabase() throws IOException, SQLException, ClassNotFoundException {
    File defaultDatabase = new File(DATABASES_DIR + "/default");

    if (!defaultDatabase.exists()) {
      if (!defaultDatabase.mkdirs()) {
        throw new IOException("Can not create default database directory: " + defaultDatabase.getAbsolutePath());
      }
    }

    Engine engine1 = new Engine(defaultDatabase, DEFAULT_PASSWORD);
    engine1.init();
  }
}
