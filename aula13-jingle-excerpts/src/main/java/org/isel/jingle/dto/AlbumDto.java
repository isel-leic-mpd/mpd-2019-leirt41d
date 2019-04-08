package org.isel.jingle.dto;

public class AlbumDto {
    private final String name;
    private final int playcount;
    private final String mbid;
    private final String url;
    private final ImageDto[] image;
    private final TracksDto tracks;

    public AlbumDto(String name, int playcount, String mbid, String url, ImageDto[] image, TracksDto tracks) {
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

    public ImageDto[] getImage() {
        return image;
    }

    public TracksDto getTracks() {
        return tracks;
    }
}
