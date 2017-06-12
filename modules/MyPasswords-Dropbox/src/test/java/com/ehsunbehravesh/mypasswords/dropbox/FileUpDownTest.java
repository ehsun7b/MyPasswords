package com.ehsunbehravesh.mypasswords.dropbox;

import com.dropbox.core.DbxHost;
import com.dropbox.core.v2.DbxClientV2;
import com.ehsunbehravesh.mypasswords.dropbox.key.KeyProvider;
import com.ehsunbehravesh.mypasswords.dropbox.key.KeyProviderFactory;
import com.ehsunbehravesh.mypasswords.dropbox.oauth.Authorization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ehsun7b
 */
public class FileUpDownTest {
  
  public FileUpDownTest() {
  }

  @Test
  public void testUploadFile() throws Exception {
    String ehs_token = "o9MMPWzWozsAAAAAAAA7LcZj4A4b2LG1e6YbezbZ7ZoeJO-n-Hzu-yLZbuNFxLIS";
    
    Authorization auth = new Authorization(new KeyProviderFactory().keyProvider());
    DbxClientV2 client = auth.createClient(ehs_token, DbxHost.DEFAULT);
    
    FileUpDown upDown = new FileUpDown();    
    File file = new File(getClass().getClassLoader().getResource("test_file.txt").getFile()); 
    
    try(FileWriter w = new FileWriter(file, true); PrintWriter pw = new PrintWriter(w)) {
      pw.println(new Date());
    }    
        
    String path = "/1/test" + new Date().getTime() + ".txt";
    upDown.uploadFile(client, file, path);
    
    upDown.download(client, new File("target/downloaded_file.txt"), path);
  }

  @Test
  public void testDownload() throws Exception {
  }
  
}
