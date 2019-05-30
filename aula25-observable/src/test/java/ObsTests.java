import io.reactivex.Observable;
import observables.ObsUtils;
import org.junit.Test;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;

public class ObsTests {

    private CompletableFuture<Long> myTimer(long delay) {
        return CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(delay); } catch(InterruptedException e) {}
            return delay;
        });
    }

    @Test
    public void createObsFromFuture() {
        Observable<Long> obs = ObsUtils.from(myTimer(10000));

        Subscription s;
        obs.subscribe(l -> System.out.println(l));
    }
}
