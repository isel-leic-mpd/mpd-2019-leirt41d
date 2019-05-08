package exam_1617_v1;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Questions {
    public static List<String>
    getArtistsNamesSortedAlphabetically(Stream<Album> albums) {
        return  albums
                .flatMap(Album::getArtists)
                .map(Artist::getName)
                .sorted()
                .collect(toList());
    }

    private static class ArtistNameTrackPair {
        private final String artistName;
        private final int trackDuration;

        ArtistNameTrackPair(String name, int duration) {
            artistName = name;
            trackDuration = duration;
        }

        String getArtistName() { return artistName; }
        int getTrackDuration() { return trackDuration; }
    }

    public static Map<String, Integer> getArtistTrackDuration(Stream<Album> albums) {
        return albums
            .flatMap( album -> album.getTracks())
            .flatMap(t ->  t.getArtists()
                        .map(a -> new ArtistNameTrackPair(a.getName(), t.getDuration()) ))
            .collect(groupingBy(ArtistNameTrackPair::getArtistName,
                    summingInt(ArtistNameTrackPair::getTrackDuration)));
    }

}
