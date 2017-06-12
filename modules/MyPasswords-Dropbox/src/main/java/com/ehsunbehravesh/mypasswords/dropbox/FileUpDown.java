package com.ehsunbehravesh.mypasswords.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 *
 * @author ehsun7b
 */
public class FileUpDown {

  public void uploadFile(DbxClientV2 dbxClient, File localFile, String dropboxPath) throws IOException, DbxException {
    try (InputStream in = new FileInputStream(localFile)) {
      FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
              .withMode(WriteMode.ADD)
              .withClientModified(new Date(localFile.lastModified()))
              .uploadAndFinish(in);

      System.out.println(metadata.toStringMultiline());
    }
  }

  public void download(DbxClientV2 dbxClient, File localFile, String dropboxPath) throws FileNotFoundException, IOException, DbxException {
    try (OutputStream out = new FileOutputStream(localFile)) {
      FileMetadata metadata = dbxClient.files().downloadBuilder(dropboxPath)
              .download(out);

      System.out.println(metadata.toStringMultiline());
    }
  }
}
