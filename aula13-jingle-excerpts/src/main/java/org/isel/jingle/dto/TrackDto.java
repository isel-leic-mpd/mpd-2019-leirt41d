package org.isel.jingle.dto;

public class TrackDto {
    private final String name;
    private final String url;
    private final int duration;

    public TrackDto(String name, String url, int duration) {
        this.name = name;
        this.url = url;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getDuration() {
        return duration;
    }
}
