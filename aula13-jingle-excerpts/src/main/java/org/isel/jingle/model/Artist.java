package org.isel.jingle.model;

public class Artist {
    final String name;
    final int listeners;
    final String mbid;
    final String url;
    final String image;
    final Iterable<Album> albums;
    final Iterable<Track> tracks;

    public Artist(String name, int listeners, String mbid,
                  String url, String image,
                  Iterable<Album> albums,
                  Iterable<Track> tracks) {
        this.name = name;
        this.listeners = listeners;
        this.mbid = mbid;
        this.url = url;
        this.image = image;
        this.albums = albums;
        this.tracks = tracks;
    }

    public String getName() {
        return name;
    }

    public int getListeners() {
        return listeners;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }

    public String getImage() {
        return image;
    }

    public Iterable<Album> getAlbums() {
        return albums;
    }

    public Iterable<Track> getTracks() {
        return tracks;
    }
}
