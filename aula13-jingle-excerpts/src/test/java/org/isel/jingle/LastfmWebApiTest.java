package org.isel.jingle;

import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;
import org.isel.jingle.util.req.Request;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


public class LastfmWebApiTest {

    @Test
    public void searchForArtistsNamedDavid(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        ArtistDto[] artists = api.searchArtist("david", 1);
        String name = artists[0].getName();
        assertEquals("David Bowie", name);
    }

    @Test
    public void getTopAlbumsFromMuse(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        ArtistDto[] artists = api.searchArtist("muse", 1);
        String mbid = artists[0].getMbid();
        AlbumDto[] albums = api.getAlbums(mbid, 1);
        assertEquals("Black Holes and Revelations", albums[0].getName());
    }

    @Test
    public void getStarlightFromBlackHolesAlbumOfMuse(){
        Request req = new BaseRequest(HttpRequest::openStream);
        LastfmWebApi api = new LastfmWebApi(req);
        ArtistDto[] artists = api.searchArtist("muse", 1);
        String mbid = artists[0].getMbid();
        AlbumDto album = api.getAlbums(mbid, 1)[0];
        TrackDto track = api.getAlbumInfo(album.getMbid())[1];
        assertEquals("Starlight", track.getName());
    }
}
