/**
 * 
 */
package fm.audiobox.api;


import java.net.SocketException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.api.exceptions.LoginException;
import fm.audiobox.api.exceptions.ServiceException;
import fm.audiobox.api.mocks.fixtures.UserFixture;
import fm.audiobox.api.mocks.models.User;

/**
 * @author keytwo
 *
 */
public class UserTest extends junit.framework.TestCase {

    AudioBoxClient abc;
    User user;
    
    @Before
    public void setUp() throws Exception {
        AudioBoxClient.setCustomModelsPackage(User.class.getPackage().getName());
        AudioBoxClient.setUserClass(User.class);
        abc = new AudioBoxClient();
    }
    
    @Test
    public void testPreconditions() {
        assertNotNull( abc );
    }

    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongPassword() {
        
        try {
            user = (User) abc.login( UserFixture.LOGIN , UserFixture.WRONG_PASS );
        } catch (LoginException e) {
           assertEquals( 401, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testLoginShouldThrowsLoginExceptionOnWrongCredentials() {
        
        try {
            user = (User) abc.login( "wrong_user" , UserFixture.WRONG_PASS );
        } catch (LoginException e) {
           assertEquals( 401, e.getErrorCode());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testUserShouldBePopulatedWithRightCredentials() {
        loginCatched();
        assertNotNull( user.getTest() );
        assertEquals("test", user.getTest() );
        
        assertNotNull( user.getProfile() );
        assertNotNull( user.getProfile().getName() );
        assertEquals( UserFixture.USERNAME , user.getUsername());
    }

    
    @Test
    public void testUserLoginFailsOnUserNotActive() {
        
        try {
            loginInactiveUser();
        } catch (LoginException e) {
            assertEquals( LoginException.INACTIVE_USER_STATE, e.getErrorCode() );
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    
    @After
    public void tearDown() throws Exception {
        
    }
    
    
    private void loginInactiveUser() throws LoginException, ServiceException {
        
    }
    
    
    private void loginCatched() {
        try {
            user = (User) abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
    }
    
    
}
