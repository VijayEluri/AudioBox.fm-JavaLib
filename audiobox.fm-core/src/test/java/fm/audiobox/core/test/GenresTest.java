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
import fm.audiobox.core.models.Genre;
import fm.audiobox.core.models.Genres;
import fm.audiobox.core.models.Track;
import fm.audiobox.core.models.Tracks;
import fm.audiobox.core.models.User;
import fm.audiobox.core.test.mocks.fixtures.Fixtures;

/**
 * @author keytwo
 *
 */
public class GenresTest extends junit.framework.TestCase {

	StaticAudioBox abc;
    User user;
    
    @Before
    public void setUp() throws Exception {
    	abc = new StaticAudioBox();
        user = abc.login( Fixtures.get( Fixtures.LOGIN ), Fixtures.get( Fixtures.RIGHT_PASS ) );
    }
    
    @Test
    public void testPreconditions() {
        assertNotNull( abc );
        assertNotNull( user );
    }

   
    @Test
    public void testGenresShouldBePopulated() {
        loginCatched();
        try {
            
            Genres genres = user.getGenres(false);
            assertNotNull(genres);
            
            Genre genre = null;
            
            for (Model g : genres.getCollection()) {
                Genre gnr = (Genre) g;
                assertNotNull(gnr);
                genre = gnr;
            }

            Genre g = (Genre) genres.get(genre.getToken());
            assertNotNull( g );
            assertSame( g, genre);
            
            List<Genre> list = new ArrayList<Genre>();
            genres.setCollection( list );
            
            assertNotNull( genres.getCollection() );
            assertSame( list, genres.getCollection() );
            
            genre = (Genre) genres.get(genre.getToken());
            assertNull( genre );
            

        } catch (ModelException e) {
            fail( e.getMessage() );
        }
    }
    
    @Test
    public void testGenreShouldBePopulatedAndContainsTracks() {
        loginCatched();
        try {
            
            Genres genres = user.getGenres(false);
            assertNotNull(genres);
            
            Genre al = (Genre) genres.get(0);
            assertNotNull(al);
            
            Tracks trs = (Tracks) al.getTracks();
            assertNotNull(trs);
            
            Track tr = (Track) trs.get(0);
            assertNotNull(tr);

        } catch (ModelException e) {
            fail(e.getMessage());
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
