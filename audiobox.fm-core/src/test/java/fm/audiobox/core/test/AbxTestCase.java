package fm.audiobox.core.test;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;

public abstract class AbxTestCase extends junit.framework.Assert {

  protected static Logger log = LoggerFactory.getLogger("AudioBox Test");
  public static final String APPLICATION_NAME = "AudioBox Tests";
  
  protected AudioBox abx;
  protected User user;
  private long startTime;
  
  @Before
  public void setup() {
    log.info("========================= Test started =========================");
    startTime = System.currentTimeMillis();
    
    abx = new AudioBox( setConfig() );
    assertNotNull(abx);
    
  }
  
  @After
  public void tearDown() {
    long total = System.currentTimeMillis() - startTime;
    log.info("===================== Test completed in " + total + " ms =====================\n");
  }
  
  
  
  protected void loginCatched() {
    try {
      user = (User) abx.login(Fixtures.get(Fixtures.LOGIN), Fixtures.get(Fixtures.RIGHT_PASS));
    } catch (LoginException e) {
      fail(e.getMessage());
    } catch (ServiceException e) {
      fail(e.getMessage());
    }
    assertNotNull(user);
  }
  
  
  protected IConfiguration setConfig() {
    IConfiguration configuration = new DefaultConfiguration( APPLICATION_NAME );

    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat( IConfiguration.ContentFormat.JSON );
    configuration.setUseCache(false);
    
    return configuration;
  }
  
}