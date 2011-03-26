package fm.audiobox.core;

import org.junit.Test;

import fm.audiobox.AudioBox;
import fm.audiobox.configurations.DefaultConfiguration;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Albums;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;
import fm.audiobox.interfaces.IConfiguration;
import fm.audiobox.interfaces.IConfiguration.RequestFormat;

public class AudioBoxRefactorTest extends junit.framework.TestCase {

  
  @Test
  public void testUserIsLoggedIn() {
    
    
    IConfiguration configuration = new DefaultConfiguration("My test application");
    
    configuration.setVersion(1, 0, 0);
    configuration.setRequestFormat(RequestFormat.XML);
    configuration.setShortResponse(false);
    configuration.setUseCache(false);
    
    AudioBox abx = new AudioBox(configuration);
    
    
    User user = null;
    try {
      user = abx.login( Fixtures.get( Fixtures.LOGIN ),  Fixtures.get( Fixtures.RIGHT_PASS ));
    } catch (LoginException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
    
    assertNotNull(user);
    
    assertEquals(user.getUsername(), "fatshotty");
    
    Playlists pls = user.getPlaylists();
    Artists arts = user.getArtists();
    Genres gnrs = user.getGenres();
    Albums albs = user.getAlbums();
    
    assertNotNull(pls);
    assertNotNull(arts);
    assertNotNull(albs);
    assertNotNull(gnrs);
    
    
    // Loading playlists
    try {
      pls.load();
    } catch (ServiceException e) {
      assertNull( e );
    } catch (LoginException e) {
      assertNull( e );
    }
    
    // Loading genres
    try {
      gnrs.load();
    } catch (ServiceException e) {
      assertNull( e );
    } catch (LoginException e) {
      assertNull( e );
    }
    
    // Loading artists
    try {
      arts.load();
    } catch (ServiceException e) {
      assertNull( e );
    } catch (LoginException e) {
      assertNull( e );
    }
    
    
    // Loading albums
    try {
      albs.load();
    } catch (ServiceException e) {
      assertNull( e );
    } catch (LoginException e) {
      assertNull( e );
    }
    
    
    // All went right
    assertTrue( true );
    
  }
  
}
