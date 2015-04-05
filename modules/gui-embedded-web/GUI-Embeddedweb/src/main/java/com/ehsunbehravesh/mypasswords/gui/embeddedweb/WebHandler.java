package com.ehsunbehravesh.mypasswords.gui.embeddedweb;

import com.google.gson.Gson;
import com.ehsunbehravesh.mypasswords.engine.Engine;
import com.ehsunbehravesh.mypasswords.engine.cipher.CipherUtils;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.CountResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.DeleteEntryResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.EngineInfo;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.EnginesStatusResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.EntriesResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.InsertEntryResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.LoginResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.ReadEntryResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.Response;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.SearchResponse;
import com.ehsunbehravesh.mypasswords.gui.embeddedweb.response.TagsResponse;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.sql.SQLException;
import com.ehsunbehravesh.mypasswords.models.Entry;
import com.ehsunbehravesh.mypasswords.models.Tag;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author ehsun.behravesh
 */
public class WebHandler implements HttpHandler {

  private static final int BUFFER_SIZE = 2048;

  private LoginProfile profile;
  private static final String DATABASES_DIR = "databases";
  private static final String DEFAULT_PASSWORD = "123";

  public WebHandler() {

  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    String path = requestURI.getPath().toLowerCase();

    if (requestURI.getPath().equalsIgnoreCase("/")) {
      //responseHome(exchange);
      loadHTML(exchange);
    } else if (requestURI.getPath().endsWith(".js")) {
      loadJS(exchange);
    } else if (requestURI.getPath().endsWith(".html")) {
      loadHTML(exchange);
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
      loadBinaryFile(exchange);
    } else if (requestURI.getPath().equalsIgnoreCase("/database")) {
      databasesStatusRequest(exchange);
    } else if (requestURI.getPath().equalsIgnoreCase("/login")) {
      loginRequest(exchange);
    } else if (requestURI.getPath().startsWith("/search")) {
      searchRequest(exchange);
    } else if (requestURI.getPath().endsWith(".woff")
            || requestURI.getPath().endsWith(".woff2")
            || requestURI.getPath().endsWith(".ttf")
            || requestURI.getPath().endsWith(".svg")
            || requestURI.getPath().endsWith(".eot")
            || requestURI.getPath().endsWith(".otf")) {
      loadBinaryFile(exchange);
    } else {
      fourOFour(exchange);
    }
  }

  @Deprecated
  private void responseHome(HttpExchange exchange) throws IOException {
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "text/html");
      exchange.sendResponseHeaders(200, 0);

