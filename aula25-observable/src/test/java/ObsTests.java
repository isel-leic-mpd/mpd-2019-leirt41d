import io.reactivex.Observable;
import org.junit.Test;

public class ObsTests {

    @Test
    public void simpleObservableTest() {
        Observable<String> obsString = Observable.create(subscriber -> {
            subscriber.onNext("Hello,");
            subscriber.onNext("World!");
            subscriber.onComplete();

        });

        obsString.subscribe(System.out::println);

        System.out.println("End!");
    }
}

