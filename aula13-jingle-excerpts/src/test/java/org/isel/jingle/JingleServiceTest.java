package org.isel.jingle;

import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;
import org.junit.Test;

import java.io.InputStream;
import java.util.function.Function;

import static junit.framework.Assert.assertEquals;
import static org.isel.jingle.util.queries.LazyQueries.count;
import static org.isel.jingle.util.queries.LazyQueries.first;
import static org.isel.jingle.util.queries.LazyQueries.last;
import static org.isel.jingle.util.queries.LazyQueries.limit;
import static org.isel.jingle.util.queries.LazyQueries.skip;

public class JingleServiceTest {
    static class HttpGet implements Function<String, InputStream> {
        int count = 0;
        @Override
        public InputStream apply(String path) {
            System.out.println("Requesting..." + ++count);
            return HttpRequest.openStream(path);
        }
    }

    @Test
    public void searchHiperAndCountAllResults() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Iterable<Artist> artists = service.searchArtist("hiper");
        assertEquals(0, httpGet.count);
        assertEquals(700, count(artists));
        assertEquals(25, httpGet.count);
        Artist last = last(artists);
        assertEquals("Coma - Hipertrofia.(2008)", last.getName());
        assertEquals(50, httpGet.count);
    }

    @Test
    public void getFirstAlbumOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Iterable<Artist> artists = service.searchArtist("muse");
        assertEquals(0, httpGet.count);
        Artist muse = first(artists);
        assertEquals(1, httpGet.count);
        Iterable<Album> albums = muse.getAlbums();
        assertEquals(1, httpGet.count);
        Album first = first(albums);
        assertEquals(2, httpGet.count);
        assertEquals("Black Holes and Revelations", first.getName());
    }

    @Test
    public void get111AlbumsOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Artist muse = first(service.searchArtist("muse"));
        Iterable<Album> albums = limit(muse.getAlbums(), 111);
        assertEquals(111, count(albums));
        assertEquals(4, httpGet.count); // 1 for artist + 3 pages of albums each with 50 albums
    }

    @Test
    public void getSecondSongFromBlackHolesAlbumOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Album blackHoles = first(first(service.searchArtist("muse")).getAlbums());
        assertEquals(2, httpGet.count); // 1 for artist + 1 page of albums
        assertEquals("Black Holes and Revelations", blackHoles.getName());
        Track song = first(skip(blackHoles.getTracks(), 1));
        assertEquals(3, httpGet.count); // + 1 to getTracks
        assertEquals("Starlight", song.getName());
    }

    @Test
    public void get42thTrackOfMuse() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Iterable<Track> tracks = first(service.searchArtist("muse")).getTracks();
        assertEquals(1, httpGet.count); // 1 for artist + 0 for tracks because it fetches lazily
        Track track = first(skip(tracks, 42)); // + 1 to getAlbums + 4 to get tracks of first 4 albums.
        assertEquals("MK Ultra", track.getName());
        assertEquals(6, httpGet.count);
    }
    @Test
    public void getLastTrackOfMuseOf500() {
        HttpGet httpGet = new HttpGet();
        JingleService service = new JingleService(new LastfmWebApi(new BaseRequest(httpGet)));
        Iterable<Track> tracks = limit(first(service.searchArtist("muse")).getTracks(), 500);
        assertEquals(500, count(tracks));
        assertEquals(78, httpGet.count); // Each page has 50 albums => 50 requests to get their tracks. Some albums have no tracks.
    }
}
