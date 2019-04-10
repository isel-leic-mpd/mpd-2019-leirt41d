package org.isel.jingle;

import org.isel.jingle.dto.ArtistDto;
import org.isel.jingle.dto.TrackDto;
import org.isel.jingle.model.Album;
import org.isel.jingle.model.Artist;
import org.isel.jingle.model.Track;
import static org.isel.jingle.util.queries.LazyQueries.*;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.HttpRequest;
import io.reactivex.Observable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.isel.jingle.util.queries.LazyQueries.iterate;

public class JingleService {

    final LastfmWebApi api;

    public JingleService(LastfmWebApi api) {
        this.api = api;
    }

    public JingleService() {
        this(new LastfmWebApi(new BaseRequest(HttpRequest::openStream)));
    }

    public Iterable<Artist> searchArtist(String name) {
        /* The original lazy operation pipeline (chain) to obtain
           the Artist sequence given a the string contained in his name
         */
        /*
        Iterable<Integer> numbers =
                iterate(1, i -> i +1);
        Iterable<ArtistDto[]> artistDtoSeq =
                map(numbers,
                        n -> api.searchArtist(name, n));

        Iterable<ArtistDto[]> limitedArtists =
                takeWhile(artistDtoSeq,
                            (ArtistDto[] a) -> a.length > 0);
        Iterable<ArtistDto> artistsDto =
                flatMap(limitedArtists,
                         a  -> from(a));
        Iterable<Artist> artists =
                map(artistsDto, this::dtoToArtist);
        */

        /*
            Alternative operation pipeline  specification
            using a call chain.
            More succinct but difficult to read...
         */
        return
            map(
                flatMap(
                    takeWhile(
                            map(
                                iterate(1, i -> i+1),
                                i -> api.searchArtist(name, i)
                            ),
                            a -> a.length > 0
                    ),
                    a -> from(a)
                ),
                this::dtoToArtist
            );

        /*
            This specification is the more compact and easier to read.
            What must we do to be able to use this notation?

            return iterate(1, i -> +1)
                    .map(i -> api.searchArtist(name, i))
                    .takeWhile(a -> a.length >= 0)
                    .flatMap( a -> from(a))
                    .map(this::dtoToArtist);
        */

    }

    /*
        Este iterador é uma versão alternativa à sequência de
        operações da versão anterior para gerar a sequência de artistas.
        É funcionalmente equivalente, mas está feitoà medida, só servindo
        este propósito, pelo que não é boa solução.
        A grande vantagem da versão anterior é consistir numa *composição* de operações
        já existentes, sem necessidade de construir iteradores à medida, como este.

        Notem: nesta versão estamos preocupados com o *algoritmo* (o como) para
        concretizar o nosso objectivo. Na versão com *composição* dizemos
        o que queremos. Dizemos que temos uma abordagem *declarativa*
        em oposição à abordagem *imperativa* desta solução
     */
    private class SearchArtistIterator
                    implements Iterator<Artist> {
        private int pageNumber;
        private final String artistName;
        private ArtistDto[] dtos;
        private int index;

        public SearchArtistIterator(
                String artistName) {
            pageNumber = 1;
            this.artistName = artistName;
        }

        @Override
        public boolean hasNext() {
            if (dtos == null || index == dtos.length) { // get more artistDto
                dtos = api.searchArtist(artistName, pageNumber++);
                index = 0;
                return dtos.length > 0;
            }
            return true;
        }

        @Override
        public Artist next() {
            if (!hasNext())
                throw new NoSuchElementException();
            // Get current ArtistDto and transform it  in Artist
            ArtistDto dto = dtos[index++];
            return dtoToArtist(dto);
        }
    }

    /*
        This searchArtist alternative returns an iterator
        made specifically to solve the problem.*Not* a good idea!
     */
    public Iterable<Artist> searchArtist2(String name) {

         return () -> new SearchArtistIterator(name);
    }

    private Iterable<Album> getAlbums(String artistMbid) {

        throw new UnsupportedOperationException();
    }

    private Iterable<Track> getAlbumTracks(String albumMbid) {
        if (albumMbid == null)
            return from(new Track[0]);
        return map(from(api.getAlbumInfo(albumMbid)),
                   this::dtoToTrack);

    }

    private Iterable<Track> getTracks(String artistMbid) {
       Iterable<Album> albums = getAlbums(artistMbid);
       Iterable<Track> tracks = flatMap(albums,
               album -> getAlbumTracks(album.getMbid()));
       return tracks;
    }
    // builders

    private Artist dtoToArtist(ArtistDto dto) {
        return new Artist(
                dto.getName(),
                dto.getListeners(),
                dto.getMbid(),
                dto.getUrl(),
                dto.getImage()[0].getImageUrl(),
                getAlbums(dto.getName()),
                getTracks(dto.getMbid())
        );
    }

    private Track dtoToTrack(TrackDto dto) {
        return null;
    }
}
