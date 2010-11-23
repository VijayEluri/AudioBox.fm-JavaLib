/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.AudioBoxClient;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class AudioBoxClientTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    Albums albums;
    Fixtures fx = new Fixtures();
    
    @Before
    public void setUp() throws Exception {
        abc = new AudioBoxClient();
    }


    @Test
    public void testModelLoading() {
        
        assertNotNull( abc );
        loginCatched();
        assertNotNull( user );
        try {
            
            Track tr = (Track) AudioBoxClient.getModelInstance(AudioBoxClient.TRACK_KEY, null);
            assertTrue(tr instanceof Track);
            
            AudioBoxClient.setModelClassFor("ABC", null);
            AudioBoxClient.getModelInstance("ABC", null);
            
        } catch (ModelException e) {
            assertEquals( ModelException.CLASS_NOT_FOUND, e.getErrorCode());
        }
        
        try {
            
            AudioBoxClient.setModelClassFor(AudioBoxClient.TRACK_KEY, TestModel.class);
            TestModel tm = (TestModel) AudioBoxClient.getModelInstance(AudioBoxClient.TRACK_KEY, null);
            assertTrue(tm instanceof TestModel);
            
            
        } catch (ModelException e) {
            assertEquals( ModelException.INSTANTIATION_FAILED, e.getErrorCode());
            AudioBoxClient.setModelClassFor(AudioBoxClient.TRACK_KEY, null); // <-- resets the key
        }
    }


    private void loginCatched() {
        try {
            user = abc.login( Fixtures.get( Fixtures.LOGIN),  Fixtures.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (SocketException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }
    
    
    private abstract class TestModel extends Model {
        
    }
}
