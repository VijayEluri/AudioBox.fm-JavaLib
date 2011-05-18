package fm.audiobox.core.test;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.ContentFormat;

public abstract class AudioBoxTestCase extends junit.framework.Assert {

  protected static Logger log = Logger.getLogger("AudioBox Test");
  
  protected AudioBox abc;
  protected User user;
  private long startTime;
  
  @Before
  public void setup() {
    log.info("========================= Test started =========================");
    startTime = System.currentTimeMillis();
    
    IConfiguration configuration = new DefaultConfiguration("My test application");

    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(ContentFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    abc = new AudioBox(configuration);
    assertNotNull(abc);
    
  }
  
  @After
  public void tearDown() {
    long total = System.currentTimeMillis() - startTime;
    log.info("===================== Test completed in " + total + " ms =====================\n");
  }
  
  
  protected void loginCatched() {
    try {
      user = (User) abc.login(Fixtures.get(Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS));
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    assertNotNull(user);
  }
}