      try (OutputStream os = exchange.getResponseBody()) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("page/index.html")) {
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
      String path = requestURI.getPath();//.toLowerCase();
      //String script = "js/" + path.substring(path.lastIndexOf("/") + 1);
      String script = path.startsWith("/") ? path.substring(1) : path;

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
      String path = requestURI.getPath();//.toLowerCase();
      String css = path.startsWith("/") ? path.substring(1) : path;

      /*
      if (requestURI.getRawPath().endsWith("main.css")) {
        css = "css/main.css";
      }*/

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
    responseHeaders.set("Content-Type", "application/json; charset=UTF-8");

    Gson gson = new Gson();
    Response response = null;

    if (method.equalsIgnoreCase("POST")) {
      response = new InsertEntryResponse(true);

      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          profile.genToken();
          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

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

            Tag[] tags = new Tag[0];
            if (json.indexOf("[") > 0) {
              json = json.substring(json.indexOf("["), json.indexOf("]") + 1);
              tags = gson.fromJson(json, Tag[].class);
            }

            entry = profile.getEngine().insert(entry, tags);
            if (entry.getId() > 0) {
              ((InsertEntryResponse) response).setId(entry.getId());
              response.setSuccessMessage(MessageFormat.format("The entry {0} saved successfully.", entry.getTitle()));
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

        } else {
          try {
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
      }
    } else if (method.equalsIgnoreCase("GET")) {
      Headers requestHeaders = exchange.getRequestHeaders();
      response = new ReadEntryResponse(true);
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          profile.genToken();
          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

          String path = uri.getPath();
          int lastIndexOfSlash = path.lastIndexOf("/");
          if (lastIndexOfSlash > 0 && lastIndexOfSlash < path.length() - 1) {
            String strId = path.substring(lastIndexOfSlash + 1);
            System.out.println("READ ENTRY ID: " + strId);

            int id = 0;
            try {
              id = Integer.parseInt(strId);
              Entry entry = profile.getEngine().load(id);
              Tag[] tags = profile.getEngine().loadTagsByEntryId(id);

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
        } else {
          try {
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
      }
    } else if (method.equalsIgnoreCase("PUT")) {
      response = new InsertEntryResponse(true);
      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          profile.genToken();
          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

          String path = uri.getPath();
          int lastIndexOfSlash = path.lastIndexOf("/");
          if (lastIndexOfSlash > 0 && lastIndexOfSlash < path.length() - 1) {
            String strId = path.substring(lastIndexOfSlash + 1);
            System.out.println("UPDATE ENTRY ID: " + strId);

            int id;

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

              Tag[] tags = new Tag[0];
              if (json.indexOf("[") > 0) {
                json = json.substring(json.indexOf("["), json.indexOf("]") + 1);
                tags = gson.fromJson(json, Tag[].class);
              }

              entry = profile.getEngine().update(entry, tags);
              if (entry.getId() > 0) {
                response.setSuccessMessage(MessageFormat.format("The entry {0} saved successfully.", entry.getTitle()));
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
        } else {
          try {
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
      }
    } else if (method.equalsIgnoreCase("DELETE")) {
      response = new DeleteEntryResponse(true);
      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          profile.genToken();
          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

          String path = uri.getPath();
          int lastIndexOfSlash = path.lastIndexOf("/");
          if (lastIndexOfSlash > 0 && lastIndexOfSlash < path.length() - 1) {
            String strId = path.substring(lastIndexOfSlash + 1);
            System.out.println("DELETE ENTRY ID: " + strId);

            int id = 0;
            try {
              id = Integer.parseInt(strId);
              profile.getEngine().delete(id);
              response.setSuccessMessage("Entry with ID " + id + " deleted successfully.");
            } catch (NumberFormatException ex) {
              response.setSuccess(false);
              response.setErrorMessage("Invalid ID " + strId);
            } catch (SQLException | ClassNotFoundException ex) {
              response.setSuccess(false);
              response.setErrorMessage("Engine Exception " + ex.getMessage());
            }
          }
        } else {
          try {
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
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
      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          profile.genToken();
          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          responseHeaders.set("token", profile.getToken());

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
              response.setCount(profile.getEngine().countOfEntries());
            } catch (SQLException | ClassNotFoundException ex) {
              response.setSuccess(false);
              response.setErrorMessage("SQL Exception. " + ex.getMessage());
            }
          } else if (path.endsWith("tag")) {
            try {
              response.setCount(profile.getEngine().countOfTags());
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
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
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

  private void searchRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          profile.genToken();
          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

          Gson gson = new Gson();
          SearchResponse response = new SearchResponse(true);

          String path = uri.getPath();
          String terms = path.length() > "/search".length() ? path.substring("/search/".length()) : "";

          response.setSuccessMessage(terms);

          String json = gson.toJson(response);
          try {
            exchange.getResponseBody().write(json.getBytes("UTF-8"));
          } catch (IOException ex) {
            System.out.println("IO Exception! " + ex.getMessage());
          }
        } else {
          try {
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
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
    //URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          profile.genToken();
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

          Gson gson = new Gson();
          EntriesResponse response = new EntriesResponse(true);
          try {
            List<Entry> entries = profile.getEngine().entries();
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
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
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
    //URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    Gson gson = new Gson();
    TagsResponse response = new TagsResponse(true);

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      Headers requestHeaders = exchange.getRequestHeaders();
      try {
        if (requestHeaders.containsKey("token") && profile != null && profile.validateToken(requestHeaders.getFirst("token"))) {

          responseHeaders.set("Content-Type", "application/json; charset=UTF-8");
          profile.genToken();
          responseHeaders.set("token", profile.getToken());

          try {
            exchange.sendResponseHeaders(200, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }

          try {
            List<Tag> tags = profile.getEngine().tags();
            Tag[] arr = new Tag[tags.size()];
            arr = tags.toArray(arr);
            response.setTags(arr);
          } catch (SQLException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException ex) {
            response.setSuccess(false);
            response.setErrorMessage("Exception during read all entries. " + ex.getMessage());
          }

        } else {
          try {
            exchange.sendResponseHeaders(403, 0);
          } catch (IOException ex) {
            System.out.println("IOException during sending headers. " + ex.getMessage());
          }
        }
      } catch (Exception ex) {
        System.out.println("IOException during checking token. " + ex.getMessage());
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

  private void loadBinaryFile(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

      String path = requestURI.getPath();
      String file = path.startsWith("/") ? path.substring(1) : path;//"img/" + path.substring(path.lastIndexOf("/") + 1);

      String mimeType = null;
      String lowerPath = path.toLowerCase().trim();
      
      if (lowerPath.endsWith("gif")) {
        mimeType = "image/gif";
      } else if (lowerPath.endsWith("jpg") || lowerPath.endsWith("jpeg")) {
        mimeType = "image/jpeg";
      } else if (lowerPath.endsWith("png")) {
        mimeType = "image/png";
      } else if (lowerPath.endsWith("otf")) {
        mimeType = "font/opentype";
      } else if (lowerPath.endsWith("eot")) {
        mimeType = "application/vnd.ms-fontobject";
      } else if (lowerPath.endsWith("svg")) {
        mimeType = "image/svg+xml";
      } else if (lowerPath.endsWith("ttf")) {
        mimeType = "application/octet-stream";
      } else if (lowerPath.endsWith("woff") || lowerPath.endsWith("woff2")) {
        mimeType = "font/woff2";
      } 

      if (file != null) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", mimeType);
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
          try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
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
    //URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("GET")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "application/json; charset=UTF-8");

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
              EngineInfo engineInfo = new EngineInfo(dbDirectory.getName());
              engineInfos.add(engineInfo);
            }

            EngineInfo[] engines = new EngineInfo[engineInfos.size()];
            engines = engineInfos.toArray(engines);
            response.setEngines(engines);
          } else {
            createDefaultDatabase();
            response.setSuccessMessage("Default database has been created. Password: " + DEFAULT_PASSWORD);
            response.setEngines(new EngineInfo[]{new EngineInfo("default")});

          }
        } else {
          createDefaultDatabase();
          response.setSuccessMessage("Default database has been created. Password: " + DEFAULT_PASSWORD);
          response.setEngines(new EngineInfo[]{new EngineInfo("default")});
        }
      } catch (IOException | SQLException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
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

  private void createDefaultDatabase() throws IOException, SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    File defaultDatabase = new File(DATABASES_DIR + "/default");

    if (!defaultDatabase.exists()) {
      if (!defaultDatabase.mkdirs()) {
        throw new IOException("Can not create default database directory: " + defaultDatabase.getAbsolutePath());
      }
    }

    Engine engine1 = new Engine(defaultDatabase, DEFAULT_PASSWORD);
    engine1.init();
    engine1.setSettingSHA256("password", DEFAULT_PASSWORD);
  }

  private void loginRequest(HttpExchange exchange) {
    URI uri = exchange.getRequestURI();
    String method = exchange.getRequestMethod();

    if (method.equalsIgnoreCase("POST")) {
      Headers responseHeaders = exchange.getResponseHeaders();
      responseHeaders.set("Content-Type", "application/json; charset=UTF-8");

      Gson gson = new Gson();
      LoginResponse response = new LoginResponse(true);

      StringBuilder request = new StringBuilder();
      byte[] buffer = new byte[BUFFER_SIZE];
      int len;
      try (InputStream is = exchange.getRequestBody()) {
        while ((len = is.read(buffer)) > 0) {
          request.append(new String(buffer, 0, len, "UTF-8"));
        }

        HashMap<String, String> requestMap = gson.fromJson(request.toString(), HashMap.class);

        Engine engine = new Engine(DATABASES_DIR + "/" + requestMap.get("engine"), requestMap.get("password"));
        engine.init();
        String currentPassword = engine.getSetting("password");
        String enteredPassword = requestMap.get("password").trim();
        String hashedPassword = CipherUtils.SHA256(enteredPassword);
        if (currentPassword != null && currentPassword.equals(hashedPassword)) {
          profile = new LoginProfile();
          profile.setPassword(enteredPassword);
          profile.genToken();
          profile.setEngine(engine);

          if (profile.getToken() != null) {
            responseHeaders.set("token", profile.getToken());
            response.setLoginSuccess(true);
          } else {
            response.setErrorMessage("Token generation failed.");
            response.setSuccess(false);
          }
        } else {
          response.setLoginSuccess(false);
          response.setLoginMessage("Wrong password!");
        }
      } catch (Exception ex) {
        response.setSuccess(false);
        response.setErrorMessage("Error in reading request body. " + ex.getMessage());
        profile = null;
      }

      try {
        exchange.sendResponseHeaders(200, 0);
      } catch (IOException ex) {
        System.out.println("IOException during sending headers. " + ex.getMessage());
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

  private void loadHTML(HttpExchange exchange) throws IOException {
    URI requestURI = exchange.getRequestURI();
    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
      String path = requestURI.getPath();//.toLowerCase();
      String html = path.equals("/") ? "html/index.html" : (path.startsWith("/") ? path.substring(1) : path);
      
      
      if (html != null) {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream os = exchange.getResponseBody()) {
          try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(html)) {
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
}
