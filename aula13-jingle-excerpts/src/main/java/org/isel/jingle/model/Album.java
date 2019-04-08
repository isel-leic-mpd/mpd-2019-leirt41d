package org.isel.jingle.model;

public class Album {
    private final String name;
    private final int playcount;
    private final String mbid;
    private final String url;
    private final String image;
    private final Iterable<Track> tracks;

    public Album(String name, int playcount, String mbid, String url, String image, Iterable<Track> tracks) {
        this.name = name;
        this.playcount = playcount;
        this.mbid = mbid;
        this.url = url;
        this.image = image;
        this.tracks = tracks;
    }

    public String getName() {
        return name;
    }

    public int getPlaycount() {
        return playcount;
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

    public Iterable<Track> getTracks() {
        return tracks;
    }
}
