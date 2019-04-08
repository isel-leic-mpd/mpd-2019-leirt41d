package org.isel.jingle.dto;

public class ArtistDto {
    final String name;
    final int listeners;
    final String mbid;
    final String url;
    final ImageDto[] image;

    public ArtistDto(String name, int listeners, String mbid, String url, ImageDto[] image) {
        this.name = name;
        this.listeners = listeners;
        this.mbid = mbid;
        this.url = url;
        this.image = image;
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

    public ImageDto[] getImage() {
        return image;
    }
}
