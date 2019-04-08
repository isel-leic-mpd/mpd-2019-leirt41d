package org.isel.jingle;

import com.google.gson.Gson;
import org.isel.jingle.dto.AlbumDto;
import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.util.req.Request;


public class LastfmWebApi {
    private static final String LASTFM_API_KEY = "*****************************";
    private static final String LASTFM_HOST = "http://ws.audioscrobbler.com/2.0/";
    private static final String LASTFM_SEARCH = LASTFM_HOST
                                                    + "?method=artist.search&format=json&artist=%s&page=%d&api_key="
                                                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUMS = LASTFM_HOST
                                                    + "?method=artist.gettopalbums&format=json&mbid=%s&page=%d&api_key="
                                                    + LASTFM_API_KEY;

    private static final String LASTFM_GET_ALBUM_INFO = LASTFM_HOST
                                                    + "?method=album.getinfo&format=json&mbid=%s&api_key="
                                                    + LASTFM_API_KEY;
    private final Request request;
    protected final Gson gson;

    public LastfmWebApi(Request request) {
        this(request, new Gson());
    }

    public LastfmWebApi(Request request, Gson gson) {
        this.request = request;
        this.gson = gson;
    }

    public ArtistDto[] searchArtist(String name, int page) {

        throw new UnsupportedOperationException();
    }
    public AlbumDto[] getAlbums(String artistMbid, int page) {
        throw new UnsupportedOperationException();
    }
    public TrackDto[] getAlbumInfo(String albumMbid){
        throw new UnsupportedOperationException();
    }
}
