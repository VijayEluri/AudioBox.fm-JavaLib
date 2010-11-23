/**
 * 
 */
package fm.audiobox.core.test;


import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fm.audiobox.core.api.Model;
import fm.audiobox.core.exceptions.LoginException;
import fm.audiobox.core.exceptions.ModelException;
import fm.audiobox.core.models.Artist;
import fm.audiobox.core.models.Artists;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class ArtistsTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    Fixtures fx = new Fixtures();
    
    @Before
    public void setUp() throws Exception {
        
    	abc = new StaticAudioBox();
        
        user = abc.login(Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ));
    }
    
    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }

   
    @Test
    public void testArtistsShouldBePopulated() {
        loginCatched();
        try {
            
            Artists artists = user.getArtists(false);
            assertNotNull(artists);
            
            Artist artist = null;
            
            for (Model ar : artists.getCollection()) {
                Artist art = (Artist) ar;
                assertNotNull(art);
                artist = art;
            }

            assertNotNull(artist);
            
            Artist ar = (Artist) artists.get(artist.getToken());
            assertNotNull( ar );
            assertSame( ar, artist );
            
            List<Artist> list = new ArrayList<Artist>();
            artists.setCollection( list );
            
            assertNotNull( artists.getCollection() );
            assertSame( list, artists.getCollection() );
            
            ar = (Artist) artists.get(ar.getToken());
            assertNull( ar );

        } catch (ModelException e) {
            fail( e.getMessage() );
        }
    }
    
    @Test
    public void testArtistshouldBePopulatedAndContainsTracks() {
        loginCatched();
        try {
            
            Artists artists = user.getArtists(false);
            assertNotNull(artists);
            
            Artist ar = (Artist) artists.get(0);
            assertNotNull(ar);
            
            Tracks trs = (Tracks) ar.getTracks();
            assertNotNull(trs);
            
            Track tr = (Track) trs.get(0);
            assertNotNull(tr);
            
            Track tr2 = ar.getTrack( tr.getToken() );
            
            assertNotNull( tr2 );
            assertSame(tr, tr2);

        } catch (ModelException e) {
            fail( e.getMessage() );
        }
    }
    

    private void loginCatched() {
        try {
            user = abc.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ) );
        } catch (LoginException e) {
            fail(e.getMessage());
        } catch (SocketException e) {
            fail(e.getMessage());
        } catch (ModelException e) {
            fail(e.getMessage());
        }
    }
    
}
