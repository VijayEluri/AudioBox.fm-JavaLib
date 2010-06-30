package fm.audiobox.core.test;


import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.StaticAudioBox;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.exceptions.ServiceException;
import fm.audiobox.core.models.Album;
import fm.audiobox.core.models.Playlist;
import fm.audiobox.core.models.Playlists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.test.mocks.fixtures.UserFixture;
import fm.audiobox.core.test.mocks.models.User;

public class ThreadedTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;

    @SuppressWarnings("deprecation")
    @Before
    public void setUp() throws Exception {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.impl", "debug");

        System.setProperty("org.apache.commons.logging.simplelog.log.fm.audiobox.api", "debug");

        StaticAudioBox.setModelClassFor(StaticAudioBox.USER_KEY , User.class );

        abc = new StaticAudioBox();
        abc.setForceTrust(true);
        
        try {
            user = (User) abc.login( UserFixture.LOGIN , UserFixture.RIGHT_PASS );
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }

    @Test
    public void testTrhead() throws InterruptedException {
        
        final Log logger = LogFactory.getLog(ThreadedTest.class);
        final Handle h1 = new Handle();
        final Handle h2 = new Handle();
        
        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    
                    logger.info("\nStarted thread #1\n");
                    
                    String[] tracks = user.getUploadedTracks();
                    assertNotNull(tracks);
                    assertTrue(tracks.length > 0);
                    /*
                    for (int i = 0; i < tracks.length; i++)
                        assertTrue( tracks[i].length() == 40 );
                    */
                    h1.setDone(true);
                    
                    logger.info("\nEnded thread #1\n");
                    
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (LoginException e) {
                    e.printStackTrace();
                }
            }

        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logger.info("\nStarted thread #2\n");
                    
                    Playlists pls = (Playlists) user.getPlaylists(false);
                    //pls.invoke();
                    assertNotNull(pls.getCollection());
                    
                    Playlist pl = pls.get(0);
                    //pl.invoke();
                    
                    assertNotNull(pl);
                    
                    Tracks tracks = (Tracks) pl.getTracks();
                    
                    assertNotNull(tracks);
                    
                    Track tr = tracks.get(0);
                    
                    assertNotNull(tr);
                    assertNotNull( tr.getName() );
                    assertNotNull( tr.getToken() );

                    assertNotNull( tr.getArtist() );
                    assertNotNull( tr.getArtist().getToken());

                    assertNotNull( tr.getAlbum() );
                    assertNotNull( tr.getAlbum().getToken() );

                    assertNotNull( ((Album) tr.getAlbum()).getArtist() );
                    assertNotNull( ((Album) tr.getAlbum()).getArtist().getToken() );
                    
                    h2.setDone(true);
                    
                    logger.info("\nEnded thread #2\n");
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (LoginException e) {
                    e.printStackTrace();
                } catch (ModelException e) {
                    e.printStackTrace();
                }
            }
        });

        long _start = System.currentTimeMillis();
        t1.start();
        t2.start();
        
        while ( t1.isAlive() || t2.isAlive() ) {}

        logger.info( "End: " +  (  System.currentTimeMillis() - _start  ) );
        
        assertTrue( h1.getDone() && h2.getDone() );
    }

    private class Handle {
        
        private boolean done = false;
        
        public void setDone(boolean done) {
            this.done = done;
        }
        
        public boolean getDone() {
            return this.done;
        }
    }
}
