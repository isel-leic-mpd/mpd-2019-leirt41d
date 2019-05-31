import io.reactivex.Observable;

public class JingleService {

    /*

    refactoring of searchArtist in order to use reactivex Observable
    public Observable<Artist> searchArtist(String name) {
        Observable<Artist> artists =
                Observable.range(1, Integer.MAX_VALUE)
                        .map(p -> api.searchArtist(name,p))
                        .flatMap(f -> Observable.fromFuture(f))
                        .takeWhile(aa -> aa.length > 0)
                        .flatMap(aa -> Observable.fromArray(aa))
                        .map(this::dtoToArtist);
        return artists;
    }
    */
}
