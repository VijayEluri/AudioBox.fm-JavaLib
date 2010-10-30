package fm.audiobox.sync.test.mocks.fixtures;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Fixtures {

    private static Log log = LogFactory.getLog(Fixtures.class);
    
    public static final String LOGIN = "login";
    public static final String RIGHT_PASS = "right_pass";
    public static final String USERNAME = "username";
    public static final String WRONG_PASS = "wrong_pass";

    public static final String INACTIVE_LOGIN = "inactive_login";
    public static final String INACTIVE_RIGHT_PASS = "inactive_right_pass";
    public static final String INACTIVE_USERNAME = "inactive_username";
    public static final String INACTIVE_WRONG_PASS = "inactive_wrong_pass";
    
    public static final String UPLOAD_TEST_FILE = "upload_test_file";
    public static final String DOWNLOAD_TEST_FILE = "download_test_file";
    public static final String TRACK_TO_DOWNLOAD = "track_to_download";
    
    public static final String SCAN_FOLDER = "scan_folder";
    
    private static Properties ps = new Properties();
    
    static {
    	try {
            
            ps.load( Fixtures.class.getResourceAsStream("/fixtures.properties") );
            
        } catch (FileNotFoundException e) {
            log.error("Environment properties file not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Unable to access the environment properties file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return ps.getProperty(key);
    }

}
