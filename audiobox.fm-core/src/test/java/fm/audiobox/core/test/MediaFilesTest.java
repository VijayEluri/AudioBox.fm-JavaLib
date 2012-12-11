package fm.audiobox.core.test;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.MediaFile;
import fm.audiobox.core.models.MediaFiles;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.interfaces.IConfiguration;

public class MediaFilesTest extends AbxTestCase {

  @Before
  public void setUp() {
    loginCatched();
  }

  @Test
  public void mediaFilesListForCloud() {
    
    Playlists pls = user.getPlaylists();
    
    try {
      pls.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
    
    MediaFiles mediaFiles = pl.getMediaFiles();
    
    assertFalse( mediaFiles.size() == pl.getMediaFilesCount() );
    assertFalse( mediaFiles.size() > 0 );
    
    try {
      mediaFiles.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertEquals( mediaFiles.size(), pl.getMediaFilesCount() );
    assertTrue( mediaFiles.size() > 0 );
    
  }
  
  @Test
  public void allMediaFilesShouldBeCorrectlyPopulated() {
    Playlists pls = user.getPlaylists();
    MediaFiles mfs = null;
    
    try {
      pls.load(false);
      Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
      mfs = pl.getMediaFiles();
      mfs.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    for( MediaFile mf : mfs ) {
      assertSame( mf.getType(), MediaFiles.Type.AudioFile );
      assertNotNull( mf.getToken()  );
      assertNotNull( mf.getArtist()  );
      assertNotNull( mf.getAlbum()  );
      assertNotNull( mf.getGenre()  );
      assertNotNull( mf.getReleaseYear()  );
      assertNotNull( mf.getTitle()  );
      assertNotNull( mf.getLenStr()  );
      assertNotNull( mf.getLenInt()  );
      assertNotNull( mf.getPosition()  );
      assertNotNull( mf.getFilename()  );
      assertNotNull( mf.isLoved()  );
      assertNotNull( mf.getDiscNumber()  );
      assertNotNull( mf.getMime()  );
      assertEquals( mf.getSource(), MediaFile.Source.cloud.toString() );
      assertNotNull( mf.getShareToken()  );
      assertNotNull( mf.getArtwork()  );
      
    }
    
  }
  
  @Test
  public void mediaFilesShouldBeFullyPopulated() {
    Playlists pls = user.getPlaylists();
    MediaFiles mfs = null;
    
    try {
      pls.load(false);
      Playlist pl = pls.getPlaylistByType( Playlists.Type.CloudPlaylist );
      mfs = pl.getMediaFiles();
      mfs.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    String proto = abx.getConfiguration().getProtocol( IConfiguration.Connectors.NODE );
    String host = abx.getConfiguration().getHost( IConfiguration.Connectors.NODE );
    int port = abx.getConfiguration().getPort( IConfiguration.Connectors.NODE );
    String apiPath = abx.getConfiguration().getPath( IConfiguration.Connectors.NODE );
    
    String url = proto + "://" + host + ":" + port + apiPath + "/stream/";
    
    MediaFile mf = mfs.get(0);
    
    try {
      mf.load(false);
    } catch (ServiceException e) {
      fail( e.getMessage() );
    } catch (LoginException e) {
      fail( e.getMessage() );
    }
    
    assertNotNull(       mf.getType()                  );
    assertNotNull(       mf.getToken()                  );
    assertNotNull(       mf.getArtist()                  );
    assertNotNull(       mf.getAlbum()                  );
    assertNotNull(       mf.getGenre()                  );
    assertNotNull(       mf.getReleaseYear()                  );
    assertNotNull(       mf.getTitle()                  );
    assertNotNull(       mf.getLenStr()                  );
    assertNotNull(       mf.getLenInt()                  );
    assertNotNull(       mf.getPosition()                  );
    assertNotNull(       mf.getFilename()                  );
    assertNotNull(       mf.isLoved()                  );
    assertNotNull(       mf.getDiscNumber()                  );
    assertNotNull(       mf.getMime()                  );
    assertNotNull(       mf.getSource()                  );
    assertNotNull(       mf.getShareToken()                  );
    assertNotNull(       mf.getArtwork()                  );
    assertNotNull(       mf.getSize()                  );
    assertNotNull(       mf.getHash()                  );
    assertNotNull(       mf.getAudioBitrate()                  );
    assertNotNull(       mf.getAudioSampleRate()                  );
    assertNotNull(       mf.getPlays()                  );
    
//  assertNotNull(       mf.getRemotePath()                  );
//  assertNotNull(       mf.getVideoBitrate()                  );
//  assertNotNull(       mf.getVideoCodec()                  );
//  assertNotNull(       mf.getVideoResolution()                  );
//  assertNotNull(       mf.getVideoFps()                  );
//  assertNotNull(       mf.getVideoAspect()                  );
//  assertNotNull(       mf.getVideoContainer()                  );
//  assertNotNull(       mf.getAudioCodec()                  );
    
    assertEquals( url + mf.getFilename(), mf.computeStreamUrl() );
  }
  
}
